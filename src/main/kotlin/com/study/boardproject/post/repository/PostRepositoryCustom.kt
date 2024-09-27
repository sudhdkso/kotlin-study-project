package com.study.boardproject.post.repository

import com.study.boardproject.post.entity.Post

fun interface PostRepositoryCustom {
    fun searchByTitleOrContent(searchQuery: String): List<Post>
}
