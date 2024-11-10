package com.study.boardproject.viewCount.Service

import com.study.boardproject.post.dto.PostResponseDto
import com.study.boardproject.post.dto.toDto
import com.study.boardproject.post.repository.PostRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class ViewCountRankingService(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val postRepository: PostRepository,
    private val viewCountService: ViewCountService

) {

    fun getWeeklyTopPostsFromRedis(): List<PostResponseDto> {
        val redisKey = "weekly:topPosts"

        // Redis에서 월간 TOP 5 게시글 ID를 조회
        val topPostIds = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4) // 0부터 4까지 (TOP 5)

        val result =  topPostIds?.mapNotNull { postId ->
            postRepository.findById(postId.toString().toLong()).orElse(null)
        } ?: emptyList()

        return result.map { post ->
            val viewCount = post.id?.let { viewCountService.getPostViewCount(it) } ?: 0L
            post.toDto(viewCount)
        }
    }

    fun getMonthlyTopPostsFromRedis(): List<PostResponseDto> {
        val redisKey = "monthly:topPosts"

        // Redis에서 월간 TOP 5 게시글 ID를 조회
        val topPostIds = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4) // 0부터 4까지 (TOP 5)

        val result =  topPostIds?.mapNotNull { postId ->
            postRepository.findById(postId.toString().toLong()).orElse(null)
        } ?: emptyList()

        return result.map { post ->
            val viewCount = post.id?.let { viewCountService.getPostViewCount(it) } ?: 0L
            post.toDto(viewCount)
        }
    }

}