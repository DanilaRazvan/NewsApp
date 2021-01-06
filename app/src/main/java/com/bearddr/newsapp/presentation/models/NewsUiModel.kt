package com.bearddr.newsapp.presentation.models

sealed class NewsUiModel {

  data class Header(
    val title: String = "",
    val imageUrl: String = "",
    val articleUrl: String = "",
    val expanded: Boolean = false
  ) : NewsUiModel()

  data class Details(
    val publishedAt: String = "",
    val source: String = "",
    val content: String = "",
  ) : NewsUiModel()

  data class NewsSearchCategoryTitle(
    val categoryTitle: String,
  ) : NewsUiModel()

  data class ListOfNewsHeader(
    val list: List<Header> = emptyList()
  ) : NewsUiModel()
}