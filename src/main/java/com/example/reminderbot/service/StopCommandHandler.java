package com.example.reminderbot.service;

import com.example.reminderbot.data.ActiveUserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class StopCommandHandler implements BaseCommandHandler {

    private ActiveUserJpaRepository activeUserJpaRepository;

    public SendMessage hande(Long chatId, String userName) {
        activeUserJpaRepository.deleteByChatId(chatId);
        return prepareMessage(chatId, "Hey " + userName + "! You've been successfully unsubscribed from all your reminders");
    }

    @Autowired
    public void setActiveUserJpaRepository(ActiveUserJpaRepository activeUserJpaRepository) {
        this.activeUserJpaRepository = activeUserJpaRepository;
    }
}
