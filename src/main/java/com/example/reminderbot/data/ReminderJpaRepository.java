package com.example.reminderbot.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReminderJpaRepository extends JpaRepository<Reminder, UUID> {

    List<Reminder> findAllByRemindDateTimeBetween(LocalDateTime remindDateTimeStart, LocalDateTime remindDateTimeEnd);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Reminder set remindDateTime = :nextRemindDateTime where id = :id")
    void updateHiddenForCustomer(UUID id, LocalDateTime nextRemindDateTime);

}
