package com.study.boardproject.board.repository

import com.study.boardproject.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Long> {
    fun save(board: Board) : Board

    fun findAllByOrderByCreatedAtDesc(): List<Board>
}