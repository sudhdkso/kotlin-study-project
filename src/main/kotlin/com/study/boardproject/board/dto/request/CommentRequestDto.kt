package com.study.boardproject.board.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Range

data class CommentRequestDto(
    @field:NotBlank
    @field:Size(min = 1, max = Int.MAX_VALUE)
    val content:String,

    @field:Range(min = 0, max = 1)
    val depth: Int,

    @field:NotNull
    val boardId:Long,

    @field:NotBlank
    val email:String
)
