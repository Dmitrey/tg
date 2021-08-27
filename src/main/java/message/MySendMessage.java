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
import service.StopService;

import java.util.ArrayList;
import java.util.List;

public class MySendMessage implements Runnable {

    private Bot bot;
    private StopService service = new StopService();

    public MySendMessage(Bot b) {
        bot = b;
    }

    @Override
    public void run() {
        while (true) {
            for (Object obj = bot.getSendQueue().poll(); obj != null; obj = bot.getSendQueue().poll()) {
                Update update = (Update) obj;

                if (update.hasMessage()) {
                    String command = ((Update) obj).getMessage().getText();

                    switch (command) {
                        case "/selectstop":
                            List<Stop> stopList = service.getStopList();
                            StringBuilder message = new StringBuilder();
                            for (Stop stop : stopList) {
                                message.append(stop.getMyName()).append("\n");
                            }

                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(update.getMessage().getChatId());
                            sendMessage.setText(message.toString());
                            sendMessage.setReplyMarkup(createStopsButtonList());
                            try {
                                bot.execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                    }
                }

                if (update.hasCallbackQuery()) {
                    if (update.getCallbackQuery().getData().split(":")[0].equals("stop")) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        sendMessage.setText("transports");
                        sendMessage.setReplyMarkup(createTransportButtonList());
                        try {
                            bot.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        sendMessage.setText(update.getCallbackQuery().getData());
                        try {
                            System.out.println("request");
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                        try {
                            bot.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

    private ReplyKeyboard createTransportButtonList() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        List<Transport> transportList = MyRequest.getTransportList();
        for (Transport t: transportList) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(t.getName());
            button.setCallbackData("transport:");
            keyboardButtonsRow.add(button);
        }
        rows.add(keyboardButtonsRow);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    ReplyKeyboard createStopsButtonList() {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Stop stop : service.getStopList()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(stop.getMyName());
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
