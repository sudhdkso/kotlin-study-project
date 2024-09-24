package com.study.boardproject.board.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.entity.User
import com.study.boardproject.util.constants.BoardConstants.MAX_CONTENT_LENGTH
import com.study.boardproject.util.constants.BoardConstants.MAX_TITLE_LENGTH
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class PostRequestDto(
    val boardId: Long,

    @field:NotBlank(message = "title is empty")
    @field:Size(min = 1, max = MAX_TITLE_LENGTH, message = "The length of the title must be 1 to ${MAX_TITLE_LENGTH}")
    @JsonProperty("title")
    private val _title: String?,

    @field:NotBlank(message = "content is empty")
    @field:Size(min = 1, max = MAX_CONTENT_LENGTH, message = "The length of the content must be 1 to ${MAX_CONTENT_LENGTH}")
    @JsonProperty("content")
    private val _content: String?,

) {
    val title: String
        get() = _title!!

    val content: String
        get() = _content!!

    fun toEntity(user: User, board: Board) : Post {
        return Post(title, content, user, board)
    }
}

data class PostListResponseDto(
    val id: Long,
    val title:String,
    val viewCount: Long,
    val createdAt : LocalDateTime,
    val writerName: String
) {
    constructor(post: Post, name:String) : this(
        post.id ?: -1,
        post.title ?: "Default Title",
        post.viewCount,
        post.createdAt ?: LocalDateTime.now(),
        name) {

    }
}

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val remainingDays: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
    val writerEmail: String,
    val writerName: String){

    constructor(post: Post, email:String, name:String) : this(
        post.id ?: -1,
        post.title ?: "Default Title",
        post.content ?: "Default Content",
        post.viewCount,
        post.calculateEditableDaysRemaining(),
        post.createdAt ?: LocalDateTime.now(),
        post.modifiedAt ?: LocalDateTime.now(),
        email,
        name) {

    }
}

fun Post.toDto() : PostResponseDto = PostResponseDto(
    this,
    email = writer?.email ?: "unknown@example.com",
    name = writer?.name ?: "Unknown Writer"
)
fun Post.toListDto(): PostListResponseDto = PostListResponseDto(
    this,
     name = writer?.name ?: "Unknown Writer"
)
