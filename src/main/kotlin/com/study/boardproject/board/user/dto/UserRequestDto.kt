package com.study.boardproject.board.user.dto

import com.study.boardproject.board.user.entity.User
import org.springframework.security.crypto.password.PasswordEncoder

data class UserRequestDto(val email:String, val name:String, val password:String, val phoneNumber:String) {
    fun toEntity(encoder: PasswordEncoder) : User = User(email, name, encoder.encode(password), phoneNumber)
}
