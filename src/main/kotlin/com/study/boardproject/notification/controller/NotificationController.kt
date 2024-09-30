package com.study.boardproject.notification.controller

import com.study.boardproject.notification.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/notifications")
class NotificationController(private val notificationService: NotificationService) {

    @GetMapping("/subscribe/{id}")
    fun subscribe(@PathVariable("id") userId:Long,
                  @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") lastEventId: String):ResponseEntity<SseEmitter> {
        return ResponseEntity.ok().body(notificationService.subscribe(userId))
    }
}