package com.example.newsfetch.repo

import com.example.newsfetch.api.RetrofitInstance
import com.example.newsfetch.db.ArticleDataBase
import com.example.newsfetch.models.Article

class NewsRepository(val db:ArticleDataBase) {

    suspend fun getBreakingNews(countrycode:String,pageNumber:Int) =
            RetrofitInstance.api.getBreakingNews(countrycode,pageNumber)

    suspend fun getsearchNews(countrycode: String,pageNumber: Int) =
            RetrofitInstance.api.searchForNews(countrycode,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getsavedNews()  = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}