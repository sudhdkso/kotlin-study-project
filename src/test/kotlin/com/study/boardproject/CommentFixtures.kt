package com.study.boardproject

import com.study.boardproject.comment.dto.CommentCreateRequestDto
import com.study.boardproject.comment.dto.CommentRequestDto
import com.study.boardproject.comment.entity.Comment
import com.study.boardproject.post.entity.Post
import com.study.boardproject.user.entity.User

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
