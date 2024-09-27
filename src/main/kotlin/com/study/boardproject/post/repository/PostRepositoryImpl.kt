package com.study.boardproject.post.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.boardproject.post.entity.Post
import com.study.boardproject.post.entity.QPost
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

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
