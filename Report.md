Отчет по выполнению лабораторной работы: Интеграционное тестирование Spring Boot приложения с использованием Testcontainers

Тема: Интеграционные тесты, Docker, Testcontainers, Kubernetes (подготовка окружения).

1. Цель работы
   Настроить процесс сборки Spring Boot приложения для различных профилей (Dev и Prod), создать соответствующие Docker-образы и реализовать интеграционные тесты с использованием библиотеки Testcontainers для проверки корректности работы приложения в изолированных контейнерах.

2. Использованные технологии и инструменты
   Язык программирования: Java 17 (OpenJDK 17.0.6).
   Фреймворк: Spring Boot 3.5.10 (Spring Web MVC).
   Система сборки: Apache Maven (mvnw).
   Контейнеризация: Docker (версия 29.3.0), базовый образ обновлен до eclipse-temurin:17-jre-alpine (вместо openjdk:8-jdk-alpine)
   Тестирование: JUnit 5, Testcontainers (версия 1.19.8), Spring Boot Test (@SpringBootTest, TestRestTemplate).
   IDE: IntelliJ IDEA Community Edition 2025.
   ОС: Windows / Linux (кроссплатформенная сборка).

3. Выполненные шаги
   3.1. Настройка конфигурации приложения
   Реализована динамическая подмена бинов профиля системы (SystemProfile) в зависимости от переменной netology.profile.dev:
   Создан класс конфигурации JavaConfig с аннотацией @Configuration.
   Использованы аннотации @ConditionalOnProperty для создания бинов DevProfile (при true) и ProductionProfile (при false или отсутствии свойства).
   Настроены файлы application.properties для двух сценариев:
   Dev: server.port=8080, netology.profile.dev=true.
   Prod: server.port=8081, netology.profile.dev=false.
   3.2. Сборка Docker-образов
   Для каждого профиля собран отдельный Docker-образ:
   Сборка артефакта: Выполнена команда ./mvnw clean package для генерации JAR-файла.
   Dockerfile: Создан универсальный Dockerfile, адаптируемый под профиль (изменение порта EXPOSE и имени JAR при необходимости).
   Команда для Dev: docker build -t devapp:latest .
   Команда для Prod: docker build -t prodapp:latest . (с изменением порта на 8081).
   Результат: В локальном реестре Docker имеются два образа: devapp:latest и prodapp:latest.
   3.3. Реализация интеграционных тестов
   Разработан тестовый класс SpringBootDemoApplicationTests: 
   В тестовом классе SpringBootDemoApplicationTests для запуска контейнеров была использована аннотация @Container в связке с @Testcontainers, вместо ручного метода @BeforeAll public static void setUp():
   Аннотации:
   @Testcontainers — для управления жизненным циклом контейнеров. (Замена метода setUp для запуска тестовых контейнера)
   @SpringBootTest(webEnvironment = RANDOM_PORT) — для инициализации тестового контекста Spring и внедрения TestRestTemplate (согласно требованию задания).
   Контейнеры:
   Объявлены два статических поля типа GenericContainer<?>: devapp (порт 8080) и prodapp (порт 8081).
   Контейнеры автоматически запускаются перед выполнением тестов.
   Логика тестов:
   Получение маппированного порта хоста через метод getMappedPort().
   Формирование полного URL запроса (http://localhost:<port>/profile).
   Выполнение HTTP GET запроса через внедренный TestRestTemplate.
   Валидация ответа: проверка HTTP статуса (200 OK) и содержимого тела ответа (наличие строк "Current profile is dev" / "Current profile is production").

4. Полученные результаты
   Успешная сборка: Приложение корректно компилируется и упаковывается в JAR для обоих профилей.
   Работающие образы: Docker-образы devapp и prodapp успешно создаются и запускаются, приложения внутри них стартуют на указанных портах (8080 и 8081 соответственно).
   Прохождение тестов:
   Тест testDevProfile проходит успешно: контейнер devapp стартует, приложение возвращает статус 200 и строку "Current profile is dev".
   Тест testProdProfile проходит успешно: контейнер prodapp стартует, приложение возвращает статус 200 и строку "Current profile is production".
   Изоляция окружения: Тесты выполняются в изолированных Docker-контейнерах, что гарантирует идентичность среды тестирования среде выполнения (Production-like environment).

5. Вывод
   В ходе работы были освоены принципы контейнеризации Spring Boot приложений и написания надежных интеграционных тестов с использованием Testcontainers. Реализован подход, позволяющий проверять работу приложения в разных конфигурациях (Dev/Prod) без необходимости ручного развертывания сервисов на локальной машине. Использование TestRestTemplate в связке с внешними контейнерами позволило провести端到端 (end-to-end) проверку HTTP-слоя приложения.