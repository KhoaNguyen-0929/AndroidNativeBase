# 📄 White Paper: BalanceMe – Android Multi-Module Application

## 1. Giới thiệu

**BalanceMe** là ứng dụng Android đa module, giúp người dùng quản lý công việc, lập kế hoạch, và nâng cao sự tập trung thông qua kỹ thuật Pomodoro và hệ thống lập lịch khoa học. Ứng dụng được phát triển với kiến trúc sạch (Clean Architecture) cùng **Jetpack Compose**, mang đến UI hiện đại, reactive, dễ bảo trì, và tối ưu trải nghiệm người dùng trên mọi thiết bị.

---

## 2. Mục tiêu dự án

✅ Xây dựng nền tảng vững chắc, dễ mở rộng và bảo trì
✅ Áp dụng **Clean Architecture + Multi-Module** để tối ưu thời gian build, rõ ràng trách nhiệm từng module
✅ Sử dụng **Jetpack Compose** nhằm hiện đại hóa UI, tăng tốc phát triển, và tối ưu performance
✅ Triển khai các tính năng chính: Focus Mode (Pomodoro), Task Planning, Dashboard, Notifications

---

## 3. Kiến trúc tổng quan

### 3.1. Multi-Module Design

📁 **app**: Entry point, chứa ViewModels, điều hướng, UI Compose chính
📁 **core**: Thành phần tái sử dụng (UI Kit, Theme, extensions, resources, utils)
📁 **domain**: Business logic thuần, Use Cases, interface repository
📁 **data**: Implementation repository, Room Database, Data Source (Remote, Local)
📁 **features**:

* **focus**: Module tính năng Focus Mode
* **task**: Module quản lý Task & Planning
* **dashboard**: Module Dashboard tổng quan

---

### 3.2. Clean Architecture Pattern

| Tầng                            | Vai trò                                  |
| ------------------------------- | ---------------------------------------- |
| **Presentation (App/Features)** | Compose UI + ViewModels + Navigation     |
| **Domain**                      | Use Cases, Business Logic thuần          |
| **Data**                        | Repository implementation, Database, API |
| **Core**                        | Utilities, UI Components dùng chung      |

---

## 4. Tech Stack

| Thành phần                      | Công nghệ                                       |
| ------------------------------- | ----------------------------------------------- |
| **Language**                    | Kotlin                                          |
| **UI Framework**                | Jetpack Compose                                 |
| **Dependency Injection**        | Hilt                                            |
| **Database**                    | Room                                            |
| **Networking**                  | Retrofit (future extensibility)                 |
| **Reactive / State Management** | Kotlin Coroutines, Flow, StateFlow              |
| **Build System**                | Gradle Kotlin DSL, Version Catalog              |
| **Testing**                     | JUnit, Mockito, Turbine, Espresso, Compose Test |

---

## 5. Thiết kế cơ sở dữ liệu

### 5.1. Entity chính

* **Task**

    * id: Int (Primary Key)
    * title: String
    * description: String
    * priority: Int
    * status: Enum (Pending, In Progress, Done)
    * createdAt: Long
    * updatedAt: Long

* **PomodoroSession**

    * id: Int (Primary Key)
    * taskId: Int (FK to Task)
    * duration: Int
    * isCompleted: Boolean
    * createdAt: Long

* **Schedule**

    * id: Int (Primary Key)
    * taskId: Int (FK to Task)
    * startTime: Long
    * endTime: Long

✔️ **Relationship**: 1 Task – N PomodoroSession, N Schedule

---

## 6. Tính năng chính

### 6.1. Focus Mode

* Đếm ngược Pomodoro (25/5/15 mins custom)
* Gửi notification khi kết thúc
* Ghi log PomodoroSession

### 6.2. Task Planning

* CRUD task
* Gán lịch Schedule cho task
* Filter theo priority, status

### 6.3. Dashboard

* Hiển thị số task completed, session completed
* Weekly & monthly summary
* Pie chart / bar chart (Compose Canvas / MPAndroidChart integration)

### 6.4. Notifications

* Reminder khi Pomodoro kết thúc
* Reminder lịch Schedule

---

## 7. Lộ trình phát triển

| Phase           | Mục tiêu                                                      |
| --------------- | ------------------------------------------------------------- |
| **MVP Release** | Focus Mode, Task CRUD, Dashboard basic                        |
| **Phase 2**     | Notifications advanced, custom Pomodoro length                |
| **Phase 3**     | AI Planning Assistant, Recovery Mode (focus fatigue recovery) |

---

## 8. Công nghệ mở rộng tiềm năng

* **WorkManager**: Schedule reminder background tasks
* **Firebase Cloud Messaging**: Notifications realtime
* **DataStore**: Preferences management
* **Ktor Client**: Networking multi-platform (nếu phát triển KMM)
* **Compose Animation / Motion Layout**: UI/UX mượt mà

---

## 9. Kết luận

🔨🤖🔧 **BalanceMe** ứng dụng chuẩn **Clean Architecture + Multi-Module + Jetpack Compose**, tối ưu codebase, dễ mở rộng và bảo trì, đồng thời mang lại trải nghiệm người dùng mượt mà, hiện đại, và hiệu quả.

---