# # Notes App

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=openjdk&logoColor=white)](#)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?style=flat&logo=spring-boot&logoColor=white)](#)
[![React](https://img.shields.io/badge/React-18-61DAFB?style=flat&logo=react&logoColor=black)](#)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=flat&logo=apache-maven&logoColor=white)](#)
[![H2](https://img.shields.io/badge/H2_Database-2.x-4479A1?style=flat&logo=databricks&logoColor=white)](#)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=flat&logo=swagger&logoColor=black)](#)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-010101?style=flat&logo=websocket&logoColor=white)](#)
[![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white)](#)
[![Mockito](https://img.shields.io/badge/Mockito-78A641?style=flat)](#)
[![Java CI with Maven](https://github.com/k1onnu/notes-app/actions/workflows/maven.yml/badge.svg)](https://github.com/k1onnu/notes-app/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/k1onnu/notes-app/graph/badge.svg?token=0f986096-3f56-438a-b656-1b3581b57192)](https://codecov.io/gh/k1onnu/notes-app)

**Notes App** — полноценное клиент-серверное приложение (Full-stack) для создания, хранения и совместного редактирования текстовых заметок в режиме реального времени. Пользователь может вести личные записи, а также предоставлять доступ к отдельным заметкам другим пользователям.

## Стек технологий

**Backend:**
* **Язык:** Java 17
* **Фреймворк:** Spring Boot 4 (Spring Web, Spring Data JPA, Spring Security)
* **Безопасность:** JWT (JSON Web Tokens)
* **База данных:** H2 Database (файловый режим)
* **Документация API:** Swagger UI (SpringDoc OpenAPI)
* **Real-time:** WebSocket (STOMP, SockJS)

**Frontend:**
* **Фреймворк:** React (Vite)
* **HTTP-клиент:** Axios
* **Маршрутизация:** React Router DOM
* **UI:** Bootstrap 5, React-Toastify

**Тестирование и CI/CD:**
* JUnit 5, Mockito, MockMvc
* GitHub Actions (CI), JaCoCo + Codecov

## Структура проекта

```text
notes-app/
├── frontend/                     # Клиентская часть (React)
│   ├── src/
│   │   ├── api/                  # Настройка Axios и перехватчиков
│   │   ├── components/           # UI-компоненты (Button, Input, Table, Modal)
│   │   ├── pages/                # Страницы (Home, Login, Notes, NotFound)
│   │   ├── App.jsx               # Роутинг и Layout
│   │   └── main.jsx              # Точка входа React
│   ├── package.json
│   └── vite.config.js
│
├── src/main/java/com/k1onnu/notes_app/  # Серверная часть (Spring Boot)
│   ├── config/                   # Настройки (Security, WebSocket, OpenAPI)
│   ├── controller/               # REST-контроллеры (Auth, Note, User)
│   ├── dto/                      # Data Transfer Objects (запросы и ответы)
│   ├── exception/                # Кастомные исключения и GlobalExceptionHandler
│   ├── model/                    # JPA-сущности (User, Note, NoteAccess)
│   ├── repository/               # Spring Data JPA репозитории
│   ├── security/                 # JwtProvider и JwtAuthFilter
│   ├── service/                  # Бизнес-логика (AuthService, NoteService)
│   └── NotesAppApplication.java  # Точка входа Spring Boot
│
├── src/test/java/...             # Тесты (Unit + Integration)
├── pom.xml                       # Maven-зависимости
├── notes-app.postman_collection.json # Коллекция запросов для Postman
└── README.md
```

## Как запустить проект

### 1. Запуск бэкенда (Spring Boot)
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/k1onnu/notes-app.git
   ```
2. Откройте корневую папку проекта в IntelliJ IDEA.
3. Запустите класс `NotesAppApplication` (порт `8080`).

### 2. Запуск фронтенда (React)
1. В терминале перейдите в папку фронтенда:
   ```bash
   cd frontend
   ```
2. Установите зависимости:
   ```bash
   npm install
   ```
3. Запустите сервер разработки:
   ```bash
   npm run dev
   ```
4. Приложение откроется по адресу: `http://localhost:5173`.

## Документация и доступы

* **Swagger UI** (Интерактивная документация API):
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

* **H2 Console** (Веб-интерфейс базы данных):
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  * **JDBC URL:** `jdbc:h2:file:./data/notesdb`
  * **User Name:** `sa`
  * **Password:** (оставить пустым)

## Тестирование API
В корне проекта находится файл `notes-app.postman_collection.json`. Импортируйте его в Postman (File -> Import), чтобы получить готовую коллекцию запросов.

## Реализованный функционал
* **Аутентификация и авторизация:** Регистрация и вход с использованием JWT. Пароли хешируются (BCrypt).
* **CRUD:** Полный цикл работы с заметками (создание, чтение, обновление, удаление) и пользователями.
* **Изоляция данных:** Каждый пользователь видит только свои заметки.
* **Совместный доступ (Шаринг):** Возможность делиться заметкой с другим пользователем с выбором прав (чтение или редактирование), а также отзывать доступ.
* **Real-time синхронизация:** Благодаря WebSocket изменения в заметке мгновенно отображаются у всех пользователей, имеющих к ней доступ.
* **Пагинация, поиск и сортировка:** Для удобной навигации по списку заметок.
* **Обработка ошибок:** Глобальный обработчик исключений (400, 401, 403, 404, 409, 500).
* **Валидация:** Проверка входных данных на уровне DTO.
* **Покрытие тестами:** Unit-тесты (Mockito) и интеграционные тесты (MockMvc).