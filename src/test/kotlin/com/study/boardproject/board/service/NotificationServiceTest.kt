package com.study.boardproject.board.service

import com.study.boardproject.board.createComment
import com.study.boardproject.board.createPost
import com.study.boardproject.board.createUser
import com.study.boardproject.board.entity.Notification
import com.study.boardproject.board.entity.User
import com.study.boardproject.board.repository.EmitterRepository
import com.study.boardproject.board.repository.NotificationRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class NotificationServiceTest : BehaviorSpec({
    val notificationRepository: NotificationRepository = mockk(relaxed = true)
    val emitterRepository: EmitterRepository = mockk(relaxed = true)
    val notificationService = NotificationService(notificationRepository, emitterRepository)

    Given("a post with a writer") {
        val author: User = mockk(relaxed = true)

        every { author.id } returns 1L
        every { author.name } returns "Author"
        val post = createPost(title = "Test Post", writer = author)

        When("sendEditDeadlineNotification is called") {
            val notification = mockk<Notification>(relaxed = true)
            every { notification.id } returns 1L
            every { notification.receiver } returns post.writer!!
            every { notification.message } returns " 게시글 [${post.title}] 수정 기간이 하루 남았습니다."
            every { notification.type } returns Notification.NotificationType.EDIT_PERIOD_IMMINENT

            every { notificationRepository.save(any()) } returns notification

            notificationService.sendEditDeadlineNotification(post)

            Then("a notification should be saved and sent") {
                val expectedMessage = " 게시글 [${post.title}] 수정 기간이 하루 남았습니다."
                verify {
                    notificationRepository.save(withArg<Notification> {
                        it.message shouldBe expectedMessage
                        it.receiver shouldBe post.writer
                        it.type shouldBe Notification.NotificationType.EDIT_PERIOD_IMMINENT
                    })
                }
            }
        }

        When("sendCommentNotification is called with a comment") {
            val commenter: User = mockk(relaxed = true)

            every { commenter.id } returns 2L
            every { commenter.name } returns "Commenter"

            val comment = createComment(
                content = "Test Comment",
                writer = commenter
            )

            val notification = mockk<Notification>(relaxed = true)
            every { notification.id } returns 2L
            every { notification.receiver } returns post.writer!!
            every { notification.message } returns "게시글 [${post.title}]에 ${comment.writer?.name}님이 ${comment.content}라고 댓글을 달았습니다."
            every { notification.type } returns Notification.NotificationType.COMMENT_ADDED_NOTIFICATION

            every { notificationRepository.save(any()) } returns notification

            notification.javaClass.getDeclaredField("id").apply {
                isAccessible = true
                set(notification, 2L)
            }

            notificationService.sendCommentNotification(post, comment)

            Then("a notification should be saved and sent") {
                val expectedMessage = "게시글 [${post.title}]에 ${comment.writer?.name}님이 ${comment.content}라고 댓글을 달았습니다."
                verify {
                    notificationRepository.save(withArg<Notification> {
                        it.message shouldBe expectedMessage
                        it.receiver shouldBe post.writer
                        it.type shouldBe Notification.NotificationType.COMMENT_ADDED_NOTIFICATION
                    })
                }
            }
        }
    }

    Given("a post without a writer") {
        val post = createPost(title = "Test Post", writer = null)

        When("sendEditDeadlineNotification is called") {
            Then("an IllegalArgumentException should be thrown") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendEditDeadlineNotification(post)
                }.message shouldBe "사용자를 찾을 수 없습니다."
            }
        }

        When("sendCommentNotification is called") {
            val comment = createComment(
                content = "Test Comment",
                writer = createUser(email = "Commenter@example.com", name = "Commenter")
            )

            Then("an IllegalArgumentException should be thrown") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendCommentNotification(post, comment)
                }.message shouldBe "사용자를 찾을 수 없습니다."
            }
        }
    }


    Given("a post with a writer but save returns null ID") {
        val post = createPost(title = "Test Post", writer = createUser(email = "Author@example.com", name = "Author"))

        When("sendEditDeadlineNotification is called and save returns null ID") {

            val notification = mockk<Notification>(relaxed = true)

            every { notification.id } returns null
            every { notification.receiver } returns post.writer!!
            every { notification.type } returns Notification.NotificationType.EDIT_PERIOD_IMMINENT

            every { notificationRepository.save(any()) } returns notification


            Then("an IllegalArgumentException should be thrown") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendEditDeadlineNotification(post)
                }.message shouldBe "알림을 찾을 수 없습니다."
            }
        }

        When("sendCommentNotification is called and save returns null ID") {
            val comment = createComment(
                content = "Test Comment",
                writer = createUser(email = "Commenter@example.com", name = "Commenter")
            )

            val notification = mockk<Notification>(relaxed = true)

            every { notification.id } returns null
            every { notification.receiver } returns post.writer!!
            every { notification.message } returns "게시글 [${post.title}]에 ${comment.writer?.name}님이 ${comment.content}라고 댓글을 달았습니다."
            every { notification.type } returns Notification.NotificationType.COMMENT_ADDED_NOTIFICATION

            every { notificationRepository.save(any()) } returns notification

            Then("an IllegalArgumentException should be thrown") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendCommentNotification(post, comment)
                }.message shouldBe "알림을 찾을 수 없습니다."
            }
        }
    }
})