package com.study.boardproject.comment.dto

import com.study.boardproject.comment.entity.Comment
import java.time.LocalDateTime


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