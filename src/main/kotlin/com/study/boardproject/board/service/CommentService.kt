package com.study.boardproject.board.service

import com.study.boardproject.board.dto.request.CommentRequestDto
import com.study.boardproject.board.entity.Comment
import com.study.boardproject.board.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(
    val commnetRepository: CommentRepository,
    val boardService: BoardService,
    val userService: UserService
) {

    fun save(requestDto: CommentRequestDto) : Comment{

        val board = boardService.findByBoardId(requestDto.boardId)
        val writer = userService.findUserByEmail(requestDto.email)

        checkRequest(requestDto)

        val savedComment = commnetRepository.save(requestDto.toEntity(board, writer))
        return savedComment
    }

    fun checkRequest(requestDto: CommentRequestDto) {
        require(requestDto.content.isNotBlank()) {
            "댓글의 내용이 비어있습니다."
        }

        require(requestDto.depth == 0 || requestDto.depth == 1) {
            "옳지 않은 등록입니다."
        }
    }
}