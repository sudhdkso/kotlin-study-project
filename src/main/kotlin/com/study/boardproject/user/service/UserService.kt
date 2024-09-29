package com.study.boardproject.user.service

import com.study.boardproject.user.dto.UserRequestDto
import com.study.boardproject.user.dto.UserResponseDto
import com.study.boardproject.user.dto.UserUpdateRequestDto
import com.study.boardproject.user.dto.toDto
import com.study.boardproject.user.entity.User
import com.study.boardproject.user.entity.enums.Level
import com.study.boardproject.user.repository.UserRepository
import com.study.boardproject.user.repository.getByEmail
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val encoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(UserService::class.java)
    fun findUserByEmail(email: String): User = userRepository.getByEmail(email)

    @Transactional
    fun save(requestDto: UserRequestDto): UserResponseDto {

        checkEmail(requestDto.email)
        checkUserInput(requestDto.phoneNumber, requestDto.password)

        val user = userRepository.save(requestDto.toEntity(encoder))
        log.info("Created User: username={}, email={}", user.name, user.email)

        return user.toDto()
    }

    @Transactional
    fun update(user: User, requestDto: UserUpdateRequestDto) : UserResponseDto {
        checkUserInput(requestDto.phoneNumber, requestDto.password)
        user.update(requestDto.name, encoder.encode(requestDto.password), requestDto.phoneNumber)
        return user.toDto()
    }

    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as User
    }

    @Transactional
    fun updateUserLevel(manager: User, targetUserEmail: String, newLevel: Int) {
        if (manager.level != Level.MANAGER) {
            throw IllegalAccessException("매니저 권한이 없습니다.")
        }

        val targetUser = findUserByEmail(targetUserEmail)
        targetUser.changeLevel(Level.fromLevel(newLevel))
    }

    fun checkEmail(email:String){
        require(!isEmailDuplicate(email)) {
            "이메일이 중복되었습니다."
        }
        require(isValidEmail(email)) {
            "${email}은 올바른 이메일 형식이 아닙니다."
        }
    }

    fun checkUserInput(phoneNumber: String, password: String) {
        require(isValidPassword(password)){
            "올바른 비밀번호 형식이 아닙니다."
        }
        require(isValidPhoneNumber(phoneNumber)) {
            "${phoneNumber}는 올바른 전화번호 형식이 아닙니다."
        }
    }

    fun isValidPassword(password: String): Boolean {
        val lengthCheck = password.length in 8..15
        val upperCaseCheck = password.any { it.isUpperCase() }
        val lowerCaseCheck = password.any { it.isLowerCase() }
        val digitCheck = password.count { it.isDigit() } >= 5
        val specialCharCheck = password.any { it in "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\`~" }

        return lengthCheck && upperCaseCheck && lowerCaseCheck && digitCheck && specialCharCheck
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