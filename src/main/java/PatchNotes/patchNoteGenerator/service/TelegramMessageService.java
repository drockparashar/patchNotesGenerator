package PatchNotes.patchNoteGenerator.service;

import PatchNotes.patchNoteGenerator.config.TelegramBotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TelegramMessageService extends DefaultAbsSender {

    private final TelegramBotConfig telegramBotConfig;

    protected TelegramMessageService(TelegramBotConfig telegramBotConfig){
        super(new DefaultBotOptions(), telegramBotConfig.getBotToken());
        this.telegramBotConfig=telegramBotConfig;
    }

    public void sendMessageToGroup(String message){
        String chatId=telegramBotConfig.getChatId();
        SendMessage text=new SendMessage();
        text.setChatId(chatId);
        text.setText(message);

        try{
            execute(text);
            log.info("Message sent successfully to group {}: {}", chatId, message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to group {}: {}", chatId, e.getMessage(), e);
        }
    }

}
