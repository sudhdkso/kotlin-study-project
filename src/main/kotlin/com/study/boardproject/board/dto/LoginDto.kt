package com.study.boardproject.board.dto

data class LoginRequestDto(
    val email:String,
    val password:String
)


data class LoginResponseDto(
    val email:String,
    val token:String,
    val type:String = "Bearer"
)