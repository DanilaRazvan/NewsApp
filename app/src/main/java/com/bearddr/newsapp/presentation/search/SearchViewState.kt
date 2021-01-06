package com.bearddr.newsapp.presentation.search

import com.bearddr.newsapp.presentation.models.NewsUiModel

data class SearchViewState(
  val news: List<NewsUiModel> = emptyList(),
  val message: String = "",
  val uiAction: UiAction = UiAction.GlobalIdle
)

sealed class UiAction {
  object GlobalIdle: UiAction()

  sealed class NewsDate : UiAction() {
    object Idle: UiAction()

    object Loading: UiAction()

    object ShowMessage: UiAction()

    object DisplayNews: UiAction()
  }

  sealed class NewsRelevancy : UiAction() {
    object Idle: UiAction()

    object Loading: UiAction()

    object ShowMessage: UiAction()

    object DisplayNews: UiAction()
  }

  sealed class NewsPopularity : UiAction() {
    object Idle: UiAction()

    object Loading: UiAction()

    object ShowMessage: UiAction()

    object DisplayNews: UiAction()
  }
}

sealed class UiEvents {
  data class SearchEvent(
    val query: String
  ) : UiEvents()
}