package com.example.reminderbot.data;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class ReminderRepository extends BaseRepository<ReminderEntity> {

    @Transactional
    public void insertReminder(ReminderEntity reminder) {
        this.em.persist(reminder);
    }
}
