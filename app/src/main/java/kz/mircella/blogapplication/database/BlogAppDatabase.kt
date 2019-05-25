package kz.mircella.blogapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.mircella.blogapplication.blogpost.model.BlogPost
import kz.mircella.blogapplication.blogpost.model.BlogPostDao

@Database(entities = [BlogPost::class], version = 1)
abstract class BlogAppDatabase: RoomDatabase() {
    abstract fun blogPostDao(): BlogPostDao
}