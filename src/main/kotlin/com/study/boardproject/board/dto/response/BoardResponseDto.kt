package com.study.boardproject.board.dto.response

import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.User
import java.time.LocalDateTime

data class BoardResponseDto(
    val title: String,
    val content: String,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
    val writerEmail: String,
    val writerName: String){

    constructor(board: Board, user: User) : this(
        board.title ?: "Default Title",
        board.content ?: "Default Content",
        board.viewCount,
        board.createdAt ?: LocalDateTime.now(),
        board.modifiedAt ?: LocalDateTime.now(),
        user.email,
        user.name) {

    }
}

fun Board.toDto() : BoardResponseDto = BoardResponseDto(
    title = title ?: "Default Title",
    content = content ?: "Default Content",
    viewCount = viewCount,
    createdAt = LocalDateTime.now(),
    modifiedAt = LocalDateTime.now(),
    writerEmail = writer?.email ?: "unknown@example.com",
    writerName = writer?.name ?: "Unknown Writer"
)
