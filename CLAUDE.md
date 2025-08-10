# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Architecture

This is a multi-module Android application following Clean Architecture principles with the following structure:

- **`:app`** - Main application module containing UI navigation and app-level components
- **`:core-data`** - Data layer with repository implementations and API key verification
- **`:core-database`** - Room database configuration and entities
- **`:core-testing`** - Shared testing utilities and Hilt test runner
- **`:core-ui`** - Shared UI components, themes, and design system
- **`:feature-wanitabi`** - Main feature module with screens and ViewModels
- **`:login`** - Authentication module with login screens and WebView-based auth
- **`:test-app`** - Integration testing module

### Key Technologies
- **Jetpack Compose** for UI with Material 3 design
- **Hilt** for dependency injection
- **Room** database with KSP for code generation
- **Coroutines and Flow** for asynchronous operations
- **Navigation Compose** for app navigation
- **OkHttp** for network operations

### Navigation Flow
The app starts with a login screen (`login` destination) and navigates to the main feature screen (`main` destination) after authentication. Navigation is defined in `app/src/main/java/com/wanikanitabitabi/learn/ui/Navigation.kt:33`.

### Repository Pattern
The core data layer uses the repository pattern with `WaniTabiRepository` interface providing methods for:
- User authentication (`login`, `verifyApiKey`, `getUserInfo`)
- Data management (`add`, `waniTabis` flow)

## Development Commands

### Build Commands
- `./gradlew build` - Build all modules
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew assembleRelease` - Build release APK

### Testing Commands
- `./gradlew test` - Run unit tests for all variants
- `./gradlew testDebugUnitTest` - Run debug unit tests only
- `./gradlew connectedAndroidTest` - Run instrumentation tests on connected devices
- `./gradlew connectedCheck` - Run all device checks

### Code Quality Commands
- `./gradlew lint` - Run lint checks on default variant
- `./gradlew lintDebug` - Run lint on debug variant
- `./gradlew lintFix` - Run lint and apply safe fixes
- `./gradlew check` - Run all verification tasks (lint + tests)

### Database Schema
- Room schema files are stored in `core-database/schemas/`
- KSP generates Room code with schema location configured in `app/build.gradle.kts:44`

## Module Dependencies

When working with this codebase:
- The `:app` module depends on `:core-ui`, `:feature-wanitabi`, and `:login`
- Use the existing dependency injection setup with Hilt modules
- Follow the established package naming: `com.wanikanitabitabi.learn.*`
- New features should be created as separate feature modules when appropriate

## Build Configuration

- Minimum SDK: 24
- Target SDK: 36
- Compile SDK: 36
- Java Version: 17
- Kotlin version managed via `gradle/libs.versions.toml`
- Uses KSP for code generation instead of KAPT where possible