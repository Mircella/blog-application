package kz.mircella.blogapplication.blogpost.dto

data class BlogPostDto(val title: String,
                       val createdAt: String,
                       val authorId: String,
                       val imageIds: List<String>)