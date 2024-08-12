package com.study.boardproject.board.entity

import com.study.boardproject.board.dto.BoardRequestDto
import com.study.boardproject.util.constants.BoardConstants.EDITABLE_PERIOD_DAYS
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "board")
class Board(title: String, content: String, writer: User) : BaseEntity() {
    @Id
    @GeneratedValue
    val id: Long? = null

    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val writer: User? = writer

    @Column(nullable = false, length = 200)
    var title: String? = title

    @Column(nullable = false, length = 1000)
    var content: String? = content

    @Column
    var viewCount : Long = 0

    @OneToMany(mappedBy = "board")
    val commentList : MutableList<Comment> = mutableListOf()

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    fun update(requestDto: BoardRequestDto) {
        this.title = requestDto.title
        this.content = requestDto.content
    }

    fun calculateEditableDaysRemaining(): Long {
        val now = LocalDateTime.now()
        val daysSinceCreation = ChronoUnit.DAYS.between(createdAt, now)
        return if (daysSinceCreation <= EDITABLE_PERIOD_DAYS) {
            EDITABLE_PERIOD_DAYS - daysSinceCreation
        } else {
            -1
        }
    }

    fun canEditBoard() : Boolean {
        val now = LocalDateTime.now()
        val daysSinceCreation = ChronoUnit.DAYS.between(createdAt, now)
        return daysSinceCreation <= EDITABLE_PERIOD_DAYS
    }

    fun viewCountUp(){
        this.viewCount++
    }

    fun delete() {
        deletedAt = LocalDateTime.now()
    }
}