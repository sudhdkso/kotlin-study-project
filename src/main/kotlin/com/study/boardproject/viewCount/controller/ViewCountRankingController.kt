package com.study.boardproject.viewCount.controller

import com.study.boardproject.post.dto.PostResponseDto
import com.study.boardproject.viewCount.Service.ViewCountRankingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/posts/rank")
@RestController
class ViewCountRankingController(
    private val viewCountRankingService: ViewCountRankingService
) {

    @GetMapping("/weekly")
    fun getWeeklyTopViewCount() : ResponseEntity<List<PostResponseDto>>{
        val response = viewCountRankingService.getWeeklyTopPostsFromRedis()
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/monthly")
    fun getMonthlyTopViewCount() : ResponseEntity<List<PostResponseDto>>{
        val response = viewCountRankingService.getMonthlyTopPostsFromRedis()
        return ResponseEntity.ok().body(response)
    }
}