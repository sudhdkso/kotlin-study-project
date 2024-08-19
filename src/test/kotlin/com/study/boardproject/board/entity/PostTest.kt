package com.study.boardproject.board.entity

import com.study.boardproject.board.createPost
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk
import java.time.LocalDateTime

class PostTest: FunSpec({
    context("생성된 게시글의 조회수를 증가시키는 함수를 사용하면") {
        val post = createPost()
        val expected = post.viewCount+1

        post.viewCountUp()

        test("조회수가 1 증가한다."){
            post.viewCount shouldBeExactly expected
        }
    }

    context("방금 생성한 게시글은"){
        val post = spyk<Post>()

        every{post.createdAt} returns LocalDateTime.now()

        test("수정할 수 있다."){
            post.canEditPost() shouldBe true
        }
    }

    context("생성한지 10일 이상이 지난 게시글은"){
        val post = spyk<Post>()

        every{post.createdAt} returns LocalDateTime.now().plusDays(11)

        test("수정할 수 없다."){
            post.canEditPost() shouldBe false
        }
    }
}) {
}