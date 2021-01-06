package com.bearddr.newsapp.usecases.search.result

import com.bearddr.newsapp.presentation.models.NewsUiModel

sealed class SearchNewsResult : SearchResult {

  sealed class DateSorted : SearchNewsResult() {
    object Loading : SearchNewsResult()

    data class Success(
      val news: List<NewsUiModel>,
    ) : SearchNewsResult()

    data class Failure(
      val code: Int,
      val message: String
    ) : SearchNewsResult()

    data class Error400(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error401(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error403(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error500(
      val message: String = ""
    ) : SearchNewsResult()
  }


  sealed class RelevancySorted : SearchNewsResult() {
    object Loading : SearchNewsResult()

    data class Success(
      val news: List<NewsUiModel>,
    ) : SearchNewsResult()

    data class Failure(
      val code: Int,
      val message: String
    ) : SearchNewsResult()

    data class Error400(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error401(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error403(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error500(
      val message: String = ""
    ) : SearchNewsResult()
  }


  sealed class PopularitySorted : SearchNewsResult() {
    object Loading : SearchNewsResult()

    data class Success(
      val news: List<NewsUiModel>,
    ) : SearchNewsResult()

    data class Failure(
      val code: Int,
      val message: String
    ) : SearchNewsResult()

    data class Error400(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error401(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error403(
      val message: String = ""
    ) : SearchNewsResult()

    data class Error500(
      val message: String = ""
    ) : SearchNewsResult()
  }
}