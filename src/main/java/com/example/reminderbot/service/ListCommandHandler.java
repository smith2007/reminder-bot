package com.example.reminderbot.service;

import com.example.reminderbot.data.ActiveUser;
import com.example.reminderbot.data.ActiveUserJpaRepository;
import com.example.reminderbot.data.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListCommandHandler implements BaseCommandHandler {

    private ActiveUserJpaRepository activeUserJpaRepository;

    @Override
    public SendMessage hande(Long chatId, String userName) {
        ActiveUser activeUserJpaRepositoryByChatId = activeUserJpaRepository.findByChatId(chatId);
        List<Reminder> allByChatId = activeUserJpaRepositoryByChatId.getReminders();
        if (allByChatId.isEmpty()) {
            return prepareMessage(chatId, "You haven't created any reminders yet.");
        } else {
            String reminders = allByChatId
                    .stream()
                    .map(e -> e.getTitle() + " at " + e.getRemindDateTime() + " " + e.getPeriodicity())
                    .collect(Collectors.joining("\n"));

            return prepareMessage(chatId, "Here all your reminders:\n\n" + reminders);
        }
    }

    @Autowired
    public void setActiveUserJpaRepository(ActiveUserJpaRepository activeUserJpaRepository) {
        this.activeUserJpaRepository = activeUserJpaRepository;
    }
}
