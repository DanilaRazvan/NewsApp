package com.bearddr.newsapp.data

import com.bearddr.newsapp.data.network.responses.SearchNewsResponse
import com.bearddr.newsapp.data.network.responses.TopHeadlinesResponse

interface NewsRepository {
  suspend fun getTopHeadlines2(filters: Map<String, String>): TopHeadlinesResponse

  suspend fun getTopHeadlines(filters: List<String>): TopHeadlinesResponse

  suspend fun searchNews(filters: Map<String, String>) : SearchNewsResponse
}