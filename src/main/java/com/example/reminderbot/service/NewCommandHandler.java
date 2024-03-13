package com.example.reminderbot.service;

import com.example.reminderbot.data.ActiveUser;
import com.example.reminderbot.data.ActiveUserJpaRepository;
import com.example.reminderbot.data.Reminder;
import com.example.reminderbot.data.ReminderJpaRepository;
import com.example.reminderbot.dto.Periodicity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
public class NewCommandHandler implements BaseCommandHandler {

    private ReminderJpaRepository reminderJpaRepository;

    private ActiveUserJpaRepository activeUserJpaRepository;

    @Override
    public SendMessage hande(Long chatId, String userName) {
        return prepareMessage(chatId, """
                Please add your reminder in format: <Remind Title> <one_time/daily/weekly/monthly/annually> yyyy-MM-ddTHH:ss

                Like : Parking monthly 2024-03-19T10:00""");
    }

    public SendMessage createNew(Long chatId, String messageText) {
        String[] splittedTextMessage = messageText.trim().split(" ");
        if (splittedTextMessage.length != 3) {
            return prepareMessage(chatId, "Sorry the format of input string is invalid. You've entered "
                    + messageText + ". Please use such pattern:" +
                    "<Remind Title> <one_time/daily/weekly/monthly/annually> yyyy-MM-ddTHH:ss" +
                    "\n\nLike : Parking monthly 2024-03-19T10:00");
        } else {
            String title = splittedTextMessage[0];
            if (!StringUtils.hasText(title)) {
                return prepareMessage(chatId, "Sorry the format of remind title is invalid.");
            }

            String periodicity = splittedTextMessage[1];

            if (Stream.of(Periodicity.values()).noneMatch(e -> e.name().equalsIgnoreCase(periodicity))) {
                return prepareMessage(chatId, "Sorry the format of input string is invalid. You've entered "
                        + messageText + ". Please use such pattern:" +
                        "<Remind Title> <one_time/daily/weekly/monthly/annually> yyyy-MM-ddTHH:ss" +
                        "\n\nLike : Parking monthly 2024-03-19T10:00");
            }

            String remindDate = splittedTextMessage[2];
            LocalDateTime parsedRemindDateTime = null;
            try {
                parsedRemindDateTime = LocalDateTime.parse(remindDate);
            } catch (RuntimeException e) {
                return prepareMessage(chatId, "Sorry the format of date time is invalid. You've entered "
                        + remindDate + ". Please use such pattern:" +
                        "yyyy-MM-ddTHH:ss like : 2024-03-19T10:00");
            }

            ActiveUser user = activeUserJpaRepository.findByChatId(chatId);

            if (user == null) {
                prepareMessage(chatId, "Please start your session using /start command");
            }

            Reminder reminder = new Reminder();
            reminder.setTitle(title);
            reminder.setPeriodicity(Periodicity.valueOf(periodicity.toUpperCase()));
            reminder.setRemindDateTime(parsedRemindDateTime);
            reminder.setCreateDateTime(LocalDateTime.now());
            reminder.setActiveUser(user);
            reminderJpaRepository.saveAndFlush(reminder);
            return prepareMessage(chatId, "Reminder saved");
        }
    }

    @Autowired
    public void setReminderJpaRepository(ReminderJpaRepository reminderJpaRepository) {
        this.reminderJpaRepository = reminderJpaRepository;
    }

    @Autowired
    public void setActiveUserJpaRepository(ActiveUserJpaRepository activeUserJpaRepository) {
        this.activeUserJpaRepository = activeUserJpaRepository;
    }
}
