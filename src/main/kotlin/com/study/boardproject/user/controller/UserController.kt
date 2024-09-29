package com.study.boardproject.user.controller

import com.study.boardproject.board.user.dto.*
import com.study.boardproject.user.entity.User
import com.study.boardproject.user.service.LoginService
import com.study.boardproject.user.service.UserService
import com.study.boardproject.user.dto.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
    private val loginService: LoginService
) {

    @PostMapping("/signup")
    fun register(@RequestBody requestDto: UserRequestDto) : ResponseEntity<UserResponseDto>{
        val responseDto = userService.save(requestDto)
        return ResponseEntity.ok().body(responseDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody requestDto: LoginRequestDto) : ResponseEntity<LoginResponseDto> {
        val responseDto = loginService.login(requestDto)
        return ResponseEntity.ok().body(responseDto)
    }

    @PostMapping("/users/change-level")
    fun changeUserLevel(
        @AuthenticationPrincipal user: User,
        @RequestParam targetUserEmail: String,
        @RequestParam newLevel: Int
    ): ResponseEntity<String> {
        return try {
            userService.updateUserLevel(user, targetUserEmail, newLevel)
            ResponseEntity.ok("사용자 등급이 성공적으로 변경되었습니다.")
        } catch (e: IllegalAccessException) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    @PutMapping("/users")
    fun update(
        @AuthenticationPrincipal user: User,
        @RequestBody request: UserUpdateRequestDto
    ) : ResponseEntity<UserResponseDto>{
        val response = userService.update(user, request)
        return ResponseEntity.ok().body(response)
    }
}