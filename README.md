# WaniKani Learning App

A modern Android application for WaniKani Japanese learning, built with Jetpack Compose and following Clean Architecture principles.



https://github.com/user-attachments/assets/0312f3ee-aaef-4f0d-871f-a1b132ebb20c



## 🌟 Features

### 🔐 Authentication System
- **WebView-based Login**: Secure authentication through WaniKani's official login page
- **API Key Extraction**: Automatic extraction of personal access tokens from WaniKani settings
- **Secure Storage**: Encrypted storage of API keys using Android's EncryptedSharedPreferences
- **Success Screen**: Clean confirmation screen after successful login

### 📱 User Interface
- **Material 3 Design**: Modern Material Design 3 components and theming
- **Jetpack Compose**: Fully built with declarative UI framework
- **Responsive Layout**: Adapts to different screen sizes and orientations
- **Dark/Light Theme**: Follows system theme preferences

### 🏗️ Architecture
- **Multi-Module Structure**: Organized into feature-based modules for scalability
- **Clean Architecture**: Separation of concerns with data, domain, and presentation layers
- **Repository Pattern**: Abstracted data access with repository interfaces
- **Dependency Injection**: Hilt for clean dependency management
- **MVVM Pattern**: ViewModels for UI state management

## 📁 Project Structure

```
├── app/                          # Main application module
├── core-data/                    # Data layer (repositories, API clients)
├── core-database/                # Room database configuration
├── core-testing/                 # Shared testing utilities
├── core-ui/                      # Shared UI components and theming
├── feature-wanitabi/             # Main feature implementation
├── login/                        # Authentication module
└── test-app/                     # Integration testing
```

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - 100% Kotlin codebase
- **Jetpack Compose** - Modern declarative UI framework
- **Material 3** - Latest Material Design components
- **Hilt** - Dependency injection framework
- **Coroutines & Flow** - Asynchronous programming and reactive streams

### Architecture Components
- **Navigation Compose** - Type-safe navigation between screens
- **ViewModel** - UI-related data holder with lifecycle awareness
- **Room** - Local database with SQLite abstraction
- **StateFlow** - Reactive state management

### Development Tools
- **KSP** - Kotlin Symbol Processing for code generation
- **Version Catalog** - Centralized dependency management
- **ProGuard** - Code obfuscation and optimization

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17+
- Android SDK 24+ (minimum)
- Android SDK 36 (target)

### Building the Project

1. **Clone the repository**
   ```bash
   git clone https://github.com/rui2333/wanikanitabitabi.git
   cd wanikanitabitabi
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync and Build**
   ```bash
   ./gradlew build
   ```

4. **Run the app**
   ```bash
   ./gradlew installDebug
   ```

## 🧪 Testing

### Run Tests
```bash
# Unit tests
./gradlew test

# Instrumentation tests
./gradlew connectedAndroidTest

# Lint checks
./gradlew lint

# All verification tasks
./gradlew check
```

### Test Coverage
- Unit tests for ViewModels and business logic
- Integration tests for repository implementations
- UI tests for critical user flows
- Instrumented tests for WebView functionality

## 📋 Development Commands

Common commands for development workflow:

```bash
# Build debug variant
./gradlew assembleDebug

# Build release variant
./gradlew assembleRelease

# Run lint analysis
./gradlew lintDebug

# Run specific module tests
./gradlew :login:test

# Generate test reports
./gradlew connectedCheck
```

## 🏛️ Architecture Details

### Data Layer (`core-data`)
- Repository interfaces and implementations
- API key verification logic
- Network request handling
- Data transformation and caching

### Database Layer (`core-database`)
- Room database configuration
- Entity definitions
- DAO interfaces
- Database migrations

### UI Layer (`core-ui`)
- Shared Compose components
- Material 3 theming
- Common UI utilities
- Design system tokens

### Feature Modules
- **`login`**: Authentication flow and WebView integration
- **`feature-wanitabi`**: Main learning interface
- Each module follows Clean Architecture principles

## 🔧 Configuration

### API Integration
The app integrates with WaniKani's API v2. Authentication is handled through:
- WebView-based login flow
- Personal access token extraction
- Secure token storage
- Automatic token validation

### Build Variants
- **Debug**: Development builds with debugging enabled
- **Release**: Production builds with optimizations enabled

## 📚 Documentation

- **[CLAUDE.md](CLAUDE.md)** - Development guidance for AI assistants
- **Module Documentation** - Each module contains its own documentation
- **API Documentation** - Generated from code comments

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes following the established patterns
4. Add tests for new functionality
5. Run the verification suite (`./gradlew check`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add documentation for public APIs
- Write unit tests for business logic
- Follow the existing architectural patterns

## 📄 License

This project is licensed under the Apache License 2.0 - see the individual source files for details.

## 🙏 Acknowledgments

- **WaniKani** - For providing the excellent Japanese learning platform
- **Android Team** - For Jetpack Compose and modern Android development tools
- **Material Design** - For the beautiful design system

---

**Built with ❤️ for Japanese learning enthusiasts**
