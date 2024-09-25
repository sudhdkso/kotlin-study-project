package com.study.boardproject.core.annotation

import com.study.boardproject.board.service.CommentService
import com.study.boardproject.board.service.PostService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckRequestUser(val entityIdParam: String, val entityType: String)

@Aspect
@Component
class RequestUserCheckAspect(
    private val postService: PostService,
    private val commentService: CommentService
) {

    @Before("@annotation(checkRequestUser) && args(entityId, ..)")
    fun checkRequestUser(joinPoint: JoinPoint, checkRequestUser: CheckRequestUser, entityId: Long) {
        val args = joinPoint.args

        val entityIdValue = args.find { it is Long } as? Long
            ?: throw IllegalArgumentException("Entity ID not found")

        val authentication = SecurityContextHolder.getContext().authentication
        val currentUserEmail = authentication?.name ?: throw IllegalArgumentException("Unauthorized access")

        when (checkRequestUser.entityType) {
            "post" -> {
                val post = postService.findByPostId(entityIdValue)
                val writer = post?.writer ?: throw IllegalArgumentException("Unable to find the post author")

                if (writer.email != currentUserEmail) {
                    throw IllegalArgumentException("You are not the author of this post.")
                }

            }
            "comment" -> {
                val comment = commentService.findByCommentId(entityIdValue)
                val writer = comment?.writer ?: throw IllegalArgumentException("Unable to find the comment author")
                if(writer.email != currentUserEmail) {
                    throw IllegalArgumentException("You are not the author of this comment.")
                }
            }
            else -> throw IllegalArgumentException("Unsupported entity type")
        }
    }
}