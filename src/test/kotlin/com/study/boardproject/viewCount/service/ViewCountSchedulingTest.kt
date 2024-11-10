package com.study.boardproject.viewCount.service

import com.study.boardproject.core.configuration.RedisConfig
import com.study.boardproject.post.entity.Post
import com.study.boardproject.post.service.PostService
import com.study.boardproject.viewCount.Service.ViewCountScheduler
import com.study.boardproject.viewCount.entity.ViewCountHistory
import com.study.boardproject.viewCount.repository.ViewCountHistoryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDate

@SpringBootTest
@Import(RedisConfig::class)
class ViewCountSchedulingTest: BehaviorSpec({
    val redisTemplate :RedisTemplate<String, Any> =  mockk(relaxed = true)
    val postService : PostService = mockk()
    val viewCountHistoryRepository : ViewCountHistoryRepository = mockk()

    val viewCountScheduler = ViewCountScheduler(redisTemplate, postService, viewCountHistoryRepository)

    afterEach {
        clearMocks(redisTemplate, postService, viewCountHistoryRepository)
    }

    given("redis에 저장된 조회수를") {
        val redisKey = "post:1:viewCount"
        val viewCount = 100
        val postId = 1L
        val today = LocalDate.now()

        // Redis 키 목록 반환
        every { redisTemplate.keys("post:*:viewCount") } returns setOf(redisKey)

        every { redisTemplate.opsForValue().get(redisKey) } returns viewCount
        every { postService.saveViewCount(postId, viewCount.toLong()) } returns Unit
        every { viewCountHistoryRepository.save(any<ViewCountHistory>()) } returns ViewCountHistory(postId = postId, date = today, viewCountIncrease = viewCount.toLong())

        coEvery { redisTemplate.delete(redisKey) } returns true

        `when`("saveDailyViewCountsToRdb함수를 호출하여") {
            viewCountScheduler.saveDailyViewCountsToRdb()

            then("DB에 저장하고 해당 값을 redis에서 삭제할 수 있다") {
                verify(exactly = 1) { postService.saveViewCount(postId, viewCount.toLong()) }
                verify(exactly = 1) { redisTemplate.delete(redisKey) }
                verify(exactly = 1) { viewCountHistoryRepository.save(any()) }
            }
        }
    }

    given("주간 조회수 TOP5를 ") {
        val pageable: Pageable = PageRequest.of(0, 5)
        val redisKey = "weekly:topPosts"
        val topPosts = listOf(
            mockk<Post>().apply { every { id } returns 1L; every { title } returns "Post 1" },
            mockk<Post>().apply { every { id } returns 2L; every { title } returns "Post 2" }
        )

        every { viewCountHistoryRepository.findTopPostsByViewCountIncrease(any(), any(), pageable) } returns PageImpl(topPosts)
        coEvery { redisTemplate.opsForZSet().add(redisKey, any<String>(), any<Double>()) } returns true

        `when`("storeWeeklyTopPostsToRedis함수를 호출하여") {
            viewCountScheduler.storeWeeklyTopPostsToRedis()

            then("주간 조회수를 집계하고 redis에 저장할 수 있다.") {
                verify(exactly = 2) { redisTemplate.opsForZSet().add(redisKey, any(), any()) }
            }
        }
    }

    given("월간 조회수 TOP5를") {
        val pageable: Pageable = PageRequest.of(0, 5)
        val redisKey = "monthly:topPosts"
        val topPosts = listOf(
            mockk<Post>().apply { every { id } returns 1L; every { title } returns "Post 1" },
            mockk<Post>().apply { every { id } returns 2L; every { title } returns "Post 2" }
        )


        every { viewCountHistoryRepository.findTopPostsByViewCountIncrease(any(), any(), pageable) } returns PageImpl(topPosts)
        coEvery { redisTemplate.opsForZSet().add(redisKey, any<String>(), any<Double>()) } returns true

        `when`("storeMonthlyTopPostsToRedis함수를 호출하여") {
            viewCountScheduler.storeMonthlyTopPostsToRedis()

            then("월간 랭킹을 집계하고 저장할 수 있다.") {
                verify(exactly = 2) { redisTemplate.opsForZSet().add(redisKey, any(), any()) }
            }
        }
    }
})