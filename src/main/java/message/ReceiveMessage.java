package message;

import bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Update;

public class ReceiveMessage implements Runnable {
    private static final Logger log = Logger.getLogger(ReceiveMessage.class);
    private Bot bot;

    public ReceiveMessage(Bot b) {
        bot = b;
    }

    @Override
    public void run() {
        while (true) {
            for (Object object = bot.getReceiveQueue().poll(); object != null; object = bot.receiveQueue.poll()) {
                Update update = (Update) object;
                //isCommand
                bot.getSendQueue().add(update);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }

//    public String showAddress(){
//
//    }
}
