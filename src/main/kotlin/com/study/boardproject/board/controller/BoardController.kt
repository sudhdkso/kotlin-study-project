package com.study.boardproject.board.controller

import com.study.boardproject.board.dto.BoardCreateRequestDto
import com.study.boardproject.board.dto.BoardResponseDto
import com.study.boardproject.board.dto.BoardUpdateRequestDto
import com.study.boardproject.board.service.BoardService
import com.study.boardproject.core.annotation.CheckAccessLevel
import com.study.boardproject.post.dto.PostResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/boards")
class BoardController(private val boardService: BoardService) {

    @CheckAccessLevel(requiredLevels = [5])
    @PostMapping
    fun create(@RequestBody requestDto: BoardCreateRequestDto) : ResponseEntity<BoardResponseDto> {
        val responseDto = boardService.save(requestDto)
        return ResponseEntity.ok().body(responseDto)
    }

    @GetMapping("/{id}")
    fun getByBoardId(@PathVariable("id")boardId: Long) : ResponseEntity<List<PostResponseDto>> {
        val responseDto = boardService.getPostsByBoard(boardId)
        return ResponseEntity.ok().body(responseDto)
    }

    @GetMapping
    fun getAllBoard() : ResponseEntity<List<BoardResponseDto>> {
        val responseDto = boardService.getAllBoards()
        return ResponseEntity.ok().body(responseDto)
    }

    @CheckAccessLevel(requiredLevels = [5])
    @PutMapping
    fun update(requestDto:BoardUpdateRequestDto) : ResponseEntity<BoardResponseDto> {
        val responseDto = boardService.update(requestDto)
        return ResponseEntity.ok().body(responseDto)
    }
}