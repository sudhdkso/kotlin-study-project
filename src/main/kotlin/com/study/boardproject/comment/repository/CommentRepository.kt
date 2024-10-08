package com.study.boardproject.comment.repository

import com.study.boardproject.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.findByIdOrNull

fun CommentRepository.getByCommentId(id: Long) : Comment {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}
interface CommentRepository : JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
}