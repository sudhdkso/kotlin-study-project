package com.study.boardproject.board.entity

import com.study.boardproject.board.dto.CommentRequestDto
import jakarta.persistence.*

@Entity
@Table(name = "comment")
class Comment(content: String, depth: Int, post: Post, writer: User) : BaseEntity() {
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column(nullable = false, length = Int.MAX_VALUE)
    var content: String? = content

    @Column(nullable = false)
    val depth: Int = depth

    @JoinColumn(name = "postId", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post? = post

    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val writer: User? = writer

    fun update(requestDto: CommentRequestDto) {
        content = requestDto.content
    }
}