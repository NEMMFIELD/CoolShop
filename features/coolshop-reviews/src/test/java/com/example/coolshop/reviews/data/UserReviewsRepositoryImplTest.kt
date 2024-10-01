package com.example.coolshop.reviews.data

import com.example.database.dao.UserReviewsDao
import com.example.database.models.UserReviewDBO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class UserReviewsRepositoryImplTest {
    private lateinit var dao: UserReviewsDao
    private lateinit var repository: UserReviewsRepositoryImpl

    @Before
    fun setUp() {
        // Создаем мок для DAO
        dao = mockk(relaxed = true)
        repository = UserReviewsRepositoryImpl(dao)
    }
    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `addReview should insert review into dao`() = runBlocking {
        // Given: объект UserReviewDBO для добавления
        val review = UserReviewDBO(reviewId = 1, productId = 100, userName =  "Tom", review = "Great product!")

        // When: вызываем метод addReview в репозитории
        repository.addReview(review)

        // Then: проверяем, что метод insert был вызван с правильным объектом
        coVerify { dao.insert(review) }
    }

    @Test
    fun `loadReviews should return reviews from dao`() = runBlocking {
        // Given: productId для загрузки отзывов и ожидаемый результат
        val productId = 100
        val expectedReviews = listOf(
            UserReviewDBO(reviewId = 1, productId = 100, userName =  "Tom", review = "Great product!"),
            UserReviewDBO(reviewId = 2, productId = 101, userName =  "Nikita", review = "Super!")
        )

        // Мокируем возврат отзывов из DAO
        coEvery { dao.getReviewByProduct(productId) } returns expectedReviews

        // When: вызываем метод loadReviews в репозитории
        val actualReviews = repository.loadReviews(productId)

        // Then: проверяем, что результат соответствует ожидаемому
        assertEquals(expectedReviews, actualReviews)

        // Verify: проверяем, что метод getReviewByProduct был вызван с правильным аргументом
        coVerify { dao.getReviewByProduct(productId) }
    }
}
