package com.study.boardproject.post.dto

import com.study.boardproject.comment.dto.CommentResponseDto
import com.study.boardproject.comment.dto.toDto
import com.study.boardproject.post.entity.Post
import java.time.LocalDateTime

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val remainingDays: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
    val writerEmail: String,
    val writerName: String,
    val comments: List<CommentResponseDto>
) {

    constructor(post: Post, email: String, name: String) : this(
        post.id ?: -1,
        post.title ?: "Default Title",
        post.content ?: "Default Content",
        post.viewCount,
        post.calculateEditableDaysRemaining(),
        post.createdAt ?: LocalDateTime.now(),
        post.modifiedAt ?: LocalDateTime.now(),
        email,
        name,
        post.comments.map { it.toDto() }
    )
}


fun Post.toDto(): PostResponseDto = PostResponseDto(
    this,
    email = writer?.email ?: "unknown@example.com",
    name = writer?.name ?: "Unknown Writer"
)

