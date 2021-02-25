package com.example.newsfetch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsfetch.R
import com.example.newsfetch.db.ArticleDataBase
import com.example.newsfetch.repo.NewsRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsrepo = NewsRepository(ArticleDataBase(this))
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(application,newsrepo)
        viewModel = ViewModelProvider(this,newsViewModelProviderFactory).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(newsNavHostFragmet.findNavController())
    }
}