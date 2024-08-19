package com.study.boardproject.board.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.boardproject.board.entity.Post
import com.study.boardproject.board.entity.QPost
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

fun interface PostRepositoryCustom {
    fun searchByTitleOrContent(searchQuery: String): List<Post>
}

@Repository
class PostRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Post::class.java), PostRepositoryCustom {
    override fun searchByTitleOrContent(searchQuery: String): List<Post> {
        val post = QPost.post
        return queryFactory.selectFrom(post)
            .where(post.title.containsIgnoreCase(searchQuery)
                .or(post.content.containsIgnoreCase(searchQuery))
                .and(post.deletedAt.isNull)
            )
            .orderBy(post.createdAt.desc())
            .fetch()
    }
}

fun PostRepository.getByPostId(id: Long) : Post {
    return findByIdOrNull(id) ?: throw NoSuchElementException()
}

interface PostRepository : JpaRepository<Post, Long>,PostRepositoryCustom{
    fun save(post: Post) : Post
    fun findByDeletedAtIsNull(pageable: Pageable) : List<Post>

    @Query("SELECT b FROM Post b WHERE b.createdAt <= :nineDaysAgo AND b.createdAt > :tenDaysAgo")
    fun findWithEditPeriodImminent(nineDaysAgo: LocalDateTime, tenDaysAgo: LocalDateTime): List<Post>
}