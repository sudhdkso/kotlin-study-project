package com.study.boardproject

import com.study.boardproject.board.user.dto.UserRequestDto
import com.study.boardproject.board.user.entity.User
import com.study.boardproject.board.user.entity.enums.Level

const val NAME: String = "test"
const val EMAIL: String = "test1234@email.com"
const val PASSWORD: String = "1234"
const val PHONENUMBER: String = "010-1234-1234"
fun createUser(email:String = EMAIL, name:String = NAME, password:String = PASSWORD, phoneNumber:String = PHONENUMBER, level:Level = Level.LEVEL_1): User = User(email, name, password, phoneNumber, level = level)

fun createUserRequest(email:String = EMAIL, name:String = NAME, password:String = PASSWORD, phoneNumber:String = PHONENUMBER): UserRequestDto
= UserRequestDto(email, name, password, phoneNumber)