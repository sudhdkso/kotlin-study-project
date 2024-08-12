package com.study.boardproject.board.service

import com.study.boardproject.board.createBoard
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
class BoardSechedulerTest() : BehaviorSpec({

    Given("작성한지 9일된 게시글은") {
        val board = createBoard()
        val notificationService: NotificationService =  mockk(relaxed = true)
        val boardService: BoardService = spyk<BoardService>(BoardService( mockk(), mockk(), notificationService))
        val boardSecheduler: BoardScheduler = BoardScheduler(boardService)

        every { boardService.findBoardWithEditDedlineSoon() } returns listOf(board)

        When("스케쥴러를 통해서"){
            boardSecheduler.notifyEditDeadline()
            Then("알림 전송이 호출된다."){
               verify { notificationService.sendEditDeadlineNotification(board) }
            }
        }
    }
}) 