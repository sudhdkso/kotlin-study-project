package com.study.boardproject.comment.service


import com.study.boardproject.*
import com.study.boardproject.board.user.service.UserService
import com.study.boardproject.comment.repository.CommentRepository
import com.study.boardproject.comment.repository.getByCommentId
import com.study.boardproject.notification.service.NotificationService
import com.study.boardproject.post.service.PostService
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
class CommnetServiceTest : BehaviorSpec({

    val commentRepository: CommentRepository = mockk(relaxed = true)
    val postService: PostService = mockk()
    val userService: UserService = mockk()
    val notificationService: NotificationService = mockk()
    val commentService = CommentService(commentRepository, postService, userService, notificationService)

    Given("댓글 생성을 유효한 값으로 요청할 때") {
        val content = "댓글1"
        val request = createCommentCreateRequest(content = content)
        val email = "test@example.com"
        val post = createPost()
        val user = createUser(email = email)
        val comment = createComment(content = content)

        every { commentRepository.save(any()) } returns comment
        every { postService.findByPostId(any()) } returns post

        every { notificationService.sendCommentNotification(any(), any()) } just runs

        When("저장하면") {
            val actual = commentService.save(user, request)
            Then("성공한다.") {
                actual.content shouldBe content
            }
            Then("알림을 생성한다.") {
                verify(exactly = 1) { notificationService.sendCommentNotification(any(), any()) }
            }
        }
    }

    Given("댓글 내용이 빈 값으로 주어질 때") {
        val content = ""
        val request = createCommentCreateRequest(content = content)
        val email = "test@example.com"
        val post = createPost()
        val user = createUser(email = email)

        every { postService.findByPostId(any()) } returns post

        When("저장하면") {
            Then("IllegalArgumentException가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    commentService.save(user, request)
                }.message shouldBe "댓글의 내용이 비어있습니다."
            }

        }
    }

    Given("댓글 변경을 유효한값으로 요청할 때") {
        val commentId = 1L
        val email = "test@example.com"
        val user = createUser(email = email)

        val updateContent = "update content"
        val request = createCommentRequest(content = updateContent)
        val updateComment = createComment(content = updateContent)

        val previousContent = "previous content"
        val comment = createComment(content = previousContent, writer = user)

        every { commentRepository.getByCommentId(commentId) } returns comment
        every { commentRepository.save(any()) } returns updateComment

        When("수정하면") {
            val result = commentService.update(commentId, request)
            Then("성공한다.") {
                result.content shouldNotBe previousContent
            }
        }
    }

    Given("게시글이 할당되어 있는 댓글이") {
        val commentId = 1L
        val post = createPost()
        val comment = createComment(content = "Test comment", post = post)

        every { commentRepository.getByCommentId(any()) } returns comment

        When("삭제를 요청하면") {
            commentService.delete(commentId)
            Then("댓글은 삭제된다.") {
                verify { commentRepository.delete(comment) }
                post.comments shouldBe emptyList()
            }
        }
    }

    Given("댓글이 게시글 없이") {
        val commentId = 1L
        val comment = createComment(content = "Test comment", post = null)

        every { commentRepository.getByCommentId(commentId) } returns comment

        When("삭제를 요청하면") {
            Then("IllegalArgumentException이 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    commentService.delete(commentId)
                }.message shouldBe "게시글을 찾을 수 없습니다."
            }
        }
    }

})