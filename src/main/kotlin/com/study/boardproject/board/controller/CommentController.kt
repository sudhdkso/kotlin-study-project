package com.study.boardproject.board.controller

import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.dto.CommentResponseDto
import com.study.boardproject.board.service.CommentService
import com.study.boardproject.core.annotation.LoginUserEmail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(private val commentService: CommentService) {
    //TODO
    // -update
    // -delete
    // -read

    @PostMapping
    fun create(@LoginUserEmail email:String, @RequestBody request:CommentRequestDto) : ResponseEntity<CommentResponseDto> {
        val response = commentService.save(email, request)
        return ResponseEntity.ok().body(response)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id")commentId:Long, @RequestBody request: CommentRequestDto) : ResponseEntity<CommentResponseDto> {
        val response = commentService.update(commentId, request)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping
    fun getCommentsByPostId(@RequestParam postId:Long) : ResponseEntity<List<CommentResponseDto>> {
        val response = commentService.findCommentsByPostId(postId)
        return ResponseEntity.ok().body(response)
    }
}