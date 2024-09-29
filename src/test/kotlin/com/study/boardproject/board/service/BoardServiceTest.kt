package com.study.boardproject.board.service

import com.study.boardproject.board.repository.BoardRepository
import com.study.boardproject.createBoard
import com.study.boardproject.createBoardRequest
import com.study.boardproject.createBoardUpdateRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class BoardServiceTest : BehaviorSpec({
    val boardRepository: BoardRepository = mockk(relaxed = true)
    val boardService: BoardService = BoardService(boardRepository)

    Given("요청하는 정보가 모두 유효할 때") {
        val title = "게시판1"
        val request = createBoardRequest(title = title)

        every { boardRepository.existsByTitle(any()) } returns false
        every { boardRepository.save(any()) } returns request.toEntity()

        When("게시판을 생성하려고 하면") {

            val result = boardService.save(request)
            Then("성공한다.") {
                result.title shouldBe title
            }
        }
    }


    Given("요청의 모든 정보가 유효할 때") {
        val description = "설명설명서"
        val minReadLevel = 1
        val minwriteLevel = 1
        val request = createBoardUpdateRequest(description = description, minReadLevel = minReadLevel, minWriteLevel = minwriteLevel)
        val board = createBoard()

        every { boardService.findByBoardId(any()) } returns board


        When("게시판을 수정하려고 하면") {

            val result = boardService.update(request)
            Then("성공한다.") {
                result.description shouldBe request.description
                result.minReadLevel shouldBe request.minReadLevel
                result.minWriteLevel shouldBe request.minWriteLevel
            }
        }
    }

    Given("설명 정보가 제한된 글자수를 초과할 때") {
        val description = "b".repeat(101)

        When("게시판을 생성하려고 하면") {
            val request = createBoardRequest(description = description)
            Then("예외가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }.message shouldBe "게시판 설명이 100자를 초과하였습니다."
            }
        }

        When("게시판을 업데이트 하려고 하면") {
            val request = createBoardUpdateRequest(description = description)
            Then("예외가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    boardService.update(request)
                }.message shouldBe "게시판 설명이 100자를 초과하였습니다."
            }
        }
    }

    Given("제목 정보가 제한된 글자수를 초과할 때") {
        val title = "a".repeat(22)
        val request = createBoardRequest(title = title)

        When("게시판을 생성하려고 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }.message shouldBe "게시판 이름이 20자를 초과하였습니다."
            }
        }
    }

    Given("게시판의 이름이 중복되었을 때"){
        val request = createBoardRequest()
        every{boardRepository.existsByTitle(any())} returns true
        When("게시판을 생성하려고 하면"){
            Then("예외가 발생한다."){
                shouldThrow<IllegalArgumentException> {
                    boardService.save(request)
                }.message shouldBe "${request.title}과 동일한 이름의 게시판이 존재합니다."
            }
        }
    }

})