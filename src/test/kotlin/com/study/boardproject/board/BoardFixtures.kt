package com.study.boardproject.board

import com.study.boardproject.board.dto.BoardRequestDto
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.User

private val TITLE: String = "제목"
private val CONTENT: String = "내용"
private val WRITER: User = createUser()

fun createBoard(title:String = TITLE, content:String = CONTENT, writer:User = WRITER)
: Board = Board(title,content, writer)

fun createBoardRequest(title:String = TITLE, content:String = CONTENT, email:String= EMAIL)
: BoardRequestDto = BoardRequestDto(title, content, email)