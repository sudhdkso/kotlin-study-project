package com.study.boardproject.board.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.boardproject.board.entity.Board
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class BoardCreateRequestDto(
    @field:NotBlank(message = "title is empty")
    @field:Size(min = 1, max = 20, message = "The length of the title must be 1 to 20")
    @JsonProperty("title")
    val title: String,

    @field:NotBlank(message = "description is empty")
    @field:Size(min = 1, max = 200, message = "The length of the description must be 1 to 200")
    @JsonProperty("description")
    val description:String, //300자

    val minReadLevel: Int,

    val minWriteLevel: Int
) {
    fun toEntity() : Board  = Board(title, description, minReadLevel, minWriteLevel)
}

data class BoardUpdateRequestDto(
    val boardId: Long,
    val description: String?,
    val minReadLevel: Int?,
    val minWriteLevel: Int?
)

data class BoardResponseDto(
    val title: String,
    val description: String,
    val minReadLevel: Int,
    val minWriteLevel: Int,
    val createdAt: LocalDateTime,
    val moditiedAt: LocalDateTime
)

fun Board.toDto() : BoardResponseDto {
    return BoardResponseDto(title, description, minReadLevel, minWriteLevel, createdAt ?: LocalDateTime.now() , modifiedAt ?: LocalDateTime.now())
}