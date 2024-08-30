package com.study.boardproject.board.entity

import com.study.boardproject.board.createBoard
import com.study.boardproject.board.createPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
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