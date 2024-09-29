package com.study.boardproject.user.entity

import com.study.boardproject.board.user.entity.enums.Level
import com.study.boardproject.createUser
import io.kotest.assertions.throwables.shouldThrow
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

    test("등급이 매니저가 아닌 경우 정상적으로 등급이 변경되어야 한다") {
        val user = createUser(level = Level.LEVEL_1)
        val newLevel = Level.LEVEL_2

        user.changeLevel(newLevel)

        user.level shouldBe newLevel
    }


    test("매니저 레벨의 유저 레벨을 변경하려고 하면 IllegalArgumentException이 발생한다.") {
        val user = createUser(level = Level.MANAGER)
        val newLevel = Level.LEVEL_2

        shouldThrow<IllegalArgumentException> {
            user.changeLevel(newLevel)
        }.message shouldBe "매니저는 등급을 변경할 수 없습니다."
    }
})