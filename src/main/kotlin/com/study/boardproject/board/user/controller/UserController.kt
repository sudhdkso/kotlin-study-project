package com.study.boardproject.board.user.controller

import com.study.boardproject.board.user.dto.LoginRequestDto
import com.study.boardproject.board.user.dto.LoginResponseDto
import com.study.boardproject.board.user.dto.UserRequestDto
import com.study.boardproject.board.user.dto.UserResponseDto
import com.study.boardproject.board.user.service.LoginService
import com.study.boardproject.board.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
}