
# 🧱 Android Base Project (Modern, Modular, Scalable)

A robust Android base project built with **Kotlin**, **Jetpack Compose**, and **Clean Architecture** principles. Inspired by Google's [Now in Android](https://github.com/android/nowinandroid), this template is optimized for scalability, testability, and production-readiness.

---

## 🚀 Tech Stack
| Tech              | Purpose                                     |
|-------------------|---------------------------------------------|
| Kotlin            | Modern programming language                 |
| Jetpack Compose   | Declarative UI                              |
| Hilt              | Dependency Injection                        |
| Retrofit + OkHttp | Remote API calls                            |
| Room              | Local database (with Kotlin Flow support)   |
| DataStore (Proto) | Key-value & typed preferences               |
| Coroutines + Flow | Async, reactive programming                 |
| Firebase          | Analytics, Messaging, Crashlytics           |
| Navigation        | Screen navigation (Compose Navigation)      |
| GitHub Actions    | CI/CD automation                            |
| Modularization    | Multi-module setup for scalability          |

---
    
```text
YourApp/
├── app/                      # Application entry point: Navigation, DI, Compose setup
│
├── domain/                  # Pure business logic (UseCases, Repository interfaces)
│   ├── repository/
│   └── usecase/
│
├── core/ 
│   ├── common/              # Dispatcher and state
│   └── data/                # Unified data layer (local + remote)
│   │   ├── local/
│   │   │   ├── database/    # Room: Dao, Entity, AppDatabase
│   │   │   └── datastore/   # DataStore: Preferences, Proto
│   │   │
│   │   ├── remote/          # Retrofit, DTO, API services
│   │   │   └── api/
│   │   │
│   │   ├── repository/      # RepositoryImpl - implements domain interfaces
│   │   └── di/              # Hilt Modules: NetworkModule, DatabaseModule, etc.
│   ├── designsystem/        # Theme, Color, Typography, Shapes for the app
│   ├── model/               # Shared data models (Task, User, MoodEntry...)
│   ├── notifications/       # Notifications of app
│   ├── ui/                  # Reusable UI components (e.g. Loading, EmptyView)
│
├── feature/
│   ├── tasks/               # Task management screen
│   │   ├── ui/
│   │   ├── di/
│   │   └── presentation/
│   │
│   ├── pomodoro/            # Pomodoro timer screen
│   ├── moodtracker/         # Mood tracking screen
│   └── onboarding/          # Welcome, login, and register screens
│
└── build.gradle.kts         # Root build configuration

```

---

## ✅ Getting Started

1. **Clone the repository**  
   \`\`\`bash
   git clone git@github.com:KhoaNguyen-0929/AndroidNativeBaseProject.git
   \`\`\`

2. **Open in Android Studio (Giraffe+)**

3. **Add Firebase**  
   Place your \`google-services.json\` in the \`/app\` module.

4. **Run the project**  
   \`\`\`bash
   ./gradlew assembleDebug
   \`\`\`

---

## 🔐 Secrets Management

Use \`local.properties\` or \`.env\` pattern for secrets (API keys, etc.)
> Never commit secrets to version control.

---

## 🤖 GitHub Actions CI

This project includes CI with GitHub Actions:

- Auto-build on PR/Push
- Run unit tests
- Lint & static analysis

Sample workflow: \`.github/workflows/android.yml\`

\`\`\`yaml
- name: Build & Test
  run: ./gradlew build testDebugUnitTest lint detekt
  \`\`\`

---

## 🔍 Testing

| Type        | Tools                      |
|-------------|----------------------------|
| Unit        | JUnit, MockK               |
| UI          | Compose UI Tests           |
| Integration | WorkManager / Paging Tests |

---

## 📈 Roadmap

- [x] Room + DataStore
- [x] Modular Clean Architecture
- [x] Sealed UI State Handling
- [x] Firebase Integration
- [x] GitHub Actions CI
- [ ] Paging 3
- [ ] Offline Sync Strategy
- [ ] Performance (Baseline Profiles)
- [ ] App Theming System

---

## 📄 License

This project is released under the [MIT License](LICENSE).

---

## 🙋 Contribution

Feel free to fork, contribute, and open pull requests.