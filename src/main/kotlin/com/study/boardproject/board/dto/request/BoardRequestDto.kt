package com.study.boardproject.board.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BoardRequestDto(
    @field:NotNull(message = "title is empty")
    @field:Size(min = 1, max = 20, message = "The length of the title must be 1 to 20")
    @JsonProperty("title")
    private val _title: String?,

    @field:NotNull(message = "content is empty")
    @field:Size(min = 1, max = Int.MAX_VALUE, message = "The length of the content must be 1 to Int.MAX_VALUE")
    @JsonProperty("content")
    private val _content: String?,

    val email: String
) {
    val title: String
        get() = _title!!

    val content: String
        get() = _content!!
}
