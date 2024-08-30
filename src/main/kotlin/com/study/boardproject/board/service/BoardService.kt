package com.study.boardproject.board.service

import com.study.boardproject.board.dto.*
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.repository.BoardRepository
import com.study.boardproject.board.repository.getByBoardId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardRepository: BoardRepository
) {

    //TODO
    // -게시판 delete (soft, hard)

    fun findByBoardId(boardId: Long): Board {
        return boardRepository.getByBoardId(boardId)
    }

    @Transactional
    fun save(requestDto: BoardCreateRequestDto): BoardResponseDto {
        validateTitleLength(requestDto.title)
        validateDescriptionLength(requestDto.description)

        checkBoardTitleDuplicated(requestDto.title)

        val board = boardRepository.save(requestDto.toEntity())
        return board.toDto()
    }

    @Transactional
    fun update(requestDto: BoardUpdateRequestDto): BoardResponseDto {

        requestDto.description?.let { validateDescriptionLength(it) }

        val board = findByBoardId(requestDto.boardId)
        board.update(requestDto)
        return board.toDto()
    }

    fun getAllBoards(): List<BoardResponseDto> {
        return boardRepository.findAll().map { it.toDto() }
    }

    fun getPostsByBoard(boardId: Long): List<PostResponseDto> {
        val board = findByBoardId(boardId)
        return board.posts.map { it.toDto() }
    }


    fun checkBoardTitleDuplicated(title:String){
        require(!boardRepository.existsByTitle(title)){
            "${title}과 동일한 이름의 게시판이 존재합니다."
        }
    }

    fun validateTitleLength(title: String) {
        require(title.length <= 20) {
            "게시판 이름이 20자를 초과하였습니다."
        }
    }

    fun validateDescriptionLength(description: String) {
        require(description.length <= 100) {
            "게시판 설명이 100자를 초과하였습니다."
        }
    }
}