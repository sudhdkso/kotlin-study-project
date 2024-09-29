package com.study.boardproject

import com.study.boardproject.board.dto.BoardCreateRequestDto
import com.study.boardproject.board.dto.BoardUpdateRequestDto
import com.study.boardproject.board.entity.Board
import io.mockk.mockk


private val TITLE: String = "게시판 제목"
private val DESCRIPTION: String = "게시판 설명"
private val MIN_READ_LEVEL: Int = 1
private val MIN_WRITE_LEVEL: Int = 1

val board:Board = mockk()

fun createBoardRequest(
    title: String = TITLE,
    description: String = DESCRIPTION,
    minReadLevel: Int = MIN_READ_LEVEL,
    minWriteLevel: Int = MIN_WRITE_LEVEL
): BoardCreateRequestDto {
    return BoardCreateRequestDto(title, description, minReadLevel, minWriteLevel)
}

fun createBoardUpdateRequest(
    boardId: Long = 1L,
    description: String = DESCRIPTION,
    minReadLevel: Int = MIN_READ_LEVEL,
    minWriteLevel: Int = MIN_WRITE_LEVEL
) : BoardUpdateRequestDto {
    return BoardUpdateRequestDto(boardId, description, minReadLevel, minWriteLevel)
}

fun createBoard(
    title: String = TITLE,
    description: String = DESCRIPTION,
    minReadLevel: Int = MIN_READ_LEVEL,
    minWriteLevel: Int = MIN_WRITE_LEVEL
): Board {
    return Board(title, description, minReadLevel, minWriteLevel)
}
