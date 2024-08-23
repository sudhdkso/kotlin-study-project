package com.study.boardproject.board.repository

import com.study.boardproject.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun BoardRepository.getByBoardId(boardId: Long) : Board {
    return findByIdOrNull(boardId) ?: throw NoSuchElementException()
}

interface BoardRepository : JpaRepository<Board,Long> {
    fun existsByTitle(title: String) : Boolean
}