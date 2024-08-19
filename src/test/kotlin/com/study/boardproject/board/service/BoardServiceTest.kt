package com.study.boardproject.board.service

import com.study.boardproject.board.createBoard
import com.study.boardproject.board.createBoardRequest
import com.study.boardproject.board.createUser
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.repository.BoardRepository
import com.study.boardproject.board.repository.getByBoardId
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
class BoardServiceTest : BehaviorSpec({

    val boardRepository: BoardRepository = mockk(relaxed = true)
    val userService: UserService = mockk()
    val notificationService: NotificationService = mockk()
    val boardService = BoardService(boardRepository, userService, notificationService)

    Given("사용자와 게시글이 모두 유효한 경우") {
        val title = "테스트1"
        val request = createBoardRequest()

        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardRepository.save(any()) } returns createBoard(title = title)

        When("저장하면") {
            val actual = boardService.save(request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }
    }

    Given("게시글 내용이 유효하지 않는 경우") {
        every { userService.findUserByEmail(any()) } returns createUser()
        When("제목을 200자 초과로 작성하면") {
            val title = "a".repeat(201)
            val request = createBoardRequest(title)
            Then("예외를 반환한다."){
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }
            }
        }
        When("내용을 1000자 초과로 작성하면") {
            val content = "b".repeat(10001)
            val request = createBoardRequest(content = content)
            Then("예외를 반환한다."){
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }
            }
        }
        When("제목을 빈값으로 저장하면") {
            val title = ""
            val request = createBoardRequest(title = title)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }
            }
        }

        When("내용을 빈값으로 저장하면") {
            val content = ""
            val request = createBoardRequest(content = content)
            Then("예외를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }
            }
        }
    }

    Given("게시글이 유효한 경우") {

        val title = "수정"
        val request = createBoardRequest(title = title)
        val board = spyk<Board>(createBoard())
        val boardId = 1L

        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardService.findByBoardId(any()) } returns board

        every { boardRepository.save(any()) } returns createBoard(title = title)
        every { boardRepository.delete(any()) } just runs

        every { board.canEditBoard() } returns true
        When("수정하면") {
            val actual = boardService.update(boardId, request)
            Then("성공한다.") {
                actual.title shouldBe title
            }
        }

        When("삭제하면") {
            boardService.deleteByBoardId(boardId)
            Then("성공한다.") {
                board.deletedAt shouldNotBe null
                
            }
        }
    }

    Given("존재하지 않는 게시글을") {
        val title = "수정"
        val boardId = 1L
        val request = createBoardRequest(title = title)
        every { boardRepository.getByBoardId(any()) } throws NoSuchElementException()
        When("조회하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    boardService.findByBoardId(boardId)
                }
            }
        }
        When("수정하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    boardService.update(boardId, request)
                }
            }
        }
        When("삭제하려고 하면") {
            Then("예외를 반환한다.") {
                shouldThrow<NoSuchElementException> {
                    boardService.deleteByBoardId(boardId)
                }
            }
        }
    }

    Given("10일이 지난 게시글을") {
        val title = "수정"
        val request = createBoardRequest(title = title)
        val board = spyk<Board>(createBoard())
        val boardId = 1L

        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardService.findByBoardId(any()) } returns board

        every { board.canEditBoard() } returns false

        When("수정하려고 하면") {
            Then("예외가 발생한다."){
                shouldThrow<IllegalArgumentException> {
                    boardService.update(boardId, request)
                }
            }
        }

    }
})