package com.study.boardproject.board.entity

import com.study.boardproject.createBoard
import com.study.boardproject.createBoardUpdateRequest
import com.study.boardproject.createPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.spyk

class BoardTest : FunSpec({

    context("게시판에 게시글을 추가하면") {
        val board = createBoard()
        val post = createPost()

        board.addPost(post)

        test("게시판의 posts의 사이즈가 1 증가한다.") {
            board.posts.size shouldBe 1
        }
        test("게시글의 게시판이 해당 게시판으로 설정된다.") {
            post.board shouldBe board
        }
    }

    context("유효한 값을 가진 게시판이 있을 때, 게시판의 일부 값을 수정하면") {
        val description = "설명설명"
        val minReadLevel = 1
        val minWriteLevel = 3


        test("모두 값을 변경하려하면 모두 변경된다.") {
            val board = createBoard()

            val requestDto = createBoardUpdateRequest(
                description = description,
                minReadLevel = minReadLevel,
                minWriteLevel = minWriteLevel
            )

            board.update(requestDto)

            board.description shouldBe description
            board.minReadLevel shouldBe minReadLevel
            board.minWriteLevel shouldBe minWriteLevel
        }

        test("일부 값만 변경되어 요청이 들어오면 변경된 값만 수정된다."){
            val originDescription = "설명"
            val originMinReadLevel = 0
            val originMinWriteLevel = 0

            val board = createBoard(description = originDescription, minReadLevel = originMinReadLevel, minWriteLevel = originMinWriteLevel)

            val requestDto = createBoardUpdateRequest(
                description = description,
                minReadLevel = originMinReadLevel,
                minWriteLevel = originMinWriteLevel
            )

            board.update(requestDto)

            board.description shouldNotBe  originDescription
            board.minReadLevel shouldBe originMinReadLevel
            board.minWriteLevel shouldBe originMinWriteLevel
        }
    }

    context("게시판에 게시글을 삭제하면") {
        val board = spyk<Board>()
        val post = createPost()

        every { board.posts.remove(any()) } returns true

        board.removePost(post)

        test("게시글의 게시판이 null로 설정된다.") {
            post.board shouldBe null
        }
    }

    context("게시판의 최소 읽기 레벨을 설정하면") {
        val minReadLevel = 2
        val board = createBoard(minReadLevel = minReadLevel)

        test("canRead()에 여러 readLevel을 매개변수로 받아 올바른 boolean타입의 값을 return한다.") {
            forAll(
                row(0, false),
                row(1, false),
                row(2, true),
                row(3, true)
            ) { input, expected ->
                board.canRead(input) shouldBe expected
            }

        }
    }

    context("게시판의 최소 쓰기 레벨을 설정하면") {
        val minWriteLevel = 3
        val board = createBoard(minWriteLevel = minWriteLevel)

        test("canWrite()에 여러 writeLevel 매개변수로 받아 올바른 boolean타입의 값을 return한다.") {
            forAll(
                row(0, false),
                row(2, false),
                row(3, true),
                row(5, true)
            ) { input, expected ->
                board.canWrite(input) shouldBe expected
            }

        }
    }
})