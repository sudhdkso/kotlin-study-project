package com.study.boardproject.user.dto

data class LoginResponseDto(
    val email:String,
    val token:String,
    val type:String = "Bearer"
)