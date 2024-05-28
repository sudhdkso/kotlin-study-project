package com.study.boardproject.board

import com.study.boardproject.board.entity.User

const val NAME: String = "test"
const val EMAIL: String = "test1234@email.com"

fun createUser(email:String = EMAIL, name:String = NAME): User = User(email, name)
