package com.study.boardproject.viewCount.repository

import com.study.boardproject.post.entity.Post
import com.study.boardproject.viewCount.entity.ViewCountHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ViewCountHistoryRepository : JpaRepository<ViewCountHistory, Long> {
    fun findByPostIdAndDate(postId: Long, date: LocalDate): ViewCountHistory?

    @Query("""
        SELECT p FROM Post p
        JOIN ViewCountHistory vch ON p.id = vch.postId
        WHERE vch.date BETWEEN :startDate AND :endDate
        GROUP BY p.id
        ORDER BY SUM(vch.viewCountIncrease) DESC
    """)
    fun findTopPostsByViewCountIncrease(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        pageable: Pageable
    ): Page<Post>
}