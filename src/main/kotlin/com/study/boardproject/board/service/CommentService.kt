package com.study.boardproject.board.service

import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.dto.CommentResponseDto
import com.study.boardproject.board.dto.toDto
import com.study.boardproject.board.repository.CommentRepository
import com.study.boardproject.board.repository.getByCommentId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commnetRepository: CommentRepository,
    private val postService: PostService,
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    @Transactional
    fun save(email: String, requestDto: CommentRequestDto): CommentResponseDto {

        val post = postService.findByPostId(requestDto.boardId)
        val writer = userService.findUserByEmail(email)

        checkRequest(requestDto)

        val comment = commnetRepository.save(requestDto.toEntity(post, writer))
        post.addComment(comment)

        if(post.writer != comment.writer){
            notificationService.sendCommentNotification(post, comment)
        }

        return comment.toDto()
    }

    fun update(commentId: Long, requestDto: CommentRequestDto): CommentResponseDto{

        checkRequest(requestDto)

        val comment = commnetRepository.getByCommentId(commentId)
        comment.update(requestDto)
        commnetRepository.save(comment)

        return commnetRepository.save(comment).toDto()
    }

    @Transactional
    fun delete(commentId: Long) {
        val comment = commnetRepository.getByCommentId(commentId)

        comment.post?.let{
            it.removeComment(comment)
        } ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다.")

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