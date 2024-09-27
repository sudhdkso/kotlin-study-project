package com.study.boardproject.notification.repository

import com.study.boardproject.notification.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Long> {
}