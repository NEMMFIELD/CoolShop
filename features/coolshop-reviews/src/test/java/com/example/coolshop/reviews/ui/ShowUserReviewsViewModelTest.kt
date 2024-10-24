package com.example.coolshop.reviews.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.coolshop.reviews.data.CoolShopReviewsMapper
import com.example.coolshop.reviews.domain.LoadUserReviewsUseCase
import com.example.data.UserReviewModel
import com.example.database.models.UserReviewDBO
import com.example.state.State
import com.example.utils.Logger
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class ShowUserReviewsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var mockLoadUserReviewsUseCase: LoadUserReviewsUseCase

    @RelaxedMockK
    private lateinit var mockSavedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var mockLogger: Logger
    private lateinit var viewModel: ShowUserReviewsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        //Настраиваем возвращаемое значение для productId
        every { mockSavedStateHandle.get<String>(PRODUCT_ID) } returns "123"

        //Создаем экземпляр ViewModel
        viewModel = ShowUserReviewsViewModel(mockLoadUserReviewsUseCase, mockSavedStateHandle, mockLogger)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Сбрасываем основной диспетчер
        clearAllMocks() // Очищаем моки
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadReviews() should emit Success state when useCase returns reviews`() = runTest {
        // Given: создаем мок UserReviewDBO и настраиваем его поля
        val reviewDBO = mockk<UserReviewDBO> {
            every { reviewId } returns 1
            every { userName } returns "Test User"
            every { review } returns "This is a test review"
            every { productId } returns 123
        }

        // Создаем список UserReviewDBO
        val reviewsDBO = listOf(reviewDBO)

        // Мокаем выполнение useCase для возврата потока с объектами UserReviewDBO
        every { mockLoadUserReviewsUseCase.execute(123) } returns flowOf(reviewsDBO)

        // Когда: вызываем метод loadReviews
        viewModel.loadReviews(123)

        // Используем collect для асинхронного получения состояния
        var emittedState: State<List<UserReviewModel>>? = null
        val job = launch {
            viewModel.reviewsStateFlow.collect { state ->
                emittedState = state // Сохраняем текущее состояние
            }
        }

        // Завершаем корутины
        advanceUntilIdle()
        job.cancel() // Завершаем коллекцию после завершения теста

        // Отладка: вывод текущего состояния
        println("Emitted state: $emittedState")

        // Then: проверяем, что состояние изменилось на Success с преобразованными отзывами
        val expectedReviews = reviewsDBO.map { CoolShopReviewsMapper.mapReviewDBOToReview(it) }
        assertEquals(State.Success(expectedReviews), emittedState)

        verify { mockLoadUserReviewsUseCase.execute(123) } // Проверяем вызов useCase
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadReviews() should emit Failure state when useCase throws exception`() = runTest {
        // Given: создаем исключение
        val exception = Exception("Failed to load reviews")

        // Мокаем выполнение useCase для возврата исключения
        every { mockLoadUserReviewsUseCase.execute(123) } returns flow { throw exception }

        // Когда: вызываем метод loadReviews
        viewModel.loadReviews(123)

        // Используем collect для асинхронного получения состояния
        var emittedState: State<List<UserReviewModel>>? = null
        val job = launch {
            viewModel.reviewsStateFlow.collect { state ->
                emittedState = state // Сохраняем текущее состояние
            }
        }

        // Завершаем корутины
        advanceUntilIdle()
        job.cancel() // Завершаем коллекцию после завершения теста

        // Отладка: вывод текущего состояния
        println("Emitted state: $emittedState")

        // Then: проверяем, что состояние изменилось на Failure с исключением
        assertEquals(State.Failure(exception), emittedState)

        verify { mockLoadUserReviewsUseCase.execute(123) } // Проверяем вызов useCase
    }
}
