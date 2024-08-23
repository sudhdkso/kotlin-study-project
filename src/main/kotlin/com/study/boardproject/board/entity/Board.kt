package com.study.boardproject.board.entity

import com.study.boardproject.board.dto.BoardUpdateRequestDto
import jakarta.persistence.*

@Entity
@Table(name = "board")
class Board(title: String, description: String, minReadLevel: Int = 0, minWriteLevel: Int = 0) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val title: String = title

    var description: String = description

    var minReadLevel = minReadLevel

    var minWriteLevel = minWriteLevel

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var posts : MutableList<Post> = mutableListOf()

    fun addPost(post: Post) {
        posts.add(post)
        post.board = this
    }

    fun removePost(post: Post) {
        posts.remove(post)
        post.board = null
    }

    fun update(requestDto: BoardUpdateRequestDto) {
        this.description = requestDto.description ?: this.description
        this.minReadLevel = requestDto.minReadLevel ?: this.minReadLevel
        this.minWriteLevel = requestDto.minWriteLevel ?: this.minWriteLevel
    }

    fun canRead(readLevel: Int): Boolean {
        return readLevel >= minReadLevel
    }

    fun canWrite(writeLevel: Int): Boolean {
        return writeLevel >= minWriteLevel
    }
}