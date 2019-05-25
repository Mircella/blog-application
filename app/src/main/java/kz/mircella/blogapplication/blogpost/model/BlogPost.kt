package kz.mircella.blogapplication.blogpost.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlogPost(@field:PrimaryKey
                    val id: Int,
                    val title: String,
                    val content: String,
                    val createdAt: String,
                    val authorId: String)