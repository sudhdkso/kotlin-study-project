package com.study.boardproject.viewCount.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "viewCountHistory")
class ViewCountHistory(postId : Long, date:LocalDate, viewCountIncrease: Long) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val postId: Long = postId

    val date: LocalDate = date

    val viewCountIncrease: Long = viewCountIncrease
}