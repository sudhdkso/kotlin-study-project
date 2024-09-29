package com.study.boardproject.comment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CommentRequestDto(
    @field:NotBlank
    @field:Size(min = 1, max = 1000)
    val content: String
)
