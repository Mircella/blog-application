package kz.mircella.blogapplication.blogpost.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BlogPostDao {

    @get:Query("SELECT * FROM BlogPost")
    val all: List<BlogPost>

    @Insert
    fun insertAll(vararg blogPosts: BlogPost)
}