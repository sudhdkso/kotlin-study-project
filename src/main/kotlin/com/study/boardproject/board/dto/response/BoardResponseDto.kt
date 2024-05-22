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
    val email: String,
    val name: String){

    constructor(board: Board, user: User) : this(board.title!!, board.content!!, board.viewCount, board.createdAt!!, board.modifiedAt!!, user.email, user.name) {

    }
}
