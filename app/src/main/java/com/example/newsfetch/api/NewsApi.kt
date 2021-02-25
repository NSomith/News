package com.example.newsfetch.api

import com.example.newsfetch.models.NewsResponse
import com.example.newsfetch.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
            @Query("country")
            countryCode:String = "in",
            @Query("page")
            pageNumber:Int = 1,
            @Query("apiKey")
            apiKey:String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
            @Query("q")
            countryCode:String,
            @Query("page")
            pageNumber:Int = 1,
            @Query("apiKey")
            apiKey:String = API_KEY
    ): Response<NewsResponse>
}