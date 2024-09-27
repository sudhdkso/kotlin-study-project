package com.study.boardproject.post.entity

import com.study.boardproject.post.dto.PostRequestDto
import com.study.boardproject.common.base.BaseTime
import com.study.boardproject.board.entity.Board
import com.study.boardproject.comment.entity.Comment
import com.study.boardproject.board.user.entity.User
import com.study.boardproject.common.constants.BoardConstants.EDITABLE_PERIOD_DAYS
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "post")
class Post(title: String, content: String, writer: User?, board: Board) : BaseTime() {
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

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val comments : MutableList<Comment> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    var board: Board? = board

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    fun update(requestDto: PostRequestDto) {
        this.title = requestDto.title
        this.content = requestDto.content
        this.board = board
    }

    fun updateBoard(board: Board){
        this.board ?.removePost(this)
        board.addPost(this)
        this.board = board
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

    fun canEditPost() : Boolean {
        val now = LocalDateTime.now()
        val daysSinceCreation = ChronoUnit.DAYS.between(now, createdAt)
        return daysSinceCreation < EDITABLE_PERIOD_DAYS
    }

    fun viewCountUp(){
        this.viewCount++
    }

    fun delete() {
        deletedAt = LocalDateTime.now()
    }

    fun addComment(comment: Comment) {
        comments.add(comment)
        comment.post = this
    }

    fun removeComment(comment: Comment) {
        comments.remove(comment)
        comment.post = null
    }
}