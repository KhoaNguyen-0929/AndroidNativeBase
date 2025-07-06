# 🍅 Pomodoro Timer App - UI/UX Design

## 📋 Tổng quan

Pomodoro Timer App được thiết kế theo kiến trúc **Single-Activity + MVVM + Repository + Hilt DI** với UI sử dụng **Jetpack Compose** và **Unidirectional Data Flow**.

## 🎨 Thiết kế UI/UX

### 1. Layout Tổng thể

```
┌─────────────────────────────────┐
│        Pomodoro Timer          │ ← Top App Bar
├─────────────────────────────────┤
│                                 │
│         ⭕ 25:00 ⭕            │ ← Timer Display (Circle)
│                                 │
│      [Focus Session]            │ ← Session Type Card
│                                 │
│    [▶️] [🔄] [⏭️]            │ ← Timer Controls
│                                 │
│                                 │
│         🍅 x 3                 │ ← Pomodoro Count
└─────────────────────────────────┘
```

### 2. Phong cách thiết kế

- **Minimalist + Friendly**: Giao diện tối giản nhưng thân thiện
- **Màu sắc chủ đạo**: 
  - Tomato Red (#E74C3C) - màu chính
  - Coral Orange (#FF6B6B) - màu phụ
  - Mint Green (#4ECDC4) - cho break sessions
- **Typography**: Google Sans/Roboto, font tròn rõ ràng
- **Animation**: Nhẹ nhàng khi chuyển đổi trạng thái

### 3. Dark Mode / Light Mode Support

- **Light Mode**: Nền sáng với màu cam nổi bật
- **Dark Mode**: Nền tối với màu cam vẫn giữ độ tương phản tốt

## 🧩 Composable Functions

### Core Components

| Function | Mục đích | Props |
|----------|----------|-------|
| `PomodoroScreen()` | Root composable của màn hình timer | `viewModel`, `modifier` |
| `TimerDisplay()` | Hiển thị thời gian đếm ngược | `timeLeft`, `progress`, `isRunning` |
| `TimerControls()` | 3 nút điều khiển timer | `isRunning`, `onStartPause`, `onReset`, `onSkip` |
| `PomodoroCount()` | Hiển thị số vòng đã hoàn thành | `count` |
| `SessionTypeDisplay()` | Hiển thị loại session hiện tại | `sessionType` |
| `TimerProgressRing()` | Vòng tròn progress (tùy chọn) | `progress`, `strokeWidth`, `colors` |

### Layout Structure

```kotlin
PomodoroScreen
├── TopAppBar
├── TimerDisplay (Circle with gradient)
├── SessionTypeDisplay (Card)
├── TimerControls (Row of buttons)
└── PomodoroCount (Footer)
```

## 📊 ViewModel State Design

### PomodoroUiState

```kotlin
data class PomodoroUiState(
    val timeLeft: String = "25:00",        // Thời gian còn lại
    val isRunning: Boolean = false,        // Trạng thái đang chạy
    val pomodoroCount: Int = 0,           // Số vòng đã hoàn thành
    val currentSession: SessionType = SessionType.Focus,  // Loại session
    val totalSeconds: Int = 25 * 60,      // Tổng thời gian (giây)
    val progress: Float = 1.0f            // Tiến độ (0.0 - 1.0)
)
```

### Session Types

```kotlin
enum class SessionType {
    Focus,      // 25 phút
    Break,      // 5 phút
    LongBreak   // 15 phút
}
```

### ViewModel Actions

| Action | Mô tả |
|--------|-------|
| `startTimer()` | Bắt đầu đếm ngược |
| `pauseTimer()` | Tạm dừng timer |
| `resetTimer()` | Reset về trạng thái ban đầu |
| `skipTimer()` | Chuyển sang session tiếp theo |
| `updateTimeLeft()` | Cập nhật thời gian và progress |

## 🎯 Features

### ✅ Đã implement

1. **Timer Display**: Vòng tròn lớn hiển thị thời gian với gradient
2. **Session Management**: Chuyển đổi giữa Focus/Break/LongBreak
3. **Controls**: Start/Pause, Reset, Skip buttons
4. **Progress Tracking**: Đếm số Pomodoro đã hoàn thành
5. **Theme Support**: Dark/Light mode với Pomodoro colors
6. **Responsive Design**: Tương thích với các kích thước màn hình

### 🔄 Cần implement tiếp

1. **Actual Timer Logic**: Countdown thực tế với coroutines
2. **Sound Notifications**: Âm thanh khi kết thúc session
3. **Settings Screen**: Cấu hình thời gian các session
4. **Statistics**: Thống kê productivity
5. **Animations**: Smooth transitions và micro-interactions

## 🎨 Color Palette

### Primary Colors
- **Tomato Red**: #E74C3C (Focus sessions)
- **Coral Orange**: #FF6B6B (Active state)
- **Soft Orange**: #FF8A65 (Inactive state)

### Secondary Colors
- **Mint Green**: #4ECDC4 (Break sessions)
- **Soft Blue**: #74B9FF (Long break sessions)
- **Light Coral**: #FFB3BA (Accent)

### Neutral Colors
- **Timer Background**: #F8F9FA (Light) / #1A1A1A (Dark)
- **Timer Surface**: #FFFFFF (Light) / #2D2D2D (Dark)
- **Timer Text**: #2C3E50 (Light) / #ECF0F1 (Dark)

## 📱 Responsive Design

### Breakpoints
- **Small**: 320dp - 480dp
- **Medium**: 480dp - 768dp  
- **Large**: 768dp+

### Adaptive Layout
- Timer circle size adjusts based on screen width
- Button spacing scales proportionally
- Text sizes use Material Design typography scale

## 🚀 Performance Considerations

1. **State Management**: Sử dụng StateFlow cho reactive updates
2. **Composable Optimization**: Tách biệt các composable để tránh re-composition không cần thiết
3. **Memory Management**: Proper cleanup trong ViewModel
4. **Background Processing**: Timer logic chạy trong background coroutines

## 🧪 Testing Strategy

### UI Tests
- Test timer display updates
- Test button interactions
- Test theme switching
- Test responsive behavior

### Unit Tests
- ViewModel state management
- Timer logic calculations
- Session type transitions

## 📈 Future Enhancements

1. **Widgets**: Home screen widget với quick controls
2. **Notifications**: Rich notifications với actions
3. **Data Persistence**: Lưu trữ statistics và settings
4. **Accessibility**: Voice commands và screen reader support
5. **Customization**: User-defined themes và layouts

---

*Thiết kế này tuân theo Material Design 3 guidelines và best practices cho productivity apps.* 