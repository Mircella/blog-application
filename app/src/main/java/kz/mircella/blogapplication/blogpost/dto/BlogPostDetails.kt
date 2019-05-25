package kz.mircella.blogapplication.blogpost.dto

data class BlogPostDetails(val title: String,
                           val content: String,
                           val authorId: String,
                           val createdAt: String,
                           val imageIds: List<String>)