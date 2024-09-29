package com.study.boardproject.user.entity

import com.study.boardproject.user.entity.enums.Role
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserDetailTest: StringSpec({
    "getUsername은 email을 return한다." {
        val user = User("detail@example.com", "test", "password")

        user.username shouldBe "detail@example.com"
    }

    "getPassword는 password를 return한다." {
        val user = User("detail@example.com", "test", "password")


        user.password shouldBe "password"
    }

    "getAuthorities는 User을 return한다." {
        val user = User("detail@example.com", "test", "password", role = Role.USER)

        user.authorities shouldBe mutableListOf(SimpleGrantedAuthority(Role.USER.type))
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