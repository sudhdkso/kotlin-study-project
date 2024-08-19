package com.study.boardproject.board.entity

import com.study.boardproject.board.createUser
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UserTest : FunSpec({

    context("유효한 값을 가진 유저가 있을 때, 사용자의 일부 값을 수정하면") {
        val user = createUser()

        val name = "변경"
        val phoneNumber = "010-4445-2223"

        user.update(name = name, phoneNumber = phoneNumber)

        test("변경하려는 이름으로 변경된다."){
            user.name shouldBe name
        }
        test("변경하려는 전화번호로 변경된다.") {
            user.phoneNumber shouldBe phoneNumber
        }
    }
})