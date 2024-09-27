package com.study.boardproject.post.service

import com.study.boardproject.board
import com.study.boardproject.board.service.BoardService
import com.study.boardproject.board.user.service.UserService
import com.study.boardproject.createPost
import com.study.boardproject.createPostRequest
import com.study.boardproject.createUser
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
        val email = "test@example.com"
        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardService.findByBoardId(any()) } returns board
        every { board.addPost(any()) } just runs

        every { postRepository.save(any()) } returns createPost(title = title)
        When("저장하면") {
            val actual = postService.save(email, request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }
    }

    Given("게시글 내용이 유효하지 않는 경우") {
        val email = "test@example.com"

        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardService.findByBoardId(any()) } returns board
        When("제목을 200자 초과로 작성하면") {
            val title = "a".repeat(201)
            val request = createPostRequest(title)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(email, request)
                }
            }
        }
        When("내용을 1000자 초과로 작성하면") {
            val content = "b".repeat(10001)
            val request = createPostRequest(content = content)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(email, request)
                }
            }
        }
        When("제목을 빈값으로 저장하면") {
            val title = ""
            val request = createPostRequest(title = title)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(email, request)
                }
            }
        }

        When("내용을 빈값으로 저장하면") {
            val content = ""
            val request = createPostRequest(content = content)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(email, request)
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
})