package com.study.boardproject.board.controller

import com.study.boardproject.board.dto.BoardRequestDto
import com.study.boardproject.board.dto.BoardResponseDto
import com.study.boardproject.board.service.BoardService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/board")
@RestController
@Validated
class BoardController(val boardService: BoardService) {
    @PostMapping
    fun create(@RequestBody @Valid requestDto: BoardRequestDto) : ResponseEntity<BoardResponseDto>{
        val boardResponse = boardService.save(requestDto)
        return ResponseEntity.ok().body(boardResponse)
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable("id")boardId: Long, req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<BoardResponseDto> {
        viewCountUp(boardId, req, res)
        val boardResponse = boardService.findOne(boardId)
        return ResponseEntity.ok().body(boardResponse)
    }

    @GetMapping
    fun getAll(
        @RequestParam("sort") sort: String,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ) : ResponseEntity<List<BoardResponseDto>> {
        val sortCriteria = SortCriteria.findSortCriteria(sort)
        val sortedPageable = PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.by(Sort.Order.desc(sortCriteria.sortProperty)))
        val responseList = boardService.findAll(sortedPageable)
        return ResponseEntity.ok().body(responseList)
    }

    @GetMapping("/search")
    fun search(

        @RequestParam("query")
        @Size(min = 2, max = 50, message = "The length of the query must be 1 to 50")
        query:String
    ) : ResponseEntity<List<BoardResponseDto>> {
        val responseList = boardService.search(query)
        return ResponseEntity.ok().body(responseList)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id")boardId: Long, @RequestBody @Valid requestDto: BoardRequestDto) : ResponseEntity<BoardResponseDto> {
        val boardResponse = boardService.update(boardId, requestDto)
        return ResponseEntity.ok().body(boardResponse)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id")boardId: Long): ResponseEntity<Any> {
        boardService.deleteByBoardId(boardId)
        return ResponseEntity.ok().body("게시글 삭제에 성공했습니다.")
    }

    fun viewCountUp(boardId: Long, req: HttpServletRequest, res: HttpServletResponse) {

        var oldCookie: Cookie?  = null
        val cookies = req.cookies

        if(cookies != null){
            for(cookie in cookies){
                if(cookie.name == "boardView") {
                    oldCookie = cookie
                    break
                }
            }
        }

        val finalOldCookie = oldCookie

        if (finalOldCookie != null) {
            if (finalOldCookie.value?.contains("[$boardId]") != true) {
                boardService.viewCountup(boardId)
                val newValue = (finalOldCookie.value ?: "") + "[$boardId]"
                finalOldCookie.value = newValue
                finalOldCookie.path = "/"
                finalOldCookie.maxAge = 60 * 60 * 24
                res.addCookie(finalOldCookie)
            }
        } else {
            boardService.viewCountup(boardId)
            val newCookie = Cookie("boardView", "[$boardId]")
            newCookie.path = "/"
            newCookie.maxAge = 60 * 60 * 24
            res.addCookie(newCookie)
        }

    }

}

enum class SortCriteria(val sortProperty: String) {
    CREATED_DATE("createdAt"),
    VIEWCOUNT("viewCount");

    companion object {
        fun findSortCriteria(sort : String) : SortCriteria{
            return values()
                .filter { it.sortProperty == sort}
                .first()
        }
    }
}