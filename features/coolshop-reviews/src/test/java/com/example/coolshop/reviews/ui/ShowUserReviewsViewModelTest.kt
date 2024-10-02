package com.example.coolshop.reviews.ui

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.coolshop.reviews.domain.LoadUserReviewsUseCase
import com.example.data.UserReviewModel
import com.example.database.models.UserReviewDBO
import com.example.state.ApiState
import com.example.utils.Logger
import com.example.utils.Mapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class ShowUserReviewsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ShowUserReviewsViewModel
    private lateinit var loadUserReviewsUseCase: LoadUserReviewsUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        loadUserReviewsUseCase = mockk()
        savedStateHandle = SavedStateHandle(mapOf(ShowUserReviewsViewModel.PRODUCT_ID to "1"))

        // Инициализация viewModel
         logger = mockk<Logger>()
        every { logger.e(any(), any(), any()) } just Runs
        // Инициализируем ViewModel с замокированным логгером
        viewModel = ShowUserReviewsViewModel(loadUserReviewsUseCase, savedStateHandle, logger)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadReviews() should emit Success state when reviews are loaded`() = runTest {
        // Мокируем успешный результат из useCase
        val mockReviewDBOs = listOf(mockk<UserReviewDBO>()) // Мокируем UserReviewDBO
        val mockReviewModels = mockReviewDBOs.map { mockk<UserReviewModel>() } // Маппинг UserReviewModel

        // Мокаем маппинг UserReviewDBO -> UserReviewModel
        mockkObject(Mapper)
        every { Mapper.mapReviewDBOToReview(any()) } returns mockReviewModels.first()

        // Мокаем результат UseCase для UserReviewDBO
        coEvery { loadUserReviewsUseCase.execute(1) } returns flowOf(mockReviewDBOs)

        // Запускаем загрузку отзывов
        viewModel.loadReviews(1)

        // Проверяем результат
        val state = viewModel.reviewsStateFlow.first() // Считываем первое значение из StateFlow
        assertTrue(state is ApiState.Success)
        assertEquals(mockReviewModels, (state as ApiState.Success).data)

        // Проверяем, что маппер был вызван для каждого DBO
        verify { Mapper.mapReviewDBOToReview(any()) }
    }

    @Test
    fun `loadReviews() should emit Failure state when useCase throws exception`() = runTest {
        // Мокируем выброс исключения из UseCase
        val exception = Exception("Network error")
        coEvery { loadUserReviewsUseCase.execute(1) } throws exception

        // Запускаем загрузку отзывов
        viewModel.loadReviews(1)

        // Проверяем результат
        val state = viewModel.reviewsStateFlow.first() // Считываем первое значение из StateFlow
        assertTrue(state is ApiState.Failure)
        assertEquals(exception, (state as ApiState.Failure).message)
    }

    @Test
    fun `init should load reviews using productId from SavedStateHandle`() = runTest {
        // Мокируем успешный результат из useCase
        val mockReviewDBOs = listOf(mockk<UserReviewDBO>())
        coEvery { loadUserReviewsUseCase.execute(1) } returns flowOf(mockReviewDBOs)

        // Переинициализируем viewModel, чтобы сработал init
        viewModel = ShowUserReviewsViewModel(loadUserReviewsUseCase, savedStateHandle, logger = logger)

        // Проверяем, что был вызван useCase с правильным productId
        coVerify { loadUserReviewsUseCase.execute(1) }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain() // Возвращаем основной диспетчер
    }
}
