package com.study.boardproject.board.service

import com.study.boardproject.board.createPost
import com.study.boardproject.notification.service.NotificationService
import com.study.boardproject.post.service.PostScheduler
import com.study.boardproject.post.service.PostService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class PostSechedulerTest() : BehaviorSpec({

    Given("작성한지 9일된 게시글은") {
        val post = createPost()
        val notificationService: NotificationService =  mockk(relaxed = true)
        val postService: PostService = spyk<PostService>(PostService( mockk(), mockk(), notificationService, mockk()))
        val postSecheduler: PostScheduler = PostScheduler(postService)

        every { postService.findBoardWithEditDedlineSoon() } returns listOf(post)

        When("스케쥴러를 통해서"){
            postSecheduler.notifyEditDeadline()
            Then("알림 전송이 호출된다."){
               verify { notificationService.sendEditDeadlineNotification(post) }
            }
        }
    }
}) 