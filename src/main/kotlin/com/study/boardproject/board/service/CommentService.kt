package com.study.boardproject.board.service

import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.dto.CommentResponseDto
import com.study.boardproject.board.dto.toDto
import com.study.boardproject.board.entity.Comment
import com.study.boardproject.board.repository.CommentRepository
import com.study.boardproject.board.repository.getByCommentId
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commnetRepository: CommentRepository,
    private val postService: PostService,
    private val userService: UserService
) {

    fun save(requestDto: CommentRequestDto): Comment {

        val board = postService.findByPostId(requestDto.boardId)
        val writer = userService.findUserByEmail(requestDto.email)

        checkRequest(requestDto)

        return commnetRepository.save(requestDto.toEntity(board, writer))
    }

    fun update(commentId: Long, requestDto: CommentRequestDto): CommentResponseDto{

        checkRequest(requestDto)

        val comment = commnetRepository.getByCommentId(commentId)
        comment.update(requestDto)
        commnetRepository.save(comment)

        return commnetRepository.save(comment).toDto()
    }

    fun delete(commentId: Long) {
        val comment = commnetRepository.getByCommentId(commentId)
        commnetRepository.delete(comment)
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