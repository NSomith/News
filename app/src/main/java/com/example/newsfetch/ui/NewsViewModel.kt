package com.example.newsfetch.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfetch.NewsApplication
import com.example.newsfetch.models.Article
import com.example.newsfetch.models.NewsResponse
import com.example.newsfetch.repo.NewsRepository
import com.example.newsfetch.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app:Application,val newsRepository:NewsRepository) :AndroidViewModel(app) {
    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingnewsPage = 1
    var breakingNewsResonse:NewsResponse? = null

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchnewsPage = 1
    var searchNewsResonse:NewsResponse? = null

    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        safeBreakingNews(countryCode)
    }

    fun getSearchNews(countryCode: String) = viewModelScope.launch {
        safeSearchNews(countryCode)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun savedNews() = newsRepository.getsavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultresponse->
                breakingnewsPage++
                if(breakingNewsResonse == null){
                    breakingNewsResonse = resultresponse
                }else{
                    val oldarticle = breakingNewsResonse?.articles
                    val newArticle = resultresponse.articles
                    oldarticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResonse?:resultresponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNews(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response =  newsRepository.getBreakingNews(countryCode,breakingnewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("NO INTERNET CONNECTION"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("COnversion Error"))
            }
        }
    }

    private suspend fun safeSearchNews(countryCode: String){
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.getsearchNews(countryCode,searchnewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("NO INTERNET CONNECTION"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("COnversion Error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
//  connectivity mannager is used to check the internet connection
        val connitivityManger = getApplication<NewsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connitivityManger.activeNetwork ?:return false
            val capabilities = connitivityManger.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->true
                else -> false
            }
        }else{
            connitivityManger.activeNetworkInfo.run {
                return when(this?.type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET ->true
                    else ->false
                }
            }
        }
        return false
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultresponse->
                searchnewsPage++ //meas that for the first time the api sen 20 pages and after loading it then again sends 20 extra
                if(searchNewsResonse == null){
                    searchNewsResonse = resultresponse
                }else{
//                    val oldarticle = searchNewsResonse?.articles
                    val newArticle = resultresponse.articles
//                    oldarticle?.addAll(newArticle)
                    searchNewsResonse!!.articles.addAll(newArticle) //can do like this also
                }
                return Resource.Success(searchNewsResonse?:resultresponse)
            }
        }
        return Resource.Error(response.message())
    }

}