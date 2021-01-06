package com.bearddr.newsapp.data.network

import com.bearddr.newsapp.data.network.responses.SearchNewsResponse
import com.bearddr.newsapp.data.network.responses.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface NewsApi {

  @GET("top-headlines")
  suspend fun getTopHeadlines(
    @Header("X-Api-Key") apiKey: String,
    @Query("category") filters: List<String>,
    @Query("country") country: String = "ro"
  ): TopHeadlinesResponse

  @GET("top-headlines")
  suspend fun getTopHeadlines2(
    @Header("X-Api-Key") apiKey: String,
    @QueryMap filters: Map<String, String>
  ): TopHeadlinesResponse

  @GET("everything")
  suspend fun searchNews(
    @Header("X-Api-Key") apiKey: String,
    @QueryMap filters: Map<String, String>
  ): SearchNewsResponse

}