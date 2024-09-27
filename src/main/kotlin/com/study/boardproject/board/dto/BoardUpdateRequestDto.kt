package com.study.boardproject.board.dto


data class BoardUpdateRequestDto(
    val boardId: Long,
    val description: String?,
    val minReadLevel: Int?,
    val minWriteLevel: Int?
)

