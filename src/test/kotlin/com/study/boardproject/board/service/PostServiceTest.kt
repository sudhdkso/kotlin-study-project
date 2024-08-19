package com.study.boardproject.board.service

import com.study.boardproject.board.createPost
import com.study.boardproject.board.createPostRequest
import com.study.boardproject.board.createUser
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.repository.PostRepository
import com.study.boardproject.board.repository.getByPostId
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
    val postService = PostService(postRepository, userService, notificationService)

    Given("사용자와 게시글이 모두 유효한 경우") {
        val title = "테스트1"
        val request = createPostRequest()

        every { userService.findUserByEmail(any()) } returns createUser()
        every { postRepository.save(any()) } returns createPost(title = title)

        When("저장하면") {
            val actual = postService.save(request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }
    }

    Given("게시글 내용이 유효하지 않는 경우") {
        every { userService.findUserByEmail(any()) } returns createUser()
        When("제목을 200자 초과로 작성하면") {
            val title = "a".repeat(201)
            val request = createPostRequest(title)
            Then("예외를 반환한다."){
                shouldThrow<IllegalArgumentException> {
                    postService.save(request)
                }
            }
        }
        When("내용을 1000자 초과로 작성하면") {
            val content = "b".repeat(10001)
            val request = createPostRequest(content = content)
            Then("예외를 반환한다."){
                shouldThrow<IllegalArgumentException> {
                    postService.save(request)
                }
            }
        }
        When("제목을 빈값으로 저장하면") {
            val title = ""
            val request = createPostRequest(title = title)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(request)
                }
            }
        }

        When("내용을 빈값으로 저장하면") {
            val content = ""
            val request = createPostRequest(content = content)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    postService.save(request)
                }
            }
        }
    }

    Given("게시글이 유효한 경우") {

        val title = "수정"
        val request = createPostRequest(title = title)
        val board = spyk<Post>(createPost())
        val boardId = 1L

        every { userService.findUserByEmail(any()) } returns createUser()
        every { postService.findByPostId(any()) } returns board

        every { postRepository.save(any()) } returns createPost(title = title)
        every { postRepository.delete(any()) } just runs

        every { board.canEditPost() } returns true
        When("수정하면") {
            val actual = postService.update(boardId, request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }

        When("삭제하면") {
            postService.deleteByPostId(boardId)
            Then("성공한다.") {
                board.deletedAt shouldNotBe null
                
            }
        }
    }

    Given("존재하지 않는 게시글을") {
        val title = "수정"
        val boardId = 1L
        val request = createPostRequest(title = title)
        every { postRepository.getByPostId(any()) } throws NoSuchElementException()
        When("조회하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    postService.findByPostId(boardId)
                }
            }
        }
        When("수정하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    postService.update(boardId, request)
                }
            }
        }
        When("삭제하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    postService.deleteByPostId(boardId)
                }
            }
        }
    }

    Given("10일이 지난 게시글을") {
        val title = "수정"
        val request = createPostRequest(title = title)
        val board = spyk<Post>(createPost())
        val boardId = 1L

        every { userService.findUserByEmail(any()) } returns createUser()
        every { postService.findByPostId(any()) } returns board

        every { board.canEditPost() } returns false

        When("수정하려고 하면") {
            Then("예외가 발생한다."){
                shouldThrow<IllegalArgumentException> {
                    postService.update(boardId, request)
                }
            }
        }

    }
})