package com.study.boardproject.board.repository

import com.study.boardproject.board.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>