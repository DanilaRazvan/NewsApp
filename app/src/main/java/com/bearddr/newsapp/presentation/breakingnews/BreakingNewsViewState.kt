package com.bearddr.newsapp.presentation.breakingnews

import com.bearddr.newsapp.presentation.models.NewsUiModel

data class BreakingNewsViewState(
  val feeds: List<Pair<NewsUiModel.Header, NewsUiModel.Details>> = emptyList(),
  val message: String = "",
  val uiAction: UiAction = UiAction.Idle
)

sealed class UiAction {
  object Idle: UiAction()

  object Loading: UiAction()

  object ShowMessage: UiAction()

  object DisplayNews: UiAction()
}

sealed class UiEvents {
  object FetchBreakingNewsEvent : UiEvents()

  data class NewsClicked(
    val position: Int
  )
}