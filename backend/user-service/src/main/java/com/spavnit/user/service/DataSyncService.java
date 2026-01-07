package com.spavnit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Сервис для автоматической синхронизации данных при запуске
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataSyncService {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("User Service готов. Автоматическая синхронизация активна.");
        log.info("Слушаем события из Auth Service через RabbitMQ");
    }
}