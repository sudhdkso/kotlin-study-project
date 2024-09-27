package com.study.boardproject.board.service

import com.study.boardproject.board.createUser
import com.study.boardproject.board.createUserRequest
import com.study.boardproject.board.user.repository.UserRepository
import com.study.boardproject.board.user.service.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val userService: UserService = spyk(UserService(BCryptPasswordEncoder(), userRepository))

    Given("유효한 값들로") {
        val name = "유효한"
        val request = createUserRequest(name = name)

        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.save(any()) } returns createUser(name = name)

        When("사용자 회원가입 시") {
            val result = userService.save(request)
            Then("잘 통과한다.") {
                request.name shouldBe result.name
            }
        }
    }

    Given("중복된 이메일로") {
        val request = createUserRequest()
        every { userRepository.existsByEmail(any()) } returns true
        When("사용자 회원가입 시") {
            Then("에러가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    userService.save(request)
                }
            }
        }
    }

    Given("올바르지 않는 이메일 형식으로") {
        val invaildEmail = "asdf.com"
        val request = createUserRequest(email = invaildEmail)

        every { userService.isEmailDuplicate(request.email) } returns false

        When("사용자 회원가입 시") {
            Then("에러가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    userService.save(request)
                }.message shouldBe "${request.email}은 올바른 이메일 형식이 아닙니다."
            }
        }
    }

    Given("올바르지 않는 휴대전화 형식으로") {
        val invaildPhoneNumber = "010-22-3345"
        val request = createUserRequest(phoneNumber = invaildPhoneNumber)
        every { userService.isEmailDuplicate(request.email) } returns false

        When("사용자 회원가입 시") {
            Then("에러가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    userService.save(request)
                }.message shouldBe "${request.phoneNumber}는 올바른 전화번호 형식이 아닙니다."
            }
        }
    }
}) {
}