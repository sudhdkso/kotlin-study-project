package com.study.boardproject.notification.service

import com.study.boardproject.board.user.entity.User
import com.study.boardproject.createComment
import com.study.boardproject.createPost
import com.study.boardproject.createUser
import com.study.boardproject.notification.entity.Notification
import com.study.boardproject.notification.repository.EmitterRepository
import com.study.boardproject.notification.repository.NotificationRepository
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

    Given("게시글이 작성되어있을 때") {
        val author: User = mockk(relaxed = true)

        every { author.id } returns 1L
        every { author.name } returns "Author"
        val post = createPost(title = "Test Post", writer = author)

        When("sendEditDeadlineNotification가 호출되면") {
            val notification = mockk<Notification>(relaxed = true)
            every { notification.id } returns 1L
            every { notification.receiver } returns post.writer!!
            every { notification.message } returns " 게시글 [${post.title}] 수정 기간이 하루 남았습니다."
            every { notification.type } returns Notification.NotificationType.EDIT_PERIOD_IMMINENT

            every { notificationRepository.save(any()) } returns notification

            notificationService.sendEditDeadlineNotification(post)

            Then("알람이 저장 및 전송된다.") {
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

        When("게시글에 댓글이 달리면 sendCommentNotification를 호출되어") {
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

            Then("알림이 전송된다.") {
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

    Given("게시글에 사용자가 null일 때") {
        val post = createPost(title = "Test Post", writer = null)

        When("sendEditDeadlineNotification이 호출되면") {
            Then("IllegalArgumentException가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendEditDeadlineNotification(post)
                }.message shouldBe "사용자를 찾을 수 없습니다."
            }
        }

        When("sendCommentNotification이 호출되면") {
            val comment = createComment(
                content = "Test Comment",
                writer = createUser(email = "Commenter@example.com", name = "Commenter")
            )

            Then("IllegalArgumentException가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendCommentNotification(post, comment)
                }.message shouldBe "사용자를 찾을 수 없습니다."
            }
        }
    }


    Given("게시글이 저장되어있을 때") {
        val post = createPost(title = "Test Post", writer = createUser(email = "Author@example.com", name = "Author"))

        When("sendEditDeadlineNotification호출하여 저장한 후 notification이 null을 return받으면") {

            val notification = mockk<Notification>(relaxed = true)

            every { notification.id } returns null
            every { notification.receiver } returns post.writer!!
            every { notification.type } returns Notification.NotificationType.EDIT_PERIOD_IMMINENT

            every { notificationRepository.save(any()) } returns notification


            Then("IllegalArgumentException가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendEditDeadlineNotification(post)
                }.message shouldBe "알림을 찾을 수 없습니다."
            }
        }

        When("sendCommentNotification 호출하여 저장한 후 notification이 null을 return받으면") {
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

            Then("IllegalArgumentException가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    notificationService.sendCommentNotification(post, comment)
                }.message shouldBe "알림을 찾을 수 없습니다."
            }
        }
    }
})