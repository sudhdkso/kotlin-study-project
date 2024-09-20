package com.study.boardproject.board.dto

import com.study.boardproject.board.entity.User
import org.springframework.security.crypto.password.PasswordEncoder

data class UserRequestDto(val email:String, val name:String, val password:String, val phoneNumber:String) {
    fun toEntity(encoder:PasswordEncoder) : User = User(email, name, encoder.encode(password), phoneNumber)
}

data class UserResponseDto(val email: String, val name: String) {
}

fun User.toDto() : UserResponseDto = UserResponseDto(email, name)