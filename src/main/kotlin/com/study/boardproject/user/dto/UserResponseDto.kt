package com.study.boardproject.user.dto

import com.study.boardproject.user.entity.User


data class UserResponseDto(val email: String, val name: String) {
}

fun User.toDto() : UserResponseDto = UserResponseDto(email, name)