package com.example.reminderbot.telegram;


import com.example.reminderbot.config.BotConfig;
import com.example.reminderbot.data.ReminderEntity;
import com.example.reminderbot.data.ReminderRepository;
import com.example.reminderbot.dto.CurrencyModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Autowired
    ReminderRepository reminderRepository;

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

            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else {
                String[] splittedTextMessage = messageText.split(" ");

                if (splittedTextMessage.length != 2) {
                    sendMessage(chatId, "Sorry the format of message is invalid. PLease use such pattern:" +
                            "<Title> yyyy-MM-dd like : Parking 2024-03-19");
                } else {
                    ReminderEntity reminderEntity = new ReminderEntity();
                    String remindDate = splittedTextMessage[1];
                    String title = splittedTextMessage[0];
                    reminderEntity.setTitle(title);
                    reminderEntity.setRemindDateTime(LocalDateTime.parse(remindDate+"T10:00:00"));
                    reminderEntity.setCreateDateTime(LocalDateTime.now());
                    reminderRepository.insertReminder(reminderEntity);
                    sendMessage(chatId, "Reminder saved");
                }
                sendMessage(chatId, "bla bla");
            }
        }

    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "Please add your reminder in format: yyyy-MM-dd like : Parking 2024-03-19";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}