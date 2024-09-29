package com.study.boardproject.user.service

import com.study.boardproject.board.user.entity.enums.Level
import com.study.boardproject.board.user.repository.UserRepository
import com.study.boardproject.board.user.service.UserService
import com.study.boardproject.createUser
import com.study.boardproject.createUserRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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

    Given("매니저 레벨의 사용자가") {
        val manager = createUser(level = Level.MANAGER)
        val targetUserEmail = "target@example.com"
        val expectedLevel = Level.LEVEL_2.value
        val targetUser = createUser(email = targetUserEmail, level = Level.LEVEL_1)
        every { userService.findUserByEmail(any()) } returns targetUser
        every { userRepository.save(any()) } returns targetUser
        When("다른 사용자의 레벨을 변경하려고 하면") {
            userService.updateUserLevel(manager, targetUserEmail, expectedLevel)
            Then("성공한다.") {
                verify(exactly = 1) { userRepository.save(targetUser) }
            }
        }
    }

    Given("매니저 레벨이 아닌 사용자가") {
        val manager = createUser(level = Level.LEVEL_4)
        val targetUserEmail = "target@example.com"
        val expectedLevel = Level.LEVEL_2.value

        When("다른 사용자의 레벨을 변경하려고 하면") {
            Then("IllegalAccessException가 발생한다.") {
                shouldThrow<IllegalAccessException> {
                    userService.updateUserLevel(manager, targetUserEmail, expectedLevel)
                }.message shouldBe "매니저 권한이 없습니다."
            }
        }
    }
})