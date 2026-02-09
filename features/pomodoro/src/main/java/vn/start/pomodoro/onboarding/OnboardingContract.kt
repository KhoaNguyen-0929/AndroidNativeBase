package vn.start.pomodoro.onboarding

/**
 * MVI Contract for Onboarding.
 */
object OnboardingContract {

    data class UiState(
        val currentPage: Int = 0,
        val totalPages: Int = 3,
        val selectedDurationMinutes: Int = 25,
        val isComplete: Boolean = false
    ) {
        val isLastPage: Boolean get() = currentPage == totalPages - 1
        val showGetStarted: Boolean get() = isLastPage
        val showNext: Boolean get() = !isLastPage
        
        companion object {
            val Initial = UiState()
        }
    }

    sealed class UiEvent {
        data object NextPage : UiEvent()
        data object PreviousPage : UiEvent()
        data class GoToPage(val page: Int) : UiEvent()
        data class SelectDuration(val minutes: Int) : UiEvent()
        data object GetStarted : UiEvent()
        data object Skip : UiEvent()
    }

    sealed class UiEffect {
        data object NavigateToHome : UiEffect()
    }

    // Onboarding page content
    data class OnboardingPage(
        val title: String,
        val description: String,
        val emoji: String
    )

    val pages = listOf(
        OnboardingPage(
            title = "Focus in short bursts",
            description = "The Pomodoro Technique helps you work in focused 25-minute sessions, making big tasks feel manageable.",
            emoji = "🍅"
        ),
        OnboardingPage(
            title = "Work → Break → Repeat",
            description = "After each focus session, take a short break. After 4 sessions, enjoy a longer break. Your brain will thank you.",
            emoji = "🔄"
        ),
        OnboardingPage(
            title = "Let's start your first session",
            description = "Choose your focus duration and begin. You can always adjust this later in settings.",
            emoji = "🚀"
        )
    )

    val durationOptions = listOf(15, 25, 30, 45, 50)
}
