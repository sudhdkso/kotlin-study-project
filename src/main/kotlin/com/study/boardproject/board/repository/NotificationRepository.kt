package com.study.boardproject.board.repository

import com.study.boardproject.board.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Long> {
}