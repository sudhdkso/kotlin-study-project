package com.study.boardproject.board.dto

import com.study.boardproject.board.entity.User

data class UserRequestDto(val email:String, val name:String) {
    fun toEntity() : User = User(email, name)
}

data class UserResponseDto(val email: String, val name: String) {
}

fun User.toDto() : UserResponseDto = UserResponseDto(email, name)