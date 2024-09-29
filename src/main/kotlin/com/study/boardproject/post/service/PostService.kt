package com.study.boardproject.post.service

import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.service.BoardService
import com.study.boardproject.board.user.entity.User
import com.study.boardproject.board.user.entity.enums.Level
import com.study.boardproject.board.user.service.UserService
import com.study.boardproject.common.constants.BoardConstants.MAX_CONTENT_LENGTH
import com.study.boardproject.common.constants.BoardConstants.MAX_TITLE_LENGTH
import com.study.boardproject.notification.service.NotificationService
import com.study.boardproject.post.dto.*
import com.study.boardproject.post.entity.Post
import com.study.boardproject.post.repository.PostRepository
import com.study.boardproject.post.repository.getByPostId
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val notificationService: NotificationService,
    private val boardService: BoardService
) {

    @Transactional
    fun save(user:User, requestDto: PostRequestDto): PostResponseDto {
        val board = boardService.findByBoardId(requestDto.boardId)

        val accessLevel = board?.minWriteLevel ?: Level.EMPTY.value
        checkAccessLevel(user.level.value, accessLevel)

        validateRequest(requestDto)

        val post = postRepository.save(requestDto.toEntity(user, board))

        board.addPost(post)
        return PostResponseDto(post, user.email, user.name)
    }

    fun findByPostId(postId: Long): Post = postRepository.getByPostId(postId)

    fun findAll(pageable: Pageable): List<PostListResponseDto> {
        return postRepository.findByDeletedAtIsNull(pageable).map { it.toListDto() }.toList()
    }

    fun getByPostId(user: User, postId: Long) : PostResponseDto{
        val post = findByPostId(postId)

        val accessLevel = post.board?.minReadLevel ?: Level.EMPTY.value
        checkAccessLevel(user.level.value, accessLevel)

        return post.toDto()
    }

    @Transactional
    fun update(postId: Long, requestDto: PostRequestDto): PostResponseDto {
        val post = findByPostId(postId)
        val board = boardService.findByBoardId(requestDto.boardId)

        checkEditablePost(post)
        validateRequest(requestDto)

        if (isBoardChanged(post.board, board)) {
            post.updateBoard(board)
        }

        post.update(requestDto)
        return post.toDto()
    }

    @Transactional
    fun deleteByPostId(postId: Long) {
        val post = findByPostId(postId)
        //soft delete
        post.delete()
    }

    fun search(query: String): List<PostResponseDto> {
        val postList = postRepository.searchByTitleOrContent(query)
        return postList.map { it.toDto() }
    }

    @Transactional
    fun viewCountup(postId: Long) {
        val post = findByPostId(postId)
        post.viewCountUp()
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

    private fun checkAccessLevel(userLevel: Int, accessLevel: Int){
        if(userLevel < accessLevel){
            throw IllegalAccessException("접근 권한이 없습니다.")
        }
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

    private fun isBoardChanged(oldBoard: Board?, newBoard: Board): Boolean {
        return oldBoard != newBoard
    }
}


