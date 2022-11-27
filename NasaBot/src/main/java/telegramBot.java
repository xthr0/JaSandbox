import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class telegramBot extends TelegramLongPollingBot {
    // Telegram bot token & name
    public static final String BOT_TOKEN = "";
    public static final String BOT_USERNAME = "";
    public static final String CHAT_ID = "";
    // Nasa api url
    public static final String URL_API = "";

    public telegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            switch (update.getMessage().getText()) {
                case "/help":
                    sendMessage("""
                            Привет, я бот NASA!
                            Я высылаю ссылки на картинки по запросу.\s
                            Картинки на сайте NASA обновляются раз в сутки.""");
                    break;
                case "/give":
                    try {
                        sendMessage(Utils.getUrl(URL_API));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    }


    private void sendMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(CHAT_ID);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
