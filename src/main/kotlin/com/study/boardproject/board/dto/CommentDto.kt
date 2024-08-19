package com.study.boardproject.board.dto

import com.study.boardproject.board.entity.Comment
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.entity.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Range
import java.time.LocalDateTime

data class CommentRequestDto(
    @field:NotBlank
    @field:Size(min = 1, max = Int.MAX_VALUE)
    val content:String,

    @field:Range(min = 0, max = 1)
    val depth: Int,

    @field:NotNull
    val boardId:Long,

    @field:NotBlank
    val email:String,

) {
    fun toEntity(post: Post, user: User) : Comment {
        return Comment(content, depth, post, user)
    }
}

data class CommentUpdateRequestDto(
    @field:NotBlank
    val commentId:Long,

    @field:NotBlank
    @field:Size(min = 1, max = Int.MAX_VALUE)
    val content:String,

    ) {
}

data class CommentResponseDto(
    val content : String?,
    val writerName : String,
    val createdAt :LocalDateTime,
    val modifiedAt : LocalDateTime
)

fun Comment.toDto() : CommentResponseDto = CommentResponseDto(
    content = content ?: "Default Content",
    writerName = writer?.name ?: "Unknown Writer",
    createdAt = createdAt ?: LocalDateTime.now(),
    modifiedAt = modifiedAt ?: LocalDateTime.now(),
)