package com.study.boardproject.board.service

import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.dto.CommentResponseDto
import com.study.boardproject.board.dto.toDto
import com.study.boardproject.board.entity.Comment
import com.study.boardproject.board.repository.CommentRepository
import com.study.boardproject.board.repository.getByCommentId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postService: PostService,
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    @Transactional
    fun save(email: String, requestDto: CommentRequestDto): CommentResponseDto {

        val post = postService.findByPostId(requestDto.boardId)
        val writer = userService.findUserByEmail(email)

        checkRequest(requestDto)

        val comment = commentRepository.save(requestDto.toEntity(post, writer))
        post.addComment(comment)

        if(post.writer != comment.writer){
            notificationService.sendCommentNotification(post, comment)
        }

        return comment.toDto()
    }

    fun findByCommentId(commentId : Long) : Comment {
        return commentRepository.getByCommentId(commentId)
    }

    fun findCommentsByPostId(postId:Long) : List<CommentResponseDto>{
        val post = postService.findByPostId(postId)
        return post.comments.map { it.toDto() }
    }

    fun update(commentId: Long, requestDto: CommentRequestDto): CommentResponseDto{

        checkRequest(requestDto)

        val comment = commentRepository.getByCommentId(commentId)
        comment.update(requestDto)
        commentRepository.save(comment)

        return commentRepository.save(comment).toDto()
    }

    @Transactional
    fun delete(commentId: Long) {
        val comment = commentRepository.getByCommentId(commentId)

        comment.post?.let{
            it.removeComment(comment)
        } ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다.")

        commentRepository.delete(comment)
    }

    fun checkRequest(requestDto: CommentRequestDto) {
        require(requestDto.content.isNotBlank()) {
            "댓글의 내용이 비어있습니다."
        }
    }
}