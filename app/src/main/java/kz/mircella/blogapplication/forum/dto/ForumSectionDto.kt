package kz.mircella.blogapplication.forum.dto

data class ForumSectionDto(val title: String,
                           val createdAt: String,
                           val authorId: String,
                           val forumTopics:  List<ForumTopicDetails>)

data class ForumTopicDetails(val title: String,
                             val lastTopicPostDateTime: String,
                             val lastTopicPostAuthorId: String)
