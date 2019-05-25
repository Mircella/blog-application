package kz.mircella.blogapplication.video.dto

data class VideoDetails(val title: String,
                        val description: String,
                        val videoImageUrl: String,
                        val videoUrl: String,
                        val authorId: String,
                        val createdAt: String)