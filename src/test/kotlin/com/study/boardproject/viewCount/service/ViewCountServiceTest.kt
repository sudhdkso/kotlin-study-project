package com.study.boardproject.viewCount.service

import com.study.boardproject.core.configuration.RedisConfig
import com.study.boardproject.viewCount.Service.ViewCountService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
@Import(RedisConfig::class)
class ViewCountServiceTest : BehaviorSpec({
    val redisTemplate :RedisTemplate<String, Any> =  mockk(relaxed = true)
    val viewCountService = ViewCountService(redisTemplate)

    afterEach {
        clearMocks(redisTemplate)
    }

    Given("게시글 id로") {
        val postId = 1L
        val key = "post:$postId:viewCount"

        When("viewCount를 증가시킬 때") {

            every { redisTemplate.opsForValue().increment(key, 1) } returns 1L

            viewCountService.incrementPostViewCount(postId)

            then("올바른 RedisKey로 호출하면 잘 동작한다.") {
                verify { redisTemplate.opsForValue().increment(key, 1) }
            }
        }

        When("조회수를 가져올 때") {
            every { redisTemplate.opsForValue().get(key) } returns 5L

            val viewCount = viewCountService.getPostViewCount(postId)

            then("올바른 RedisKey로 호출하면 잘 동작한다.") {
                viewCount shouldBe 5L
            }
        }
    }


})