package com.study.boardproject.board.service

import com.study.boardproject.board.createBoard
import com.study.boardproject.board.createBoardRequest
import com.study.boardproject.board.createUser
import com.study.boardproject.board.repository.BoardRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.validation.Validator
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@ExtendWith(SpringExtension::class)
@SpringBootTest
class BoardServiceTest : BehaviorSpec({

    val boardRepository : BoardRepository = mockk()
    val userService: UserService = mockk()
    val boardService: BoardService = BoardService(boardRepository, userService)

    val validator: Validator = LocalValidatorFactoryBean().apply { afterPropertiesSet() }

    Given("사용자와 게시글이 모두 유효한 경우"){
        val title = "테스트1"
        val request = createBoardRequest()
        every { userService.findUserByEmail(any()) } returns createUser()
        every { boardRepository.save(any()) } returns createBoard(title = title)
        When("저장하면"){
            val actual = boardService.save(request)
            Then("성공한다."){
                actual.title shouldBe title
            }
        }
    }

    Given("게시글 내용이 유효하지 않는 경우"){
        every { userService.findUserByEmail(any()) } returns createUser()
        When("제목을 빈값으로 저장하면"){
            val title = ""
            val request = createBoardRequest(title = title)
            Then("예외를 반환한다."){
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }
            }
        }
        When("내용을 빈값으로 저장하면"){
            val content= ""
            val request = createBoardRequest(content = content)
            Then("예외를 반환한다."){
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }
            }
        }
    }
})