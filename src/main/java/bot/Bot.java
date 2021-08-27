package bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@AllArgsConstructor
@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private static final Logger log = Logger.getLogger(Bot.class);
    final int RECONNECT_PAUSE = 10_000;

    @Getter
    @Setter
    String userName;
    @Getter
    @Setter
    String token;
    @Getter
    private final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    @Getter
    public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void onUpdateReceived(Update update){
        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        receiveQueue.add(update);
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                return;
            }
            botConnect();
        }
    }
}
