package com.study.boardproject.board.service

import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.Notification
import com.study.boardproject.board.repository.EmitterRepository
import com.study.boardproject.board.repository.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val emitterRepository: EmitterRepository
) {
    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    private val DEFAULT_TIMEOUT = 3600000L
    private val NOTIFICATION_NAME = "notify"

    fun sendEditDeadlineNotification(board: Board) {
        val message = " 게시글 [${board.title}] 수정 기간이 하루 남았습니다."
        val receiver = board.writer ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
        val notification = Notification("link", message, receiver, Notification.NotificationType.EDIT_PERIOD_IMMINENT)

        val notificationId = notificationRepository.save(notification).id ?: throw IllegalArgumentException("알림을 찾을 수 없습니다.")
        send(receiver.id!!, notificationId, "수정기간이 하루 남았습니다.")
    }

    fun subscribe(userId: Long): SseEmitter {
        val sseEmitter: SseEmitter = SseEmitter(DEFAULT_TIMEOUT)

        emitterRepository.save(userId, sseEmitter)

        sseEmitter.onCompletion { emitterRepository.delete(userId) }
        sseEmitter.onTimeout { emitterRepository.delete(userId) }

        try {
            sseEmitter.send(
                SseEmitter.event()
                    .id(userId.toString())
                    .name(NOTIFICATION_NAME)
                    .data("Connection completed")
            )
        } catch (exception: IOException) {
            throw IllegalArgumentException("연결 오류")
        }

        return sseEmitter
    }

    fun send(userId: Long, notificationId: Long, data: String) {
        emitterRepository.get(userId).ifPresentOrElse(
            { sseEmitter ->
                try {
                    sseEmitter.send(
                        SseEmitter.event()
                            .id(notificationId.toString())
                            .name(NOTIFICATION_NAME)
                            .data(data)
                    )
                } catch (excetpion: IOException) {
                    emitterRepository.delete(userId)
                    throw IllegalArgumentException("연결 오류")
                }
            },
            { log.info("not found emitter") }
        )
    }
}