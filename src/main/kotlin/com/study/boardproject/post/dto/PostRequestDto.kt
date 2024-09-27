package com.study.boardproject.post.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.user.entity.User
import com.study.boardproject.post.entity.Post
import com.study.boardproject.common.constants.BoardConstants
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PostRequestDto(
    val boardId: Long,

    @field:NotBlank(message = "title is empty")
    @field:Size(min = 1, max = BoardConstants.MAX_TITLE_LENGTH, message = "The length of the title must be 1 to ${BoardConstants.MAX_TITLE_LENGTH}")
    @JsonProperty("title")
    private val _title: String?,

    @field:NotBlank(message = "content is empty")
    @field:Size(
        min = 1,
        max = BoardConstants.MAX_CONTENT_LENGTH,
        message = "The length of the content must be 1 to ${BoardConstants.MAX_CONTENT_LENGTH}"
    )
    @JsonProperty("content")
    private val _content: String?,

    ) {
    val title: String
        get() = _title!!

    val content: String
        get() = _content!!

    fun toEntity(user: User, board: Board): Post {
        return Post(title, content, user, board)
    }
}