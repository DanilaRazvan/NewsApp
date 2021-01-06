package com.bearddr.newsapp.data.network.responses

import com.bearddr.newsapp.data.network.dtos.NewsDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchNewsResponse(
  @Json(name = "status")
  val status: String?,

  @Json(name = "totalResults")
  val totalResults: Int?,

  @Json(name = "articles")
  val articles: List<NewsDto>?
)