package com.study.boardproject.board.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

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

    fun updateUserName(name: String) {
        this.name = name
    }

}