package com.example.coolshop.reviews.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.coolshop.reviews.domain.AddUserReviewUseCase
import com.example.database.models.UserReviewDBO
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.Test
import kotlin.test.assertEquals


class AddingUserReviewViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Чтобы LiveData обновлялась немедленно

    @get:Rule
    val coroutineTestRule = MainCoroutineRule() // Правило для тестирования корутин

    private lateinit var viewModel: AddingUserReviewViewModel
    private val useCase: AddUserReviewUseCase = mockk(relaxed = true) // Мокаем useCase
    private val savedStateHandle: SavedStateHandle = mockk() // Мокаем SavedStateHandle

    @Before
    fun setUp() {
        every { savedStateHandle.get<String>("productId") } returns "123" // Мокаем возврат значения productId
        viewModel = AddingUserReviewViewModel(useCase, savedStateHandle)
    }

    @Test
    fun `addReview should increment id and call useCase`() = runTest {
        // Arrange
        val initialId = viewModel.id
        val reviewDBO = UserReviewDBO(reviewId = 1, userName = "Tom",review = "Super", productId = 2)

        // Act
        viewModel.addReview(reviewDBO)

        // Assert
        assertEquals(initialId + 1, viewModel.id) // Проверяем, что id инкрементирован
        coVerify { useCase.execute(reviewDBO) } // Проверяем, что useCase был вызван с нужным параметром
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}
