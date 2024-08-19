package com.study.boardproject.board.service

import com.study.boardproject.board.dto.*
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.repository.PostRepository
import com.study.boardproject.board.repository.getByPostId
import com.study.boardproject.util.constants.BoardConstants.MAX_CONTENT_LENGTH
import com.study.boardproject.util.constants.BoardConstants.MAX_TITLE_LENGTH
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val notificationService: NotificationService
) {
    // 생성
    fun save(requestDto: PostRequestDto): PostResponseDto {
        val user = userService.findUserByEmail(requestDto.email)

        validateRequest(requestDto)
        val board = postRepository.save(requestDto.toEntity(user))

        return PostResponseDto(board, user)
    }

    fun findByPostId(postId: Long): Post = postRepository.getByPostId(postId)

    //게시글 전체 조회는 최근 작성순 or 조회순 두가지
    fun findAll(pageable: Pageable): List<PostListResponseDto> {
        return postRepository.findByDeletedAtIsNull(pageable).map { it.toListDto() }.toList()
    }

    //업데이트
    fun update(postId: Long, requestDto: PostRequestDto): PostResponseDto {
        val post = findByPostId(postId)

        checkEditablePost(post)
        validateRequest(requestDto)

        post.update(requestDto)
        val savedBoard = postRepository.save(post)
        return savedBoard.toDto()
    }

    //삭제
    fun deleteByPostId(postId: Long) {
        val post = findByPostId(postId)
        //soft delete
        post.delete()
        postRepository.save(post)
    }

    fun search(query: String): List<PostResponseDto> {
        val postList = postRepository.searchByTitleOrContent(query)
        return postList.map { it.toDto() }
    }

    fun viewCountup(boardId: Long) {
        val board = findByPostId(boardId)
        board.viewCountUp()
        postRepository.save(board)
    }

    fun findBoardWithEditDedlineSoon(): List<Post> {
        val now = LocalDateTime.now()
        val nineDaysAgo = now.minusDays(9)
        val tenDaysAgo = now.minusDays(10)
        return postRepository.findWithEditPeriodImminent(nineDaysAgo, tenDaysAgo)
    }

    fun sendEditDeadlineNotifications() {
        val boards = findBoardWithEditDedlineSoon()
        boards.forEach { board -> notificationService.sendEditDeadlineNotification(board) }
    }

    private fun checkEditablePost(post: Post) {
        require(post.canEditPost()) {
            "게시글 작성 10일 지나 게시글 수정이 불가능합니다."
        }
    }

    private fun validateRequest(request: PostRequestDto) {
        require(request.title.length <= MAX_TITLE_LENGTH) {
            "게시글의 제목은 200자 이하로 작성해주세요."
        }

        require(request.content.length in 0..MAX_CONTENT_LENGTH) {
            "게시글의 내용은 1000자 이하로 작성해주세요."
        }

        require(request.title.isNotBlank() && request.content.isNotBlank()) {
            "작성하지 않은 항목이 존재합니다."
        }
    }
}


