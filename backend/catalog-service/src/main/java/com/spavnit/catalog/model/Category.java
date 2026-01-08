package com.spavnit.catalog.model;

/**
 * Категории товаров "Спавн в IT"
 */
public enum Category {
    LIFEHACKS("Лайфхаки", "Полезные советы для работы в IT"),
    CHECKLISTS("Чек-листы", "Чек-листы на испытательный срок"),
    GAMES("Игры", "Игры для офисных посиделок"),
    AI_TOOLS("ИИ-инструменты", "Доступы к платным ИИ-ботам"),
    COURSES("Курсы", "Курсы для прокачки скиллов");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}