# Notes App

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/H2_Database-blue?style=for-the-badge" alt="H2 Database" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger" />
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white" alt="Postman" />
</p>

**Notes App** — клиент-серверное приложение для создания, хранения и совместного редактирования текстовых заметок. Пользователь может вести личные записи, а также предоставлять доступ к отдельным заметкам другим пользователям.

## Стек технологий
* **Язык:** Java 17
* **Фреймворк:** Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security)
* **База данных:** H2 Database (файловый режим)
* **Документация API:** Swagger UI (SpringDoc OpenAPI)
* **Тестирование API:** Postman
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

## Планы по развитию
* Внедрение JWT-авторизации (регистрация и логин).
* Реализация функции шаринга заметок.
* Синхронизация обновлений через WebSocket.