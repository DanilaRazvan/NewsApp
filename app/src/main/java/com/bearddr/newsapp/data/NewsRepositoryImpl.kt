package com.bearddr.newsapp.data

import com.bearddr.newsapp.BuildConfig
import com.bearddr.newsapp.data.network.NewsApi
import com.bearddr.newsapp.data.network.responses.SearchNewsResponse
import com.bearddr.newsapp.data.network.responses.TopHeadlinesResponse
import timber.log.Timber
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
  private val newsApi: NewsApi
): NewsRepository {
  override suspend fun getTopHeadlines2(filters: Map<String, String>): TopHeadlinesResponse =
    newsApi.getTopHeadlines2(
      apiKey = BuildConfig.API_KEY,
      filters = filters
    )

  override suspend fun getTopHeadlines(filters: List<String>): TopHeadlinesResponse {
    return newsApi.getTopHeadlines(
      apiKey = BuildConfig.API_KEY,
      filters = filters
    )
  }

  override suspend fun searchNews(filters: Map<String, String>): SearchNewsResponse =
    newsApi.searchNews(
      apiKey = BuildConfig.API_KEY,
      filters = filters
    )
}