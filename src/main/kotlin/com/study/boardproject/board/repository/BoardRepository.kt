package com.study.boardproject.board.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.QBoard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface BoardRepositoryCustom {
    fun searchByTitleOrContent(searchQuery: String): List<Board>
}

@Repository
class BoardRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Board::class.java), BoardRepositoryCustom {
    override fun searchByTitleOrContent(searchQuery: String): List<Board> {
        val board = QBoard.board
        return queryFactory.selectFrom(board)
            .where(board.title.containsIgnoreCase(searchQuery)
                .or(board.content.containsIgnoreCase(searchQuery)))
            .orderBy(board.createdAt.desc())
            .fetch()
    }
}

fun BoardRepository.getByBoardId(id: Long) : Board{
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}

interface BoardRepository : JpaRepository<Board, Long>,BoardRepositoryCustom{
    fun save(board: Board) : Board

}