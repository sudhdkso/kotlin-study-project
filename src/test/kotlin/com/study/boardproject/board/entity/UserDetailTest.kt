package com.study.boardproject.board.entity

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class UserDetailTest: StringSpec({
    "getUsername은 email을 return한다." {
        val user = User("detail@example.com", "test", "password")

        user.username shouldBe "detail@example.com"
    }

    "getPassword는 password를 return한다." {
        val user = User("detail@example.com", "test", "password")


        user.password shouldBe "password"
    }

    "getAuthorities는 null을 return한다." {
        val user = User("detail@example.com", "test", "password")

        user.authorities shouldBe null
    }

    "isAccountNonExpired는 true를 return한다." {
        val user = User("detail@example.com", "test", "password")

        user.isAccountNonExpired shouldBe true
    }

    "isAccountNonLocked는 true를 return한다." {
        val user = User("detail@example.com", "test", "password")

        user.isAccountNonLocked shouldBe true
    }

    "isCredentialsNonExpired는 true를 return한다." {
        val user = User("detail@example.com", "test", "password")

        user.isCredentialsNonExpired shouldBe true
    }

    "isEnabled는 true를 한다." {
        val user = User("detail@example.com", "test", "password")

        user.isEnabled shouldBe true
    }
})