# Notes App

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=openjdk&logoColor=white)](#)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?style=flat&logo=spring-boot&logoColor=white)](#)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=flat&logo=apache-maven&logoColor=white)](#)
[![H2](https://img.shields.io/badge/H2_Database-2.x-4479A1?style=flat&logo=databricks&logoColor=white)](#)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=flat&logo=swagger&logoColor=black)](#)
[![Postman](https://img.shields.io/badge/Postman-Collection-FF6C37?style=flat&logo=postman&logoColor=white)](#)
[![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white)](#)

**Notes App** — клиент-серверное приложение для создания, хранения и совместного редактирования текстовых заметок. Пользователь может вести личные записи, а также предоставлять доступ к отдельным заметкам другим пользователям.

## Стек технологий
* **Язык:** Java 17
* **Фреймворк:** Spring Boot 4 (Spring Web, Spring Data JPA, Spring Security)
* **База данных:** H2 Database (файловый режим)
* **Документация API:** Swagger UI (SpringDoc OpenAPI)
* **Тестирование:** Postman, JUnit 5, MockMvc
* **Сборка:** Maven

## Как запустить проект

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/k1onnu/notes-app.git
   ```
2. Откройте проект в IntelliJ IDEA (как Maven-проект, дождитесь загрузки зависимостей).
3. Запустите приложение (запустите класс `NotesAppApplication`).
4. Приложение будет доступно по адресу: `http://localhost:8080`.

## Документация и доступы

* **Swagger UI** (Интерактивная документация API):
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

* **H2 Console** (Веб-интерфейс базы данных):
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  * **JDBC URL:** `jdbc:h2:file:./data/notesdb`
  * **User Name:** `sa`
  * **Password:** (оставить пустым)

## Тестирование API
В корне проекта находится файл `notes-app.postman_collection.json`.
Импортируйте его в Postman (File -> Import), чтобы получить готовую коллекцию запросов для проверки всех эндпоинтов.

## Реализованный функционал (на текущий момент)
* Полный CRUD для сущности **Note** (Заметки), включая полное (PUT) и частичное (PATCH) обновление.
* Полный CRUD для сущности **User** (Пользователи).
* Валидация входных данных (DTO).
* Пагинация при получении списка заметок.
* Проверка на существование записей перед обновлением/удалением.
* Глобальная обработка ошибок (404, 409, 400).
* Написаны Unit-тесты для проверки обработки ошибок (JUnit 5, MockMvc).

## Планы по развитию
* Внедрение JWT-авторизации (регистрация и логин).
* Реализация функции шаринга заметок.
* Синхронизация обновлений через WebSocket.