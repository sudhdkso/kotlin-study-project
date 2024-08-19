package com.study.boardproject.board.dto

import com.study.boardproject.board.entity.User

data class UserRequestDto(val email:String, val name:String, val password:String, val phoneNumber:String) {
    fun toEntity() : User = User(email, name, password, phoneNumber)
}

data class UserResponseDto(val email: String, val name: String) {
}

fun User.toDto() : UserResponseDto = UserResponseDto(email, name)