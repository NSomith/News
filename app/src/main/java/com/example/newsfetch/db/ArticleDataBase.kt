package com.example.newsfetch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsfetch.models.Article

@Database(entities = [Article::class],version = 1)

@TypeConverters(Convters::class)
abstract class ArticleDataBase : RoomDatabase() {

    abstract fun getArticleDao():ArticleDao

    companion object{

        private var instance:ArticleDataBase? = null
        private val Lock =Any()

        operator fun invoke(context: Context) = instance?: synchronized(Lock){
            instance?: createDataBase(context).also{
                instance = it
            }
        }

        private fun createDataBase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDataBase::class.java,
                        "articles.db"
                ).build()
    }
}