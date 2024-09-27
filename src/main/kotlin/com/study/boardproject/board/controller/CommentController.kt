package com.study.boardproject.board.controller

import com.study.boardproject.board.dto.CommentCreateRequestDto
import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.dto.CommentResponseDto
import com.study.boardproject.board.service.CommentService
import com.study.boardproject.core.annotation.CheckRequestUser
import com.study.boardproject.core.annotation.LoginUserEmail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(private val commentService: CommentService) {

    @PostMapping
    fun create(@LoginUserEmail email:String, @RequestBody request: CommentCreateRequestDto) : ResponseEntity<CommentResponseDto> {
        val response = commentService.save(email, request)
        return ResponseEntity.ok().body(response)
    }

    @PutMapping("/{id}")
    @CheckRequestUser(entityIdParam = "id", entityType = "comment")
    fun update(@PathVariable("id")commentId:Long, @RequestBody request: CommentRequestDto) : ResponseEntity<CommentResponseDto> {
        val response = commentService.update(commentId, request)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping
    fun getCommentsByPostId(@RequestParam postId:Long) : ResponseEntity<List<CommentResponseDto>> {
        val response = commentService.findCommentsByPostId(postId)
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/{id}")
    @CheckRequestUser(entityIdParam = "id", entityType = "comment")
    fun delete(@PathVariable("id") commentId:Long) : ResponseEntity<Any> {
        commentService.delete(commentId)
        return ResponseEntity.ok().body("댓글 삭제에 성공했습니다.")
    }
}