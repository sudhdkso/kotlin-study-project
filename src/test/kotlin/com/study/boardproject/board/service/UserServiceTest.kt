package com.study.boardproject.board.service

import com.study.boardproject.board.entity.User
import org.junit.jupiter.api.Test

class UserServiceTest {

    @Test
    fun findUserByEmailTest() {
    }

    @Test
    fun saveUserTest() {
    }

    fun createUser(email: String = "test1@email.com", name:String = "test") : User = User(email,name)
}