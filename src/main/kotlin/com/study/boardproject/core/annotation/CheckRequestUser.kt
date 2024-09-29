package com.study.boardproject.core.annotation

import com.study.boardproject.comment.service.CommentService
import com.study.boardproject.post.service.PostService
import com.study.boardproject.user.entity.User
import com.study.boardproject.user.entity.enums.Level
import com.study.boardproject.user.service.UserService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckRequestUser(val entityIdParam: String, val entityType: String)

@Aspect
@Component
class RequestUserCheckAspect(
    private val postService: PostService,
    private val commentService: CommentService,
    private val userService: UserService
) {

    @Before("@annotation(checkRequestUser) && args(entityId, ..)")
    fun checkRequestUser(joinPoint: JoinPoint, checkRequestUser: CheckRequestUser, entityId: Long) {
        val args = joinPoint.args

        val entityIdValue = args.find { it is Long } as? Long
            ?: throw IllegalArgumentException("Entity ID not found")

        val currentUser = userService.getCurrentUser()

        val methodSignature = joinPoint.signature
        val method = (methodSignature as MethodSignature).method

        val isDeleteMapping = method.isAnnotationPresent(DeleteMapping::class.java)
        val isPutMapping = method.isAnnotationPresent(PutMapping::class.java)

        when (checkRequestUser.entityType) {
            "post" -> {
                val post = postService.findByPostId(entityIdValue)
                if (isPutMapping) {
                    validateAuthor(post?.writer, currentUser)
                } else if (isDeleteMapping) {
                    validateAuthorOrManager(post?.writer, currentUser, "post")
                }
            }
            "comment" -> {
                val comment = commentService.findByCommentId(entityIdValue)
                if (isPutMapping) {
                    validateAuthor(comment?.writer, currentUser)
                } else if (isDeleteMapping) {
                    validateAuthorOrManager(comment?.writer, currentUser, "comment")
                }
            }
            else -> throw IllegalArgumentException("지원하지 않는 타입입ㄴ디ㅏ.")
        }
    }
}


private fun validateAuthor(writer: User?, currentUser: User) {
    writer ?: throw IllegalArgumentException("작성자를 찾을 수 없습니다.")
    if (writer.email != currentUser.email) {
        throw IllegalArgumentException("작성자가 아닙니다.")
    }
}

private fun validateAuthorOrManager(writer: User?, currentUser: User, entityType: String) {
    writer ?: throw IllegalArgumentException("작성자를 찾을 수 없습니다.")
    if (currentUser.level != Level.MANAGER && writer.email != currentUser.email) {
        throw IllegalArgumentException("삭제할 수 없습니다.")
    }
}