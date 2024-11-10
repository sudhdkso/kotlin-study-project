package com.study.boardproject.viewCount.Service

import com.study.boardproject.post.service.PostService
import com.study.boardproject.viewCount.entity.ViewCountHistory
import com.study.boardproject.viewCount.repository.ViewCountHistoryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ViewCountScheduler(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val postService: PostService,
    private val viewCountHistoryRepository: ViewCountHistoryRepository
) {

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    fun saveDailyViewCountsToRdb() {
        val today = LocalDate.now()
        val redisKeys = redisTemplate.keys("post:*:viewCount")

        for (key in redisKeys) {
            // Redis에서 조회수를 가져와서 RDB에 저장
            val postId = key.split(":")[1].toLong()  // 예시: "post:1:viewCount"에서 1을 추출
            val viewCountIncrease = redisTemplate.opsForValue().get(key) as Int

            // Rdb에 데이터 저장하는 로직
            postService.saveViewCount(postId, viewCountIncrease.toLong())

            val viewCountHistory = ViewCountHistory(
                postId = postId,
                date = today,
                viewCountIncrease = viewCountIncrease.toLong()
            )

            viewCountHistoryRepository.save(viewCountHistory)

            // Redis에서 삭제
            redisTemplate.delete(key)
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")  // 매주 월요일 자정에 실행
    fun storeWeeklyTopPostsToRedis() {
        val startDate = LocalDate.now().minusWeeks(1)
        val endDate = LocalDate.now()
        val pageable: Pageable = PageRequest.of(0, 5)

        val topPosts = viewCountHistoryRepository.findTopPostsByViewCountIncrease(startDate, endDate, pageable).content

        val redisKey = "weekly:topPosts" // 주간 TOP 게시글을 위한 Redis Key

        topPosts.forEachIndexed { index, post ->
            redisTemplate.opsForZSet().add(redisKey, post.id.toString(), index.toDouble())
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")  // 매월 1일 자정에 실행
    fun storeMonthlyTopPostsToRedis() {
        val startDate = LocalDate.now().minusMonths(1)
        val endDate = LocalDate.now()
        val pageable: Pageable = PageRequest.of(0, 5)

        val topPosts = viewCountHistoryRepository.findTopPostsByViewCountIncrease(startDate, endDate, pageable).content

        val redisKey = "monthly:topPosts" // 월간 TOP 게시글을 위한 Redis Key


        topPosts.forEachIndexed { index, post ->
            redisTemplate.opsForZSet().add(redisKey, post.id.toString(), index.toDouble())
        }
    }

}