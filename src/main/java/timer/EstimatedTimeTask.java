package timer;

import bot.Bot;
import entity.Transport;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import service.MyRequest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EstimatedTimeTask extends TimerTask {

    private String transportName;
    private String stopId;
    private Timer timer;
    private Long chatId;
    private Bot bot;

    public EstimatedTimeTask(String transportName, String stopId, Timer timer, Long chatId, Bot bot) {
        this.transportName = transportName;
        this.stopId = stopId;
        this.timer = timer;
        this.chatId = chatId;
        this.bot = bot;
    }

    @Override
    public void run() {
        List<Transport> transportList = MyRequest.getTransports(stopId);
        for (Transport tr: transportList) {
            if (tr.getName().equals(transportName) && !tr.getEstimatedTime().toLowerCase().contains("каждые") && !tr.getFullName().equals("null")){
                timer.cancel();
                sendMessage(tr);
            }
        }
    }

    private void sendMessage(Transport transport){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(transport.getFullName() +" прибудет через " + transport.getEstimatedTime());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
