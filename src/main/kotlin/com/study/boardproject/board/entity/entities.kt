package com.study.boardproject.board.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.study.boardproject.board.dto.request.BoardRequestDto
import com.study.boardproject.board.dto.request.UserRequestDto
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
class User(email: String, name: String) {
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column(nullable = false, updatable = false)
    val email = email

    @Column(nullable = false)
    var name = name
        protected set

    //private var _boardList: MutableList<Board> = mutableListOf()

    @OneToMany(mappedBy = "writer")
    @JsonIgnore
    val boardList :MutableList<Board> = mutableListOf()

    constructor(requestDto: UserRequestDto) : this(requestDto.email, requestDto.name)

    fun updateUserName(name: String) {
        this.name = name
    }

}

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

    constructor (requestDto: BoardRequestDto, writer: User) : this(requestDto.title, requestDto.content, writer)

    fun update(requestDto: BoardRequestDto) {
        this.title = requestDto.title
        this.content = requestDto.content
    }

    fun viewCountUp(){
        this.viewCount++
    }
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity{
    @CreatedDate
    @get:Column(nullable = false, updatable = false)
    open var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @get:Column(nullable = false)
    open var modifiedAt: LocalDateTime? = null


}