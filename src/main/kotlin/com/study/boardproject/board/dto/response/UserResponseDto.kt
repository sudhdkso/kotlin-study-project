package com.study.boardproject.board.dto.response

import com.study.boardproject.board.entity.User

class UserResponseDto(val email: String, val name: String) {
}

fun User.toDto() : UserResponseDto = UserResponseDto(email, name)