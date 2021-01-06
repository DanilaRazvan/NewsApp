package com.bearddr.newsapp.usecases.breakingnews.result

import com.bearddr.newsapp.presentation.models.NewsUiModel

sealed class FetchBreakingNewsResult : BreakingNewsResult {
  object Loading : FetchBreakingNewsResult()

  data class Success(
    val news: List<Pair<NewsUiModel.Header, NewsUiModel.Details>>
  ) : FetchBreakingNewsResult()

  data class Failure(
    val code: Int,
    val message: String
  ) : FetchBreakingNewsResult()

  data class Error400(
    val message: String = ""
  ) : FetchBreakingNewsResult()

  data class Error401(
    val message: String = ""
  ) : FetchBreakingNewsResult()

  data class Error403(
    val message: String = ""
  ) : FetchBreakingNewsResult()

  data class Error500(
    val message: String = ""
  ) : FetchBreakingNewsResult()
}