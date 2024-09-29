package com.study.boardproject.comment.dto

import com.study.boardproject.comment.entity.Comment
import com.study.boardproject.user.entity.User
import com.study.boardproject.post.entity.Post
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CommentCreateRequestDto(
    @field:NotBlank
    @field:Size(min = 1, max = Int.MAX_VALUE)
    val content:String,

    @field:NotNull
    val boardId:Long
) {
    fun toEntity(post: Post, user: User) : Comment {
        return Comment(content, post, user)
    }
}