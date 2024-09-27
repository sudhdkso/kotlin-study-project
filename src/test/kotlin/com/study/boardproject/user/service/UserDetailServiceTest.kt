package com.study.boardproject.user.service

import com.study.boardproject.createUser
import com.study.boardproject.board.user.repository.UserRepository
import com.study.boardproject.board.user.repository.getByEmail
import com.study.boardproject.board.user.service.UserDetailService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailServiceTest : BehaviorSpec({
    val userRepository = mockk<UserRepository>()
    val userDetailService = UserDetailService(userRepository)

    Given("이미 가입된 사용자의 이메일이라면") {
        val email = "test@example.com"
        val user = createUser(email = email)

        every { userRepository.getByEmail(email) } returns user

        When("loadUserByUsername를 email을 매개변수로 호출하면") {
            val result = userDetailService.loadUserByUsername(email)

            Then("올바른 userDetail을 return한다.") {
                result.username shouldBe email
                result.password shouldBe user.password
            }
        }
    }

    Given("가입되지 않은 이메일이라면") {
        val email = "notfound@example.com"

        every { userRepository.getByEmail(email) } throws UsernameNotFoundException(email)

        When("loadUserByUsername를 가입되지 않은 이메일로 호출하면") {
            Then("UsernameNotFoundException을 반환한다.") {
                shouldThrow<UsernameNotFoundException> {
                    userDetailService.loadUserByUsername(email)
                }
            }
        }
    }
})