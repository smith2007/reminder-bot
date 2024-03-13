package com.example.reminderbot.data;

import com.example.reminderbot.dto.Periodicity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reminder")
public class Reminder {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "title")
    private String title;

    @Column(name = "periodicity")
    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;

    @ManyToOne
    @JoinColumn(name = "active_user_id", nullable = false)
    private ActiveUser activeUser;

    @Column(name = "remind_date_time")
    private LocalDateTime remindDateTime;

    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", periodicity=" + periodicity +
                ", activeUser=" + activeUser +
                ", remindDateTime=" + remindDateTime +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
