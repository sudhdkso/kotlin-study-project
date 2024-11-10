package com.study.boardproject.post.repository

import com.study.boardproject.post.entity.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

fun PostRepository.getByPostId(id: Long) : Post {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}

interface PostRepository : JpaRepository<Post, Long>, PostRepositoryCustom {
    fun save(post: Post) : Post
    fun findByDeletedAtIsNull(pageable: Pageable) : List<Post>

    @Query("SELECT b FROM Post b WHERE b.createdAt <= :nineDaysAgo AND b.createdAt > :tenDaysAgo")
    fun findWithEditPeriodImminent(nineDaysAgo: LocalDateTime, tenDaysAgo: LocalDateTime): List<Post>

    // 주간 조회수 TOP 5
    @Query("SELECT p FROM Post p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.viewCount DESC")
    fun findTopPostsByViewCount(@Param("startDate") startDate: LocalDateTime, @Param("endDate") endDate: LocalDateTime): List<Post>

    // 월간 조회수 TOP 5
    @Query("SELECT p FROM Post p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.viewCount DESC")
    fun findTopPostsByViewCountForMonth(@Param("startDate") startDate: LocalDateTime, @Param("endDate") endDate: LocalDateTime): List<Post>
}