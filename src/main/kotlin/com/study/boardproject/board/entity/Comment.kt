package com.study.boardproject.board.entity

import com.study.boardproject.board.dto.request.CommentRequestDto
import jakarta.persistence.*

@Entity
@Table(name = "comment")
class Comment(content: String, depth: Int, board: Board, writer: User) : BaseEntity() {
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column(nullable = false, length = Int.MAX_VALUE)
    val content: String? = content

    @Column(nullable = false)
    val depth: Int = depth

    @JoinColumn(name = "boardId", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val board: Board? = board

    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val writer: User? = writer

    constructor(request: CommentRequestDto, board: Board, writer: User)
            : this(request.content, request.depth, board, writer) {
    }
}