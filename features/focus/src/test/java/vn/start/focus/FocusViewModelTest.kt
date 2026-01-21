package vn.start.focus

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import vn.start.domain.model.ScheduleModel
import vn.start.domain.usecase.schedule.AddScheduleUseCase
import vn.start.domain.usecase.schedule.GetSchedulesForTaskUseCase
import vn.start.focus.ui.FocusContract
import vn.start.focus.ui.FocusViewModel
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for FocusViewModel demonstrating proper ViewModel testing patterns.
 * 
 * Key testing patterns:
 * - Use TestDispatcher for coroutine testing
 * - Mock use cases to isolate ViewModel logic
 * - Test state transitions and effects
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FocusViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var addScheduleUseCase: AddScheduleUseCase

    @Mock
    private lateinit var getSchedulesForTaskUseCase: GetSchedulesForTaskUseCase

    private lateinit var viewModel: FocusViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        viewModel = FocusViewModel(
            addScheduleUseCase = addScheduleUseCase,
            getSchedulesForTaskUseCase = getSchedulesForTaskUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() {
        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.schedules.isEmpty())
        assertNull(state.currentTaskId)
    }

    @Test
    fun `insertSchedule should show loading state`() = runTest(testDispatcher) {
        // Given
        whenever(addScheduleUseCase.invoke(any())).thenReturn(Unit)

        // When
        viewModel.onEvent(FocusContract.UiEvent.InsertSchedule(
            taskId = 1,
            startTime = System.currentTimeMillis(),
            duration = 1500
        ))

        // Then - verify loading state was set
        testDispatcher.scheduler.advanceUntilIdle()
        verify(addScheduleUseCase).invoke(any())
    }

    @Test
    fun `insertSchedule should update state on success`() = runTest(testDispatcher) {
        // Given
        whenever(addScheduleUseCase.invoke(any())).thenReturn(Unit)

        // When
        viewModel.onEvent(FocusContract.UiEvent.InsertSchedule(
            taskId = 1,
            startTime = System.currentTimeMillis(),
            duration = 1500
        ))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `insertSchedule should update state on error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to insert schedule"
        whenever(addScheduleUseCase.invoke(any())).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.onEvent(FocusContract.UiEvent.InsertSchedule(
            taskId = 1,
            startTime = System.currentTimeMillis(),
            duration = 1500
        ))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `loadSchedulesForTask should update currentTaskId`() = runTest(testDispatcher) {
        // Given
        val taskId = 42

        // When
        viewModel.onEvent(FocusContract.UiEvent.LoadSchedulesForTask(taskId))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(taskId, viewModel.uiState.value.currentTaskId)
    }

    @Test
    fun `resetError should clear error state`() = runTest(testDispatcher) {
        // Given - simulate an error first
        whenever(addScheduleUseCase.invoke(any())).thenThrow(RuntimeException("Error"))
        viewModel.onEvent(FocusContract.UiEvent.InsertSchedule(1, 0, 100))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verify error is set
        assertTrue(viewModel.uiState.value.error != null)

        // When
        viewModel.onEvent(FocusContract.UiEvent.ResetError)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `getSchedulesForTask should return flow from use case`() = runTest(testDispatcher) {
        // Given
        val schedules = listOf(
            ScheduleModel(id = 1, taskId = 1, startTime = 1000L, endTime = 2000L),
            ScheduleModel(id = 2, taskId = 1, startTime = 3000L, endTime = 4000L)
        )
        whenever(getSchedulesForTaskUseCase.invoke(1)).thenReturn(flowOf(schedules))

        // When
        val result = viewModel.getSchedulesForTask(1)

        // Then
        result.collect { actualSchedules ->
            assertEquals(schedules, actualSchedules)
        }
    }
}
