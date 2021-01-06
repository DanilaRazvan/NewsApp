package com.bearddr.newsapp.presentation.settings

import com.bearddr.newsapp.presentation.models.CategoryUiModel

data class SettingsViewState(
  val categories: List<CategoryUiModel> = emptyList(),
  val newCategories: List<CategoryUiModel> = emptyList(),
  val message: String = "",
  val uiAction: UiAction = UiAction.Idle
)

sealed class UiAction {
  object Idle: UiAction()

  object ShowMessage: UiAction()

  object ShowCategories: UiAction()

  object Loading: UiAction()
}

sealed class UiEvent {
  object GetCategories: UiEvent()

  object SaveEvent : UiEvent()
}