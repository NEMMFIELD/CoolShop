package com.example.coolshop.reviews.domain

import com.example.database.models.UserReviewDBO
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import kotlin.test.Test


class AddUserReviewUseCaseTest {
    private lateinit var repository: UserReviewsRepository
    private lateinit var useCase: AddUserReviewUseCase

    @Before
    fun setUp() {
        // Мокаем репозиторий
        repository = mockk(relaxed = true)
        useCase = AddUserReviewUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun `execute should call addReview on repository with correct review`() = runBlocking {
        // Given: объект UserReviewDBO для добавления
        val review = UserReviewDBO(reviewId = 1, productId = 100, userName = "Tom", review = "Excellent!")

        // When: вызываем метод execute
        useCase.execute(review)

        // Then: проверяем, что метод addReview репозитория был вызван с правильным аргументом
        coVerify { repository.addReview(review) }
    }
}
