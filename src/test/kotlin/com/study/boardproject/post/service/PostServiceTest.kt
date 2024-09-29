package com.study.boardproject.post.service

import com.study.boardproject.*
import com.study.boardproject.board.service.BoardService
import com.study.boardproject.board.user.service.UserService
import com.study.boardproject.notification.service.NotificationService
import com.study.boardproject.post.entity.Post
import com.study.boardproject.post.repository.PostRepository
import com.study.boardproject.post.repository.getByPostId
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class PostServiceTest : BehaviorSpec({
    val postRepository: PostRepository = mockk(relaxed = true)
    val userService: UserService = mockk()
    val notificationService: NotificationService = mockk()
    val boardService: BoardService = mockk()
    val postService = PostService(postRepository, userService, notificationService, boardService)


    Given("사용자와 게시글이 모두 유효한 경우") {
        val title = "테스트1"
        val request = createPostRequest()
        val user = createUser(email = "test@example.com")
        every { boardService.findByBoardId(any()) } returns createBoard()
        every { board.addPost(any()) } just runs

        every { postRepository.save(any()) } returns createPost(title = title)
        When("저장하면") {
            val actual = postService.save(user, request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }
    }

    Given("사용자 레벨이 쓰기 레벨보다 작은 경우") {
        val request = createPostRequest()
        val user = createUser(email = "test@example.com")
        every { boardService.findByBoardId(any()) } returns createBoard(minWriteLevel = 5)

        When("게시글을 저장하면") {
            Then("IllegalAccessException이 발생한다.") {
                shouldThrow<IllegalAccessException> {
                    postService.save(user, request)
                }.message shouldBe "접근 권한이 없습니다."
            }
        }
    }

    Given("게시글 내용이 유효하지 않는 경우") {
        val user = createUser(email = "test@example.com")
        every { boardService.findByBoardId(any()) } returns createBoard()
        When("제목을 200자 초과로 작성하면") {
            val title = "a".repeat(201)
            val request = createPostRequest(title)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(user, request)
                }
            }
        }
        When("내용을 1000자 초과로 작성하면") {
            val content = "b".repeat(10001)
            val request = createPostRequest(content = content)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(user, request)
                }
            }
        }
        When("제목을 빈값으로 저장하면") {
            val title = ""
            val request = createPostRequest(title = title)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(user, request)
                }
            }
        }

        When("내용을 빈값으로 저장하면") {
            val content = ""
            val request = createPostRequest(content = content)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(user, request)
                }
            }
        }
    }

    Given("게시글이 유효한 경우") {

        val title = "수정"
        val request = createPostRequest(title = title)
        val post = spyk<Post>(createPost())
        val boardId = 1L

        every { postService.findByPostId(any()) } returns post
        every { boardService.findByBoardId(any()) } returns board

        every { post.board } returns board
        every { postRepository.save(any()) } returns createPost(title = title)
        every { postRepository.delete(any()) } just runs

        every { post.canEditPost() } returns true
        When("수정하면") {
            val actual = postService.update(boardId, request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }

        When("삭제하면") {
            postService.deleteByPostId(boardId)
            Then("성공한다.") {
                post.deletedAt shouldNotBe null

            }
        }
    }

    Given("존재하지 않는 게시글을") {
        val title = "수정"
        val postId = 1L
        val request = createPostRequest(title = title)
        every { postRepository.getByPostId(any()) } throws NoSuchElementException()
        When("조회하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    postService.findByPostId(postId)
                }
            }
        }
        When("수정하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    postService.update(postId, request)
                }
            }
        }
        When("삭제하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    postService.deleteByPostId(postId)
                }
            }
        }
    }

    Given("10일이 지난 게시글을") {
        val title = "수정"
        val request = createPostRequest(title = title)
        val post = spyk<Post>(createPost())
        val postId = 1L

        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardService.findByBoardId(any()) } returns board
        every { postService.findByPostId(any()) } returns post

        every { post.canEditPost() } returns false

        When("수정하려고 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.update(postId, request)
                }
            }
        }

    }

    Given("사용자 레벨이 읽기 레벨을 충족하는"){
        val user = createUser()
        val postId = 1L
        val content = "Test Content"
        every {postService.findByPostId(any())} returns createPost(content = content, board = createBoard(minReadLevel = 1))

        When("조회하려고 할 때"){
            val result = postService.getByPostId(user, postId)
            Then("성공적으로 조회된다."){
                result.content shouldBe content
            }
        }
    }

    Given("사용자 레벨보다 읽기 레벨이 더 높은 게시글을"){
        val user = createUser()
        val postId = 1L
        every {postService.findByPostId(any())} returns createPost(board = createBoard(minReadLevel = 5))

        When("조회하려고 할 때"){
            Then("IllegalAccessException가 발생한다."){
                shouldThrow<IllegalAccessException> {
                    postService.getByPostId(user, postId)
                }.message shouldBe "접근 권한이 없습니다."
            }
        }
    }
})