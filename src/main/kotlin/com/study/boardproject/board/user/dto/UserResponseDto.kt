package com.study.boardproject.board.user.dto

import com.study.boardproject.board.user.entity.User


data class UserResponseDto(val email: String, val name: String) {
}

fun User.toDto() : UserResponseDto = UserResponseDto(email, name)