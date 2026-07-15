# Repository Guidelines

## Project Structure & Module Organization

The project is a Kotlin Multiplatform (Compose Multiplatform) application. All source code lives under `composeApp/src/`:

- `commonMain/kotlin/xyz/catfootbeats/maiup/` — Shared source code (UI, API, ViewModel, data, models)
- `androidMain/kotlin/.../maiup/` — Android-specific implementations (DataStore, URL handling)
- `iosMain/kotlin/.../maiup/` — iOS-specific implementations
- `jvmMain/kotlin/.../maiup/` — Desktop (JVM) entry point and implementations
- `commonMain/composeResources/` — Shared drawable assets
- `commonTest/` — Shared test code

Key source directories under `commonMain`:
- `api/` — HTTP client and API layer (Lxns API)
- `model/` — Data classes, enums, serialization
- `data/` — DataStore persistence and preferences
- `ui/pages/` — Screen-level composables (Home, B50, Rank, Settings, Search)
- `ui/components/` — Reusable composable components
- `ui/theme/` — Theme configuration (Miuix-based)
- `viewmodel/` — ViewModels
- `di/` — Koin dependency injection module
- `utils/` — Platform-agnostic utilities

iOS app entry point is in `iosApp/`. Desktop entry point is `jvmMain/.../main.kt`.

## Build, Test, and Development Commands

```shell
# Run desktop (JVM) application
./gradlew :composeApp:run

# Build Android debug APK
./gradlew :composeApp:assembleDebug

# Run all tests
./gradlew :composeApp:check
```

The project uses Kotlin 2.3.20 with Compose Multiplatform 1.10.3 and the Miuix KMP component library for UI.

## Coding Style & Naming Conventions

- Kotlin code style is `official` (configured in `gradle.properties`)
- UI composables use PascalCase; functions and properties use camelCase
- API model classes use `@Serializable` data classes with `@SerialName` for JSON field mapping
- Compose resources use snake_case file names in `composeResources/drawable/`
- Import ordering: AndroidX → Compose → Ktor → Koin → Project-internal

No static analysis or linting tools are currently configured.

## Testing Guidelines

Tests are written with `kotlin.test` and placed in `commonTest/kotlin/`. Test classes mirror the source structure:

```shell
./gradlew :composeApp:check
```

No coverage thresholds or naming conventions are enforced. Tests are minimal — add meaningful coverage when introducing shared logic.

## Commit & Pull Request Guidelines

Commit messages are written in Chinese and describe the change directly. Examples from history:

- `加入调整库，加入hot reload`
- `完成b50页面`
- `优化代码结构`
- `断网重试，token输入提示`

Pull requests should describe the change, mention any new dependencies, and include screenshots for UI changes. Link related issues when applicable.

## Configuration & Secrets

- API tokens (落雪查分器, 水鱼) are stored via DataStore Preferences, never committed
- The OAuth `client_id` in `SettingsPage.kt` is a public client identifier — treat tokens as sensitive
- Version name is set in `gradle.properties` as `versionName`
