package com.study.boardproject.board.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class BoardRequestDto(
    @field:NotBlank(message = "title is empty")
    @field:Size(min = 1, max = 20, message = "The length of the title must be 1 to 20")
    @JsonProperty("title")
    private val _title: String?,

    @field:NotBlank(message = "content is empty")
    @field:Size(min = 1, max = Int.MAX_VALUE, message = "The length of the content must be 1 to Int.MAX_VALUE")
    @JsonProperty("content")
    private val _content: String?,

    val email: String
) {
    val title: String
        get() = _title!!

    val content: String
        get() = _content!!

    fun toEntity(user: User) : Board {
        return Board(title, content, user)
    }
}

data class BoardResponseDto(
    val title: String,
    val content: String,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
    val writerEmail: String,
    val writerName: String){

    constructor(board: Board, user: User) : this(
        board.title ?: "Default Title",
        board.content ?: "Default Content",
        board.viewCount,
        board.createdAt ?: LocalDateTime.now(),
        board.modifiedAt ?: LocalDateTime.now(),
        user.email,
        user.name) {

    }
}

fun Board.toDto() : BoardResponseDto = BoardResponseDto(
    title = title ?: "Default Title",
    content = content ?: "Default Content",
    viewCount = viewCount,
    createdAt = LocalDateTime.now(),
    modifiedAt = LocalDateTime.now(),
    writerEmail = writer?.email ?: "unknown@example.com",
    writerName = writer?.name ?: "Unknown Writer"
)

