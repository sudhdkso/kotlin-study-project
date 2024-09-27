package com.study.boardproject.board.user.service

import com.study.boardproject.board.user.dto.UserRequestDto
import com.study.boardproject.board.user.dto.UserResponseDto
import com.study.boardproject.board.user.dto.toDto
import com.study.boardproject.board.user.entity.User
import com.study.boardproject.board.user.repository.UserRepository
import com.study.boardproject.board.user.repository.getByEmail
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val encoder: PasswordEncoder,
                  private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(UserService::class.java)
    fun findUserByEmail(email: String): User = userRepository.getByEmail(email)

    fun save(requestDto: UserRequestDto) : UserResponseDto {

        checkUserInput(requestDto.email, requestDto.phoneNumber)

        val user = userRepository.save(requestDto.toEntity(encoder))
        log.info("Created User: username={}, email={}", user.name, user.email)

        return user.toDto()
    }

    fun checkUserInput(email:String, phoneNumber: String) {
        require(!isEmailDuplicate(email)) {
            "이메일이 중복되었습니다."
        }
        require(isValidEmail(email)){
            "${email}은 올바른 이메일 형식이 아닙니다."
        }
        require(isValidPhoneNumber(phoneNumber)){
            "${phoneNumber}는 올바른 전화번호 형식이 아닙니다."
        }
    }
    fun isEmailDuplicate(email: String): Boolean = userRepository.existsByEmail(email)

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}+\$".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneRegex = "^(010-\\d{4}-\\d{4}|010\\d{8})\$".toRegex()
        return phoneRegex.matches(phoneNumber)
    }
}