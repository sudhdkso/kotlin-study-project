package com.study.boardproject.post.controller

import com.study.boardproject.post.dto.PostListResponseDto
import com.study.boardproject.post.dto.PostRequestDto
import com.study.boardproject.post.dto.PostResponseDto
import com.study.boardproject.post.dto.toDto
import com.study.boardproject.post.service.PostService
import com.study.boardproject.core.annotation.CheckRequestUser
import com.study.boardproject.core.annotation.LoginUserEmail
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

@RequestMapping("/api/post")
@RestController
@Validated
class PostController(private val postService: PostService) {
    @PostMapping
    fun create(@LoginUserEmail email: String, @RequestBody @Valid requestDto: PostRequestDto) : ResponseEntity<PostResponseDto>{

        val postResponse = postService.save(email, requestDto)
        return ResponseEntity.ok().body(postResponse)
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable("id")postdId: Long, req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<PostResponseDto> {
        viewCountUp(postdId, req, res)
        val postResponse = postService.findByPostId(postdId).toDto()
        return ResponseEntity.ok().body(postResponse)
    }

    @GetMapping
    fun getAll(
        @RequestParam("sortBy") sortBy: String,
        @RequestParam("order") sortOrder: String,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ) : ResponseEntity<List<PostListResponseDto>> {
        val sortCriteria = SortCriteria.findSortCriteria(sortBy)
        val sortDirection = if (sortOrder.equals("desc", ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val sortedPageable = PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.by(sortDirection, sortCriteria.sortProperty))
        val responseList = postService.findAll(sortedPageable)
        return ResponseEntity.ok().body(responseList)
    }

    @GetMapping("/search")
    fun search(
        @RequestParam("query")
        @Size(min = 2, max = 50, message = "The length of the query must be 1 to 50")
        query:String
    ) : ResponseEntity<List<PostResponseDto>> {
        val responseList = postService.search(query)
        return ResponseEntity.ok().body(responseList)
    }

    @PutMapping("/{id}")
    @CheckRequestUser(entityIdParam = "id", entityType = "post")
    fun update(@PathVariable("id")postId: Long, @RequestBody @Valid requestDto: PostRequestDto) : ResponseEntity<PostResponseDto> {
        val postResponse = postService.update(postId, requestDto)
        return ResponseEntity.ok().body(postResponse)
    }

    @DeleteMapping("/{id}")
    @CheckRequestUser(entityIdParam = "id", entityType = "post")
    fun delete(@PathVariable("id")postId: Long): ResponseEntity<Any> {
        postService.deleteByPostId(postId)
        return ResponseEntity.ok().body("게시글 삭제에 성공했습니다.")
    }

    fun viewCountUp(postId: Long, req: HttpServletRequest, res: HttpServletResponse) {

        var oldCookie: Cookie?  = null
        val cookies = req.cookies

        if(cookies != null){
            for(cookie in cookies){
                if(cookie.name == "postView") {
                    oldCookie = cookie
                    break
                }
            }
        }

        val finalOldCookie = oldCookie

        if (finalOldCookie != null) {
            if (finalOldCookie.value?.contains("[$postId]") != true) {
                postService.viewCountup(postId)
                val newValue = (finalOldCookie.value ?: "") + "[$postId]"
                finalOldCookie.value = newValue
                finalOldCookie.path = "/"
                finalOldCookie.maxAge = 60 * 60 * 24
                res.addCookie(finalOldCookie)
            }
        } else {
            postService.viewCountup(postId)
            val newCookie = Cookie("postView", "[$postId]")
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
        fun findSortCriteria(sort : String) : SortCriteria {
            return values().first { it.sortProperty == sort }
        }
    }
}