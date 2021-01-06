package com.bearddr.newsapp.data.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class NewsDto(
  @Json(name = "source")
  val source: SourceDto?,

  @Json(name = "author")
  val author: String?,

  @Json(name = "title")
  val title: String?,

  @Json(name = "description")
  val description: String?,

  @Json(name = "url")
  val articleUrl: String?,

  @Json(name = "urlToImage")
  val imageUrl: String?,

  @Json(name = "publishedAt")
  val publishedAt: LocalDateTime?,

  @Json(name = "content")
  val content: String?

)