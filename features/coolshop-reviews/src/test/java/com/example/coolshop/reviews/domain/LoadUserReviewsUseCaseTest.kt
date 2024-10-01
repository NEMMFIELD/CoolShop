package com.example.coolshop.reviews.domain

import android.util.Log
import com.example.database.models.UserReviewDBO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class LoadUserReviewsUseCaseTest {
    private lateinit var repository: UserReviewsRepository
    private lateinit var useCase: LoadUserReviewsUseCase

    @Before
    fun setUp() {
        // Мокаем репозиторий
        repository = mockk()
        useCase = LoadUserReviewsUseCase(repository)

        // Мокаем статический метод Log.d
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0 // Возвращаем 0 для методов логирования
    }

    @After
    fun tearDown() {
        // Снимаем мокирование всех статических методов
        unmockkAll()
    }

    @Test
    fun `execute should return flow with reviews`() = runTest {
        // Given: productId и список отзывов, который должен быть возвращен репозиторием
        val productId = 100
        val expectedReviews = listOf(
            UserReviewDBO(reviewId = 1, productId = 100, userName = "Tom", review = "Excellent!"),
            UserReviewDBO(reviewId = 2, productId = 10, userName = "Tomy", review = "Bravo!")
        )

        // Мокаем возврат списка отзывов из репозитория
        coEvery { repository.loadReviews(productId) } returns expectedReviews

        // When: вызываем метод execute и собираем результат flow
        val result = useCase.execute(productId).first()

        // Then: проверяем, что результат совпадает с ожидаемым списком отзывов
        assertEquals(expectedReviews, result)

        // Проверяем, что метод loadReviews был вызван с правильным идентификатором
        coVerify { repository.loadReviews(productId) }
    }
}
