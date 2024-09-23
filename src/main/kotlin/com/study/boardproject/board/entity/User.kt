package com.study.boardproject.board.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.study.boardproject.board.entity.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
class User(email: String, name: String, password:String, phoneNumber: String? = null, role: Role = Role.USER) : UserDetails {
    @Id
    @GeneratedValue
    val id: Long? = null

    @Column(nullable = false, updatable = false)
    val email = email

    @Column(nullable = false)
    var name = name
        protected set

    @Column(nullable = false)
    private var password = password

    @Enumerated(EnumType.STRING)
    var role: Role = role

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

    override fun getAuthorities(): MutableCollection<GrantedAuthority>? {
        return mutableListOf(SimpleGrantedAuthority(role.type))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}

