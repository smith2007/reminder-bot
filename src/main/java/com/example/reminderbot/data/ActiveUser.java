package com.example.reminderbot.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "active_user")
public class ActiveUser {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "user_name")
    private String userName;

    @Column(name = "chat_id", unique = true)
    private Long chatId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "activeUser", cascade = CascadeType.ALL)
    private List<Reminder> reminders;

    @Column(name = "registration_date_time")
    private LocalDateTime registrationDateTime;

}
