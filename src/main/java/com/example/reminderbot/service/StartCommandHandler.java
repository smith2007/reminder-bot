package com.example.reminderbot.service;

import com.example.reminderbot.data.ActiveUser;
import com.example.reminderbot.data.ActiveUserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;

@Service
public class StartCommandHandler implements BaseCommandHandler {

    private ActiveUserJpaRepository activeUserJpaRepository;

    public SendMessage hande(Long chatId, String userName) {
        ActiveUser existingActiveUser = activeUserJpaRepository.findByChatId(chatId);
        if (existingActiveUser == null) {
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUserName(userName);
            activeUser.setChatId(chatId);
            activeUser.setRegistrationDateTime(LocalDateTime.now());
            activeUserJpaRepository.saveAndFlush(activeUser);
            return prepareMessage(chatId, "Hi, " + userName + ", nice to meet you!\n\n"
                    + "Use /new command for create new reminder.\n\n"
                    + "Use /stop command for unsubscribe from all reminders.\n\n"
                    + "Use /list command for fetch all your reminders.");
        } else {
            return prepareMessage(chatId, "Hi, " + userName + ", welcome back!\n\n"
                    + "Use /new command for create new reminder.\n\n"
                    + "Use /stop command for unsubscribe from all reminders.\n\n"
                    + "Use /list command for fetch all your reminders.");
        }
    }

    @Autowired
    public void setActiveUserJpaRepository(ActiveUserJpaRepository activeUserJpaRepository) {
        this.activeUserJpaRepository = activeUserJpaRepository;
    }
}
