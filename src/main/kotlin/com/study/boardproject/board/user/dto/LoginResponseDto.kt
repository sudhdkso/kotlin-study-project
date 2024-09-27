package com.study.boardproject.board.user.dto

data class LoginResponseDto(
    val email:String,
    val token:String,
    val type:String = "Bearer"
)