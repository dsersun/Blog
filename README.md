# Simple Blog Application

## Описание
Проект "Simple Blog" – это веб-приложение для публикации и управления блогами. Приложение позволяет пользователям создавать, редактировать, публиковать и удалять посты. Также поддерживается функционал блокировки и разблокировки пользователей, а также управление статусами публикаций.

## Основные Функции
- **CRUD Операции для Постов**: Создание, чтение, обновление и удаление постов. Так же публикация и скрытие постов.
- **Управление Пользователями**: Регистрация, аутентификация и управление правами пользователей.
- **Фильтрация Постов**: Поиск постов по тегам, авторам и статусам публикаций.
- **Блокировка и Разблокировка Пользователей**: Возможность блокировки пользователей с обновлением статуса их постов.
- **Комментирование постов**: Возможность оставлять комментарии (зарегистрированными пользователями), простановка лайков и дизлайков. 
- **Асинхронная Отправка Писем**: Уведомления пользователям о блокировке/разблокировке их учетных записей.

## Технологии
- **Spring Boot**: Для создания веб-приложения.
- **Spring Security**: Для аутентификации и авторизации.
- **JPA/Hibernate**: Для взаимодействия с базой данных.
- **RabbitMQ/ActiveMQ/Kafka** (опционально): Для асинхронной обработки задач, например, отправки электронных писем.
- **Thymeleaf**: Для серверной генерации HTML.
- **PostgreSQL/MySQL**: В качестве системы управления базами данных.
- **Технологии фронтенда не определены.**

## Как Запустить
1. **Настройка Базы Данных**: Создайте базу данных и настройте параметры подключения в `application.properties`.
2. **Запуск Приложения**: Запустите приложение с помощью Spring Boot. Для этого можно использовать Maven:
   ```
   mvn spring-boot:run
   ```

## Конфигурация
Все основные параметры конфигурации приложения находятся в файле `application.properties`. Это включает настройки базы данных, почтового сервера и другие параметры приложения.

---