package com.study.boardproject.board

import com.study.boardproject.board.dto.PostRequestDto
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.entity.User

private val TITLE: String = "제목"
private val CONTENT: String = "내용"
private val WRITER: User = createUser()

fun createBoard(title:String = TITLE, content:String = CONTENT, writer:User = WRITER)
: Post = Post(title,content, writer)

fun createBoardRequest(title:String = TITLE, content:String = CONTENT, email:String= EMAIL)
: PostRequestDto = PostRequestDto(title, content, email)