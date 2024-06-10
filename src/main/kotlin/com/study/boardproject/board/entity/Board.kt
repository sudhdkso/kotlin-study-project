package com.study.boardproject.board.entity

import com.study.boardproject.board.dto.request.BoardRequestDto
import jakarta.persistence.*

@Entity
@Table(name = "board")
class Board(title: String, content: String, writer: User) : BaseEntity() {
    @Id
    @GeneratedValue
    val id: Long? = null

    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val writer: User? = writer

    @Column(nullable = false, length = 20)
    var title: String? = title

    @Column(nullable = false, length = Int.MAX_VALUE)
    var content: String? = content

    @Column
    var viewCount : Long = 0

    fun update(requestDto: BoardRequestDto) {
        this.title = requestDto.title
        this.content = requestDto.content
    }

    fun viewCountUp(){
        this.viewCount++
    }
}