# Complex

Kotlin Multiplatform приложение для создания и редактирования страниц с поддержкой Markdown.

## Платформы

| Платформа | Стек |
|-----------|------|
| **Android** | Compose Multiplatform + Ktor Client |
| **Desktop** (Windows, macOS, Linux) | Compose Desktop + Ktor Client |
| **Web** (Wasm) | Compose for Web + Ktor Client |
| **Server** | Ktor Server + Exposed + H2 |

Все платформы подключаются к общему Ktor-серверу и синхронизируют данные в реальном времени (опрос каждые 3 сек).

## Структура проекта

```
complex/
├── shared/            # Общий KMP модуль (UI, ViewModel, модели, API-клиент)
├── androidApp/        # Android приложение
├── desktopApp/        # Desktop приложение (Compose Desktop)
├── webApp/            # Web приложение (Kotlin/Wasm)
├── server/            # Ktor backend (REST API + H2 база)
└── gradle/            # Version catalog (libs.versions.toml)
```

## Запуск

### Сервер (обязателен для работы всех клиентов)

```bash
./gradlew :server:run
```

Сервер стартует на `http://localhost:8080`.

### Desktop

```bash
./gradlew :desktopApp:run
```

### Web

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Откройте `http://localhost:8081/` в браузере.

### Android

```bash
./gradlew :androidApp:assembleDebug
adb install androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

> На эмуляторе сервер доступен через `10.0.2.2:8080`.

## API

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/api/pages` | Все страницы |
| `GET` | `/api/pages/{id}` | Страница по ID |
| `POST` | `/api/pages` | Создать страницу |
| `PUT` | `/api/pages/{id}` | Обновить страницу |
| `DELETE` | `/api/pages/{id}` | Удалить страницу |

## Локализация

Поддерживаемые языки: **English**, **Русский**, **Deutsch**.

Определяются автоматически по настройкам устройства. Ресурсы в `shared/src/commonMain/composeResources/values*/`.

## Стек технологий

- **Kotlin 2.1.0** — язык
- **Compose Multiplatform 1.7.1** — UI
- **Ktor 3.0.3** — HTTP клиент/сервер
- **Exposed 0.44.1** — ORM
- **H2 2.3.232** — файловая база данных
- **kotlinx.serialization** — JSON
- **kotlinx.coroutines** — асинхронность
- **kotlinx-datetime** — работа со временем
