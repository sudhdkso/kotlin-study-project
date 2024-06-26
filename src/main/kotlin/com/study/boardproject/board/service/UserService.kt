package com.study.boardproject.board.service

import com.study.boardproject.board.dto.UserRequestDto
import com.study.boardproject.board.dto.UserResponseDto
import com.study.boardproject.board.dto.toDto
import com.study.boardproject.board.entity.User
import com.study.boardproject.board.repository.UserRepository
import com.study.boardproject.board.repository.getByEmail
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {
    fun findUserByEmail(email: String): User = userRepository.getByEmail(email)

    fun save(requestDto: UserRequestDto) : UserResponseDto {
        val user = userRepository.save(requestDto.toEntity())
        return user.toDto()
    }
}