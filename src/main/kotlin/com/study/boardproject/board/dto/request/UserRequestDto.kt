package com.study.boardproject.board.dto.request

import com.study.boardproject.board.entity.User

class UserRequestDto(val email:String, val name:String) {
    fun toEntity() : User = User(email, name)
}