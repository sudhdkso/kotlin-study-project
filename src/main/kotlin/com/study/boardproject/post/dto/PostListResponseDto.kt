package com.study.boardproject.post.dto

import com.study.boardproject.post.entity.Post
import java.time.LocalDateTime

data class PostListResponseDto(
    val id: Long,
    val title: String,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val writerName: String
) {
    constructor(post: Post, name: String) : this(
        post.id ?: -1,
        post.title ?: "Default Title",
        post.viewCount,
        post.createdAt ?: LocalDateTime.now(),
        name
    )
}

fun Post.toListDto(): PostListResponseDto = PostListResponseDto(
    this,
    name = writer?.name ?: "Unknown Writer"
)
