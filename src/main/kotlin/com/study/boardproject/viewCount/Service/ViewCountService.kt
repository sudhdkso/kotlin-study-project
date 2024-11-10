package com.study.boardproject.viewCount.Service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class ViewCountService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    // 게시글 조회수 키 가져오기
    private fun getPostViewCountKey(postId: Long): String {
        return "post:$postId:viewCount"
    }

    // 조회수 증가
    fun incrementPostViewCount(postId: Long) {
        val key = getPostViewCountKey(postId)
        redisTemplate.opsForValue().increment(key, 1)
    }

    // 게시글 조회수 가져오기
    fun getPostViewCount(postId: Long): Long {
        val key = getPostViewCountKey(postId)
        return redisTemplate.opsForValue().get(key)?.toString()?.toLong() ?: 0
    }
}