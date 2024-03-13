package com.example.reminderbot.telegram;


import com.example.reminderbot.config.BotConfig;
import com.example.reminderbot.service.ListCommandHandler;
import com.example.reminderbot.service.NewCommandHandler;
import com.example.reminderbot.service.StartCommandHandler;
import com.example.reminderbot.service.StopCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    public static final String START = "/start";
    public static final String STOP = "/stop";
    public static final String LIST = "/list";
    public static final String NEW = "/new";
    private final BotConfig botConfig;

    private StartCommandHandler startCommandHandler;

    private StopCommandHandler stopCommandHandler;

    private NewCommandHandler newCommandHandler;

    private ListCommandHandler listCommandHandler;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();
            SendMessage outputMessage = switch (messageText) {
                case START -> startCommandHandler.hande(chatId, userName);
                case STOP -> stopCommandHandler.hande(chatId, userName);
                case NEW -> newCommandHandler.hande(chatId, userName);
                case LIST -> listCommandHandler.hande(chatId, userName);
                default -> newCommandHandler.createNew(chatId, messageText);
            };
            try {
                execute(outputMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Autowired
    public void setStartCommandHandler(StartCommandHandler startCommandHandler) {
        this.startCommandHandler = startCommandHandler;
    }

    @Autowired
    public void setStopCommandHandler(StopCommandHandler stopCommandHandler) {
        this.stopCommandHandler = stopCommandHandler;
    }

    @Autowired
    public void setNewCommandHandler(NewCommandHandler newCommandHandler) {
        this.newCommandHandler = newCommandHandler;
    }

    @Autowired
    public void setListCommandHandler(ListCommandHandler listCommandHandler) {
        this.listCommandHandler = listCommandHandler;
    }
}