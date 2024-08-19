package com.study.boardproject.board.service

import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@EnableScheduling
@Component
class PostScheduler(private val postService: PostService) {

    @Scheduled(cron = "0 0 0 * * ?")
    fun notifyEditDeadline() {
        postService.sendEditDeadlineNotifications()
    }
}