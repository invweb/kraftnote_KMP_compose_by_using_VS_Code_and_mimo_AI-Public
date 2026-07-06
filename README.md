# KraftNote

A cross-platform page editor built with **Kotlin Multiplatform (KMP)**. One shared codebase compiles to Android, Desktop, and Web targets, with a shared server backend.

> **Note:** This project uses **Kotlin Multiplatform (KMP)** — the general cross-platform technology that supports Android, iOS, Desktop, Web, and Server. It is **not** KMM (Kotlin Multiplatform Mobile), which is a subset of KMP limited to Android and iOS only.

## Platforms

| Platform | Stack |
|----------|-------|
| **Android** | Compose Multiplatform + Ktor Client |
| **Desktop** (Windows, macOS, Linux) | Compose Desktop + Ktor Client |
| **Web** (Wasm) | Compose for Web + Ktor Client |
| **Server** | Ktor Server + Exposed + H2 |

All platforms connect to a shared Ktor server and synchronize data in real time (polling every 3 seconds).

## Project Structure

```
complex/
├── shared/            # Shared KMP module (UI, ViewModel, models, API client)
├── androidApp/        # Android application
├── desktopApp/        # Desktop application (Compose Desktop)
├── webApp/            # Web application (Kotlin/Wasm)
├── server/            # Ktor backend (REST API + H2 database)
└── gradle/            # Version catalog (libs.versions.toml)
```

## Getting Started

### Server (required for all clients)

```bash
./gradlew :server:run
```

Server starts at `http://localhost:8080`.

### Desktop

```bash
./gradlew :desktopApp:run
```

### Web

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Open `http://localhost:8081/` in your browser.

### Android

```bash
./gradlew :androidApp:assembleDebug
adb install androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

> On the emulator, the server is accessible via `10.0.2.2:8080`.

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/pages` | List all pages |
| `GET` | `/api/pages/{id}` | Get a page by ID |
| `POST` | `/api/pages` | Create a page |
| `PUT` | `/api/pages/{id}` | Update a page |
| `DELETE` | `/api/pages/{id}` | Delete a page |

## Localization

Supported languages: **English**, **Russian**, **German**.

Language is determined automatically based on device settings. Resources are located in `shared/src/commonMain/composeResources/values*/`.

## Tech Stack

- **Kotlin Multiplatform (KMP) 2.1.0** — cross-platform language
- **Compose Multiplatform 1.7.1** — UI framework
- **Ktor 3.0.3** — HTTP client/server
- **Exposed 0.44.1** — ORM
- **H2 2.3.232** — file-based database
- **kotlinx.serialization** — JSON
- **kotlinx.coroutines** — async
- **kotlinx-datetime** — date/time utilities
