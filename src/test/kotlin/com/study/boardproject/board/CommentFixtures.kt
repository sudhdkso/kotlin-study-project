package com.study.boardproject.board

import com.study.boardproject.board.dto.CommentCreateRequestDto
import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.entity.Comment
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.entity.User

private val CONTENT = "댓글 내용"
private val POST_ID = 1L

private val WRITER: User = createUser()
private val POST: Post = createPost()
fun createCommentCreateRequest(content: String = CONTENT, postId: Long = POST_ID): CommentCreateRequestDto =
    CommentCreateRequestDto(content, postId)

fun createComment(content: String = CONTENT, post: Post? = POST, writer: User = WRITER): Comment =
    Comment(content, post, writer)

fun createCommentRequest(content: String = CONTENT): CommentRequestDto =
    CommentRequestDto(content)
