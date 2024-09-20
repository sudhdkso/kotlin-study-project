package com.study.boardproject.board.controller

import com.study.boardproject.board.dto.LoginRequestDto
import com.study.boardproject.board.dto.LoginResponseDto
import com.study.boardproject.board.dto.UserRequestDto
import com.study.boardproject.board.dto.UserResponseDto
import com.study.boardproject.board.service.LoginService
import com.study.boardproject.board.service.UserService
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
    fun login(@RequestBody requestDto:LoginRequestDto) : ResponseEntity<LoginResponseDto> {
        val responseDto = loginService.login(requestDto)
        return ResponseEntity.ok().body(responseDto)
    }
}