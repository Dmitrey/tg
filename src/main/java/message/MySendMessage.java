package message;

import bot.Bot;
import entity.Stop;
import entity.Transport;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import service.MyRequest;
import service.StopNameSearcher;
import service.StopService;
import service.TransportService;
import timer.EstimatedTimeTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class MySendMessage implements Runnable {

    private Bot bot;
    private StopService service = new StopService();
    String tramway = "\uD83D\uDE8B";
    String bus = "\uD83D\uDE8C";
    String trolleybus = "\uD83D\uDE8E";
    String transportName = "";
    String stopId = "";

    public MySendMessage(Bot b) {
        bot = b;
    }

    @Override
    public void run() {
        while (true) {
            for (Object obj = bot.getSendQueue().poll(); obj != null; obj = bot.getSendQueue().poll()) {
                Update update = (Update) obj;
                SendMessage sendMessage = new SendMessage();
                String messageText;
                //просто сообщение
                if (update.hasMessage()) {
                    String command = ((Update) obj).getMessage().getText();

                    switch (command) {
                        case "/start":
                            sendMessage.setChatId(update.getMessage().getChatId());
                            sendMessage.setText("Hello, mate! Let me guide you through the darkest transport madness))\n" +
                                    "Enter name of the stop...");
//                            sendMessage.setReplyMarkup(createStopsButtonList());
                            try {
                                bot.execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            if (command.length() < 3) {
                                sendMessage(update.getMessage().getChatId(), "Enter at least 3 symbols");
                                break;
                            }
                            // he-he boi, time to some stupid search))
                            List<Stop> stops = StopNameSearcher.findStop(command);
                            if (stops.isEmpty()) {
                                sendNoStopsFound(update.getMessage().getChatId());
                                break;
                            }

                            for (Stop s : stops) {
                                List<Transport> transports = TransportService.getTransportByStop(s);
                                transports = transports.stream().filter(x -> !x.getFullName().equals("null")).collect(Collectors.toList());
                                sendMessage.setChatId(update.getMessage().getChatId());
                                sendMessage.setText(s.getStopName());
                                sendMessage.setReplyMarkup(createTransportsMarkup(transports));
                                try {
                                    bot.execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                    }
                }

                //callback от нажатия кнопки
                if (update.hasCallbackQuery()) {

                    if (update.getCallbackQuery().getData().split(":")[0].equals("transport")) {
                        String message = update.getCallbackQuery().getData().split(":")[1];
                        transportName = update.getCallbackQuery().getData().split(":")[2];
                        stopId = update.getCallbackQuery().getData().split(":")[3];
                        List<Transport> transportList = MyRequest.getTransports(stopId);
                        transportList = transportList.stream().filter(x -> !x.getFullName().equals("null")).collect(Collectors.toList());
                        for (Transport tr : transportList) {
                            if (tr.getName().equals(transportName)) {
                                message = tr.getEstimatedTime();
                                if (tr.getEstimatedTime().toLowerCase().contains("каждые")) {
                                    message += "\nWant to get real estimated time when available?";
                                    sendMessage.setReplyMarkup(createMarkupForTrackAnswer());
                                }
                            }
                        }
                        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        sendMessage.setText(message);
                        try {
                            bot.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    if (update.getCallbackQuery().getData().equals("answer:yes")) {
                        Timer timer = new Timer();
                        EstimatedTimeTask task = new EstimatedTimeTask(transportName,
                                stopId, timer, update.getCallbackQuery().getMessage().getChatId(), bot);
                        System.out.println("Hi there" + transportName + stopId + timer + update.getCallbackQuery().getMessage().getChatId() + "end");
                        timer.scheduleAtFixedRate(task, 0, 10_000);
                        sendMessage(update.getCallbackQuery().getMessage().getChatId(), "notification will be send");
                    }
                }

            }
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard createMarkupForTrackAnswer() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText("Yes");
        buttonYes.setCallbackData("answer:yes");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText("No");
        buttonNo.setCallbackData("answer:no");
        keyboardButtonsRow.add(buttonYes);
        keyboardButtonsRow.add(buttonNo);
        rows.add(keyboardButtonsRow);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    private void sendNoStopsFound(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("didn't find a stop with this name(");
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard createTransportsMarkup(List<Transport> transports) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Transport t : transports) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String text = "";
            switch (t.getType()) {
                case "Автобус":
                    text += bus;
                    break;
                case "Троллейбус":
                    text += trolleybus;
                    break;
                case "Трамвай":
                    text += tramway;
                    break;
            }
            text += " " + t.getName() + " " + t.getDirection();
            button.setText(text);
            button.setCallbackData("transport:" + t.getEstimatedTime() + ":" + t.getName() + ":" + t.getStopId());
            System.out.println("Transport markup " + t.getEstimatedTime() + ":" + t.getName() + ":" + t.getStopId());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(button);
            rows.add(keyboardButtonsRow);
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    ReplyKeyboard createStopsButtonList() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Stop stop : service.getStopList()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(stop.getStopName());
            button.setCallbackData("stop:" + stop.getId());
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(button);
            rows.add(keyboardButtonsRow);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
}
