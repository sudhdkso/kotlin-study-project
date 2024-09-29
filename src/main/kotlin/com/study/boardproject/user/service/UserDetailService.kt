package com.study.boardproject.user.service

import com.study.boardproject.user.repository.UserRepository
import com.study.boardproject.user.repository.getByEmail
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.getByEmail(username)
    }
}