package com.example.reminderbot.scheduler;

import com.example.reminderbot.data.Reminder;
import com.example.reminderbot.data.ReminderJpaRepository;
import com.example.reminderbot.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class ReminderScheduler {

    private ReminderJpaRepository reminderJpaRepository;

    private TelegramBot telegramBot;

    @Scheduled(cron = "0 * * * * *")
    public void scheduleTaskUsingCronExpression() {
        LocalDateTime timeStart = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime timeEnd = timeStart.plusMinutes(1);
        List<Reminder> allByRemindDateTimeBetween = reminderJpaRepository.findAllByRemindDateTimeBetween(timeStart, timeEnd);
        log.info("FOUND " + allByRemindDateTimeBetween.size());
        for (Reminder reminder : allByRemindDateTimeBetween) {
            SendMessage message = new SendMessage();
            message.setChatId(reminder.getActiveUser().getChatId());
            message.setText("Hey " + reminder.getActiveUser().getUserName() +
                    "! You asked us to remind you something at this time: " + reminder.getTitle());
            try {
                telegramBot.execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            ChronoUnit periodicityUnit = switch (reminder.getPeriodicity()) {
                case ONE_TIME -> null;
                case DAILY -> ChronoUnit.DAYS;
                case WEEKLY -> ChronoUnit.WEEKS;
                case MONTHLY -> ChronoUnit.MONTHS;
                case ANNUALLY -> ChronoUnit.YEARS;
            };
            if (periodicityUnit == null) {
                reminderJpaRepository.deleteById(reminder.getId());
            } else {
                reminderJpaRepository.updateHiddenForCustomer(reminder.getId(), reminder.getRemindDateTime().plus(1, periodicityUnit));
            }
        }
    }


    @Autowired
    public void setReminderJpaRepository(ReminderJpaRepository reminderJpaRepository) {
        this.reminderJpaRepository = reminderJpaRepository;
    }

    @Autowired
    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

}
