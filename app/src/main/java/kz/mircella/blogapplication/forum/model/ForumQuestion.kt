package kz.mircella.blogapplication.forum.model

data class ForumQuestion(val authorId: String, val authorLogin: String, val authorAvatarUrl: String, val postedAt: String, val question: String)