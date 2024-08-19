package com.study.boardproject.board.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class User(email: String, name: String, password:String? = null, phoneNumber: String? = null) {
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column(nullable = false, updatable = false)
    val email = email

    @Column(nullable = false)
    var name = name
        protected set

    @Column(nullable = false)
    var password = password
        protected set

    @Column(nullable = false)
    var phoneNumber = phoneNumber
        protected set

    @OneToMany(mappedBy = "writer")
    @JsonIgnore
    val boardList :MutableList<Post> = mutableListOf()

    fun update(name: String? = null, password: String? = null, phoneNumber: String? = null) {
        this.name = name ?: this.name
        this.password = password ?: this.password
        this.phoneNumber = phoneNumber ?: this.phoneNumber
    }

    constructor(email:String, name:String) : this(email, name, null, null)
}