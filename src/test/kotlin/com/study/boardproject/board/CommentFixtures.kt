package com.study.boardproject.board

import com.study.boardproject.board.dto.CommentRequestDto
import com.study.boardproject.board.entity.Board
import com.study.boardproject.board.entity.Comment
import com.study.boardproject.board.entity.User

private val CONTENT = "댓글 내용"
private val DEPTH = 0
private val BOARD_ID = 1L;

private val WRITER: User = createUser()
private val BOARD: Board = createBoard()
fun createCommentRequest(content:String = CONTENT, depth:Int = DEPTH, boardId:Long = BOARD_ID) : CommentRequestDto
 = CommentRequestDto(content, depth, boardId, EMAIL)

fun createComment(content:String = CONTENT, depth:Int = DEPTH, board:Board = BOARD, writer:User = WRITER) : Comment
= Comment(content, depth, board, writer)
