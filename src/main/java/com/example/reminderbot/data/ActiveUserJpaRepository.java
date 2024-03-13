package com.example.reminderbot.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActiveUserJpaRepository extends JpaRepository<ActiveUser, UUID> {

    @Transactional
    void deleteByChatId(Long chatId);

    ActiveUser findByChatId(Long chatId);
}
