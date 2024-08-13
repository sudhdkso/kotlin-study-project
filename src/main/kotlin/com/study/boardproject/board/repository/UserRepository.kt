package com.study.boardproject.board.repository

import com.study.boardproject.board.entity.User
import org.springframework.data.jpa.repository.JpaRepository

fun UserRepository.getByEmail(email: String) : User {
    return findByEmail(email) ?: throw NoSuchElementException()
}

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String) : Boolean
}