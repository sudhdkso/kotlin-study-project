package com.study.boardproject.board.dto

import com.study.boardproject.board.entity.Board
import java.time.LocalDateTime

data class BoardResponseDto(
    val title: String,
    val description: String,
    val minReadLevel: Int,
    val minWriteLevel: Int,
    val createdAt: LocalDateTime,
    val moditiedAt: LocalDateTime
)

fun Board.toDto() : BoardResponseDto {
    return BoardResponseDto(title, description, minReadLevel, minWriteLevel, createdAt ?: LocalDateTime.now() , modifiedAt ?: LocalDateTime.now())
}