package com.study.boardproject.board.service

import com.study.boardproject.board.createUser
import com.study.boardproject.board.dto.LoginRequestDto
import com.study.boardproject.board.repository.UserRepository
import com.study.boardproject.board.repository.getByEmail
import com.study.boardproject.jwt.TokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class LoginServiceTest : BehaviorSpec({
    val userRepository:UserRepository = mockk()
    val encoder : PasswordEncoder = BCryptPasswordEncoder()
    val authenticationManager: AuthenticationManager = mockk()
    val userDetailService: UserDetailService = mockk()
    val tokenProvider: TokenProvider = mockk()
    val loginService = LoginService(encoder, userRepository, authenticationManager, userDetailService, tokenProvider)

    Given("올바른 정보를 가지고") {
        val email = "login@example.com"
        val password = "password"
        val mockUser = createUser(email = email, password = encoder.encode(password))
        val request = LoginRequestDto(email, password)

        every {userRepository.getByEmail(any())} returns mockUser
        every { tokenProvider.createToken(any()) } returns "mock-token"
        every { userDetailService.loadUserByUsername(any()) } returns mockk<UserDetails>()
        When("로그인을 시도하면") {
            val result = loginService.login(request)
            Then("토큰이 발급된다.") {
                result.token shouldBe "mock-token"
            }
        }
    }

    Given("올바르지 않는 비밀번호를 가지고") {
        val email = "login@example.com"
        val password = "password"
        val mockUser = createUser(email = email, password = encoder.encode(password))
        val request = LoginRequestDto(email, "ppp123")

        every {userRepository.getByEmail(any())} returns mockUser

        When("로그인을 시도하면") {
            val result =
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException>{
                    loginService.login(request)
                }.message shouldBe "아이디 또는 비밀번호가 일치하지 않습니다."
            }
        }
    }
})