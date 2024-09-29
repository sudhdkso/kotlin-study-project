package com.study.boardproject.comment.service

import com.study.boardproject.user.entity.User
import com.study.boardproject.user.service.UserService
import com.study.boardproject.comment.dto.CommentCreateRequestDto
import com.study.boardproject.comment.dto.CommentRequestDto
import com.study.boardproject.comment.dto.CommentResponseDto
import com.study.boardproject.comment.dto.toDto
import com.study.boardproject.comment.entity.Comment
import com.study.boardproject.comment.repository.CommentRepository
import com.study.boardproject.comment.repository.getByCommentId
import com.study.boardproject.notification.service.NotificationService
import com.study.boardproject.post.service.PostService
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
    fun save(writer: User, requestDto: CommentCreateRequestDto): CommentResponseDto {

        val post = postService.findByPostId(requestDto.boardId)

        checkContent(requestDto.content)

        val comment = commentRepository.save(requestDto.toEntity(post, writer))
        post.addComment(comment)

        if(post.writer != writer){
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

    @Transactional
    fun update(commentId: Long, requestDto: CommentRequestDto): CommentResponseDto {
        checkContent(requestDto.content)

        val comment = commentRepository.getByCommentId(commentId)
        comment.update(requestDto.content)
        return comment.toDto()
    }

    @Transactional
    fun delete(commentId: Long) {
        val comment = commentRepository.getByCommentId(commentId)

        comment.post?.let{
            it.removeComment(comment)
        } ?: throw IllegalArgumentException("게시글을 찾을 수 없습니다.")

        commentRepository.delete(comment)
    }

    fun checkContent(content : String) {
        require(content.isNotBlank()) {
            "댓글의 내용이 비어있습니다."
        }
        require(content.length in 1..1000){
            "댓글은 1000자 이하로 작성해주세요."
        }
    }
}