package com.study.boardproject.board.controller

import com.study.boardproject.board.dto.request.UserRequestDto
import com.study.boardproject.board.dto.response.UserResponseDto
import com.study.boardproject.board.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
@RestController
class UserController(val userService: UserService) {

    @PostMapping
    fun create(@RequestBody requestDto: UserRequestDto) : ResponseEntity<UserResponseDto>{
        val responseDto = userService.save(requestDto)
        return ResponseEntity.ok().body(responseDto)
    }
}