package com.study.boardproject.board.entity

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Notification (link: String, message: String, recevier: User, notificationType: NotificationType){
    //NotificationType - 알림 타입 (댓글 알림, 게시글 수정 remind알림)
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column
    val link: String = link

    @Column
    val message: String = message

    @Column
    var checked: Boolean = false //확인했는가?

    @ManyToOne(fetch = FetchType.LAZY)
    val recevier: User = recevier

    @Enumerated(EnumType.STRING)
    val notificationType : NotificationType = notificationType

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()

    fun updateChecked() {
        this.checked = true
    }

    public enum class NotificationType {
        EMPTY,
        EDIT_PERIOD_IMMINENT
    }

}
