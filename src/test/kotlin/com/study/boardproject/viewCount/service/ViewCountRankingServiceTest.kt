package com.study.boardproject.viewCount.service

import com.study.boardproject.post.entity.Post
import com.study.boardproject.post.repository.PostRepository
import com.study.boardproject.user.entity.User
import com.study.boardproject.viewCount.Service.ViewCountRankingService
import com.study.boardproject.viewCount.Service.ViewCountService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.redis.core.RedisTemplate
import java.util.*

class ViewCountRankingServiceTest : BehaviorSpec({
    val redisTemplate: RedisTemplate<String, Any> = mockk(relaxed = true)
    val postRepository: PostRepository = mockk()
    val viewCountService: ViewCountService = mockk()
    val viewCountRankingService = ViewCountRankingService(redisTemplate, postRepository, viewCountService)

    afterEach {
        clearMocks(redisTemplate, postRepository, viewCountService)
    }

    Given("주간 조회수 TOP5를 ") {

        val redisKey = "weekly:topPosts"
        val postIdList = setOf(1L, 2L, 3L, 4L, 5L)
        val topPosts = postIdList.map {
            mockk<Post>(relaxed = true).apply {
                val user = mockk<User>().apply {
                    every { email } returns "user$it@example.com"
                    every { name } returns "User $it"
                }
                every { id } returns it
                every { title } returns "Post $it"
                every { content } returns "Content for post $it"
                every { writer } returns user
                every { viewCount } returns 100L
            }
        }

        val viewCountMap = mapOf(
            1L to 100L,
            2L to 200L,
            3L to 300L,
            4L to 400L,
            5L to 500L
        )

        every { redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4) } returns postIdList

        postIdList.forEach { postId ->
            val post = topPosts.first { it.id == postId }
            every { postRepository.findById(postId) } returns Optional.of(post)
        }

        every { viewCountService.getPostViewCount(any()) } answers {
            val postId = it.invocation.args[0] as Long
            viewCountMap[postId] ?: 0L
        }

        When("getWeeklyTopPostsFromRedis함수를 호출해서") {
            val result = viewCountRankingService.getWeeklyTopPostsFromRedis()

            Then("redis에서 가져올 수 있다.") {
                result shouldHaveSize 5
                verify(exactly = 1) { redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4) }
                verify(exactly = 5) { postRepository.findById(any()) }
                verify(exactly = 5) { viewCountService.getPostViewCount(any()) }
            }
        }
    }

    Given("월간 조회수 TOP5를") {

        val redisKey = "monthly:topPosts"
        val postIdList = setOf(1L, 2L, 3L, 4L, 5L)
        val topPosts = postIdList.map {
            mockk<Post>(relaxed = true).apply {
                val user = mockk<User>().apply {
                    every { email } returns "user$it@example.com"
                    every { name } returns "User $it"
                }
                every { id } returns it
                every { title } returns "Post $it"
                every { content } returns "Content for post $it"
                every { writer } returns user
                every { viewCount } returns 100L
            }
        }
        val viewCountMap = mapOf(
            1L to 100L,
            2L to 200L,
            3L to 300L,
            4L to 400L,
            5L to 500L
        )

        every { redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4) } returns postIdList

        postIdList.forEach { postId ->
            val post = topPosts.first { it.id == postId }
            every { postRepository.findById(postId) } returns Optional.of(post)
        }

        every { viewCountService.getPostViewCount(any()) } answers {
            val postId = it.invocation.args[0] as Long
            viewCountMap[postId] ?: 0L
        }

        When("getMonthlyTopPostsFromRedis함수를 호출하여") {
            val result = viewCountRankingService.getMonthlyTopPostsFromRedis()

            Then("redis에서 가져올 수 있다.") {
                result shouldHaveSize 5
                verify(exactly = 1) { redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4) }
                verify(exactly = 5) { postRepository.findById(any()) }
                verify(exactly = 5) { viewCountService.getPostViewCount(any()) }
            }
        }
    }
})