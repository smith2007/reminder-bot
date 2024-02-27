package com.example.reminderbot.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "reminders")
public class ReminderEntity {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "title")
    private String title;

    @Column(name = "remind_date_time")
    private LocalDateTime remindDateTime;

    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;

}
