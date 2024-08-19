package com.study.boardproject.board.service

import com.study.boardproject.board.repository.UserRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class ValidTest : FunSpec({
    val userRepository: UserRepository = mockk()
    val userService = UserService(userRepository)


    test("이메일 유효성 검사에서 올바르지 않은 여러 형식이 입력되면 올바른 boolean타입의 값을 return한다."){
        forAll(
            row("example.com", false),
            row("asdef@example.com", true),
            row("", false),
            row("ecdf@.com",false),
            row("test.com",false),
            row("test@test.net",true)

        ) { input, expected ->
            userService.isValidEmail(input) shouldBe expected
        }
    }

    test("휴대전화 유효성 검사에서 올바르지 않은 여러 형식이 입력되면 올바른 boolean타입의 값을 return한다."){
        forAll(
            row("01012341234", true),
            row("010123412", false),
            row("010-1234-1234", true),
            row("010-1234",false),
            row("010-12-12",false),
            row("010-234-24421",false)

        ) { input, expected ->
            userService.isValidPhoneNumber(input) shouldBe expected
        }
    }
})