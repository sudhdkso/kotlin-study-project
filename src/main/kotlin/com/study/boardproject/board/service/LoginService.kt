package com.study.boardproject.board.service

import com.study.boardproject.board.dto.LoginRequestDto
import com.study.boardproject.board.dto.LoginResponseDto
import com.study.boardproject.board.repository.UserRepository
import com.study.boardproject.board.repository.getByEmail
import com.study.boardproject.jwt.TokenProvider
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Transactional
@Service
class LoginService(
    private val encoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider
) {

    fun login(requestDto: LoginRequestDto): LoginResponseDto {

        val user = userRepository.getByEmail(requestDto.email)
            .takeIf { encoder.matches(requestDto.password, it.password) }
            ?: throw IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.")

        val authenticationToken = UsernamePasswordAuthenticationToken(requestDto.email, requestDto.password)
        val authentication = authenticationManagerBuilder.build().authenticate(authenticationToken)

        return LoginResponseDto(user.email, tokenProvider.createToken(authentication))
    }
}