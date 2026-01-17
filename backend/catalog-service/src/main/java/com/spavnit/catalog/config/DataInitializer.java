package com.spavnit.catalog.config;

import com.spavnit.catalog.model.Category;
import com.spavnit.catalog.model.Product;
import com.spavnit.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Инициализация тестовых товаров при запуске приложения
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            log.info("База данных уже содержит товары, пропускаем инициализацию");
            return;
        }

        log.info("Инициализация каталога товаров \"Спавн в IT\"...");

        List<Product> products = List.of(
                // ЛАЙФХАКИ
                Product.builder()
                        .name("Как не быть нитакусей")
                        .description("Лайфхаки общения с коллегами, которые помогут вам не выглядеть новичком. " +
                                "Узнайте, как правильно задавать вопросы, просить помощь и строить отношения в команде.")
                        .price(new BigDecimal("15.00"))
                        .category(Category.LIFEHACKS)
                        .image("http://localhost:5173/images/products/1.jpg")
                        .downloadLink("https://drive.google.com/file/d/lifehack1")
                        .build(),

                Product.builder()
                        .name("Гей-шутки с начальством: норм или стрем?")
                        .description("Гайд по офисному юмору: где граница между дружелюбностью и фамильярностью. " +
                                "Как поддерживать лёгкую атмосферу, не теряя профессионализм.")
                        .price(new BigDecimal("12.00"))
                        .category(Category.LIFEHACKS)
                        .image("https://example.com/images/humor.jpg")
                        .downloadLink("https://drive.google.com/file/d/lifehack2")
                        .build(),

                Product.builder()
                        .name("Как выжить на первом спринте")
                        .description("Пошаговая инструкция для джунов: от стендапа до ретроспективы. " +
                                "Что делать, когда задача горит, а ты не понимаешь половину терминов.")
                        .price(new BigDecimal("18.00"))
                        .category(Category.LIFEHACKS)
                        .image("https://example.com/images/sprint.jpg")
                        .downloadLink("https://drive.google.com/file/d/lifehack3")
                        .build(),

                // ЧЕК-ЛИСТЫ
                Product.builder()
                        .name("Чек-лист испытательного срока для разработчика")
                        .description("Все, что нужно сделать в первые 3 месяца: от настройки окружения до первого мерж реквеста. " +
                                "Включает чек-лист коммуникации с HR и командой.")
                        .price(new BigDecimal("20.00"))
                        .category(Category.CHECKLISTS)
                        .image("https://example.com/images/dev-checklist.jpg")
                        .downloadLink("https://drive.google.com/file/d/checklist1")
                        .build(),


                Product.builder()
                        .name("Чек-лист испытательного срока для тестировщика")
                        .description("Как доказать свою ценность за 3 месяца: от написания тест-кейсов до нахождения критических багов. " +
                                "Бонус: как закентиться с командой разработки.")
                        .price(new BigDecimal("20.00"))
                        .category(Category.CHECKLISTS)
                        .image("https://example.com/images/qa-checklist.jpg")
                        .downloadLink("https://drive.google.com/file/d/checklist2")
                        .build(),

                Product.builder()
                        .name("Чек-лист испытательного срока для аналитика")
                        .description("Гайд по выживанию для системных аналитиков: от сбора требований до защиты документации. " +
                                "Как стать незаменимым связующим звеном команды.")
                        .price(new BigDecimal("20.00"))
                        .category(Category.CHECKLISTS)
                        .image("https://example.com/images/analyst-checklist.jpg")
                        .downloadLink("https://drive.google.com/file/d/checklist3")
                        .build(),

                // ИГРЫ
                Product.builder()
                        .name("Покер: Увольнение коллеги")
                        .description("Настольная игра для корпоративов. Делайте ставки не на деньги, а на увольнение самого надоедливого коллеги. " +
                                "В комплекте: карты, фишки, инструкция по психологическому давлению.")
                        .price(new BigDecimal("25.00"))
                        .category(Category.GAMES)
                        .image("https://example.com/images/poker.jpg")
                        .downloadLink("https://drive.google.com/file/d/game1")
                        .build(),

                Product.builder()
                        .name("Мафия: Офисное издание")
                        .description("Классическая мафия, адаптированная под офисные реалии. Роли: Тимлид, Продакт, Джун, HR. " +
                                "Узнайте, кто из коллег саботирует проект!")
                        .price(new BigDecimal("22.00"))
                        .category(Category.GAMES)
                        .image("https://example.com/images/mafia.jpg")
                        .downloadLink("https://drive.google.com/file/d/game2")
                        .build(),

                Product.builder()
                        .name("Монополия: Зарплата босса")
                        .description("Играйте на повышение или понижение зарплаты вашего начальника. " +
                                "Стройте офисные империи и банкротьте конкурентов. Чистый капитализм!")
                        .price(new BigDecimal("30.00"))
                        .category(Category.GAMES)
                        .image("https://example.com/images/monopoly.jpg")
                        .downloadLink("https://drive.google.com/file/d/game3")
                        .build(),

                // ИИ-ИНСТРУМЕНТЫ
                Product.builder()
                        .name("ChatGPT Plus - доступ на месяц")
                        .description("Полноценный доступ к ChatGPT Plus. Пока вы думаете своей головой, " +
                                "ваш начальник с помощью GPT решает, кого повысить, а кого уволить.")
                        .price(new BigDecimal("35.00"))
                        .category(Category.AI_TOOLS)
                        .image("https://example.com/images/chatgpt.jpg")
                        .downloadLink("https://drive.google.com/file/d/ai1")
                        .build(),



                Product.builder()
                        .name("Claude Pro - доступ на месяц")
                        .description("Доступ к Claude Pro для решения сложных задач. Идеален для написания документации, " +
                                "которую никто не читает, но все требуют.")
                        .price(new BigDecimal("35.00"))
                        .category(Category.AI_TOOLS)
                        .image("https://example.com/images/claude.jpg")
                        .downloadLink("https://drive.google.com/file/d/ai2")
                        .build(),

                Product.builder()
                        .name("Midjourney - 200 генераций")
                        .description("Пакет из 200 генераций изображений. Идеально для создания презентаций, " +
                                "которые выглядят профессионально, даже если контент так себе.")
                        .price(new BigDecimal("40.00"))
                        .category(Category.AI_TOOLS)
                        .image("https://example.com/images/midjourney.jpg")
                        .downloadLink("https://drive.google.com/file/d/ai3")
                        .build(),

                // КУРСЫ
                Product.builder()
                        .name("Junior → Middle за 6 месяцев")
                        .description("Интенсивный курс для джунов, которые устали получать джунские зарплаты. " +
                                "Научитесь паттернам, алгоритмам и главное - как продавать себя дороже.")
                        .price(new BigDecimal("50.00"))
                        .category(Category.COURSES)
                        .image("https://example.com/images/junior-middle.jpg")
                        .downloadLink("https://drive.google.com/file/d/course1")
                        .build(),

                Product.builder()
                        .name("Middle → Senior: Искусство делегирования")
                        .description("Как перестать делать всё самому и начать руководить. Узнайте секреты сеньоров: " +
                                "правильное делегирование, ментворство и как выглядеть занятым на совещаниях.")
                        .price(new BigDecimal("65.00"))
                        .category(Category.COURSES)
                        .image("https://example.com/images/middle-senior.jpg")
                        .downloadLink("https://drive.google.com/file/d/course2")
                        .build(),

                Product.builder()
                        .name("Performance Review: Как получить повышение")
                        .description("Подготовка к Performance Review: как правильно презентовать свои достижения, " +
                                "договориться о повышении и не сгореть в процессе. ИИ вам тут не помощник!")
                        .price(new BigDecimal("45.00"))
                        .category(Category.COURSES)
                        .image("https://example.com/images/performance.jpg")
                        .downloadLink("https://drive.google.com/file/d/course3")
                        .build()
        );

        productRepository.saveAll(products);

        log.info("Каталог инициализирован! Добавлено {} товаров", products.size());
        log.info("Категории:");
        log.info("  - Лайфхаки: 3 товара");
        log.info("  - Чек-листы: 3 товара");
        log.info("  - Игры: 3 товара");
        log.info("  - ИИ-инструменты: 3 товара");
        log.info("  - Курсы: 3 товара");
    }
}