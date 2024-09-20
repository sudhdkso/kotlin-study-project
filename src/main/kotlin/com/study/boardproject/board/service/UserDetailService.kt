package com.study.boardproject.board.service

import com.study.boardproject.board.repository.UserRepository
import com.study.boardproject.board.repository.getByEmail
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.getByEmail(username)
    }
}