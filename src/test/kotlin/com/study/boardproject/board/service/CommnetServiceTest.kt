package com.study.boardproject.board.service


import com.study.boardproject.board.createBoard
import com.study.boardproject.board.createComment
import com.study.boardproject.board.createCommentRequest
import com.study.boardproject.board.createUser
import com.study.boardproject.board.repository.CommentRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class CommnetServiceTest : BehaviorSpec ({

    val commentRepository: CommentRepository = mockk()
    val boardService : BoardService = mockk()
    val userService: UserService = mockk()

    val commnetService: CommentService = CommentService(commentRepository, boardService, userService)

    Given("댓글 작성 요청이 유효한 요청이면"){
        val content = "댓글1"
        val request = createCommentRequest(content = content)

        every { commentRepository.save(any()) } returns createComment(content = content)
        every{ userService.findUserByEmail(any()) } returns createUser()
        every { boardService.findByBoardId(any()) } returns createBoard()

        When("저장하면"){
            val actual = commnetService.save(request)
            Then("성공한다."){
                actual.content shouldBe content
            }

        }
    }


})