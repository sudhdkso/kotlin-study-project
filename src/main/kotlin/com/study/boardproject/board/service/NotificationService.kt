package com.study.boardproject.board.service

import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.Notification
import com.study.boardproject.board.repository.EmitterRepository
import com.study.boardproject.board.repository.NotificationRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val emitterRepository: EmitterRepository
) {

    fun sendEditDeadlineNotification(board: Board) {
        val message = " 게시글 [${board.title}] 수정 기간이 하루 남았습니다."
        val receiver = board.writer ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
        val notification = Notification("link", message, receiver, Notification.NotificationType.EDIT_PERIOD_IMMINENT)

        notificationRepository.save(notification).id
    }
}