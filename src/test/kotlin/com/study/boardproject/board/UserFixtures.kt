package com.study.boardproject.board

import com.study.boardproject.board.user.dto.UserRequestDto
import com.study.boardproject.board.user.entity.User

const val NAME: String = "test"
const val EMAIL: String = "test1234@email.com"
const val PASSWORD: String = "1234"
const val PHONENUMBER: String = "010-1234-1234"
fun createUser(email:String = EMAIL, name:String = NAME, password:String = PASSWORD, phoneNumber:String = PHONENUMBER): User = User(email, name, password, phoneNumber)

fun createUserRequest(email:String = EMAIL, name:String = NAME, password:String = PASSWORD, phoneNumber:String = PHONENUMBER): UserRequestDto
= UserRequestDto(email, name, password, phoneNumber)