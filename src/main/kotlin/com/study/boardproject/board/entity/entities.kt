package com.study.boardproject.board.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity{
    @CreatedDate
    @get:Column(nullable = false, updatable = false)
    open var createdAt: LocalDateTime? = LocalDateTime.MIN
        protected set
    @LastModifiedDate
    @get:Column(nullable = false)
    open var modifiedAt: LocalDateTime? = LocalDateTime.MIN
        protected set

}