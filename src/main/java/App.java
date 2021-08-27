import bot.Bot;
import message.MySendMessage;
import message.ReceiveMessage;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import service.MyRequest;

import java.io.IOException;

public class App {
    private static final Logger log = Logger.getLogger(App.class);
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;

    public static void main(String[] args) throws IOException {

        MyRequest.getTransportList();

        ApiContextInitializer.init();
        Bot bot = new Bot("onlineSercherBot","1680997109:AAFlQx5CM88y4PY-i3MQVsC-LFCuq3XAlFg");
        bot.botConnect();

        ReceiveMessage receiveMessage = new ReceiveMessage(bot);
        MySendMessage mySendMessage = new MySendMessage(bot);

        Thread receiver = new Thread(receiveMessage);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(mySendMessage);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

    }
}
