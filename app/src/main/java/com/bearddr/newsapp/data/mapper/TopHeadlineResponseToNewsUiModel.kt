package com.bearddr.newsapp.data.mapper

import com.bearddr.newsapp.data.network.dtos.NewsDto
import com.bearddr.newsapp.presentation.models.NewsUiModel
import com.bearddr.newsapp.util.DateFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadlineResponseToNewsUiModel @Inject constructor(
  private val dateFormatter: DateFormatter
) {

  fun map(newsDto: NewsDto): Pair<NewsUiModel.Header, NewsUiModel.Details> {
    var content = newsDto.content ?: ""

    content = content.removeRange(content.lastIndexOf("["), content.length)

    return Pair(
      NewsUiModel.Header(
        title = newsDto.title ?: "",
        imageUrl = newsDto.imageUrl ?: "",
        articleUrl = newsDto.articleUrl ?: ""
      ),

      NewsUiModel.Details(
        publishedAt = newsDto.publishedAt?.let { dateFormatter.dateToSimpleString(it.toLocalDate()) }
          ?: "",
        source = newsDto.source?.name ?: "",
        content = content,
      )
    )
  }
}