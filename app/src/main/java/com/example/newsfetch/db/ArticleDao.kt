package com.example.newsfetch.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsfetch.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long //that is the id which is to be inserted or replced

    @Query("select * from articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}