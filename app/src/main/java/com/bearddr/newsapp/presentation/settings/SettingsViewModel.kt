package com.bearddr.newsapp.presentation.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.bearddr.newsapp.presentation.base.ReduxViewModel
import com.bearddr.newsapp.presentation.models.CategoryUiModel
import com.bearddr.newsapp.usecases.settings.CategoriesUseCase
import com.bearddr.newsapp.usecases.settings.result.GetCategoriesResult
import com.bearddr.newsapp.usecases.settings.result.SaveCategoriesResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsViewModel @ViewModelInject constructor(
  private val useCase: CategoriesUseCase
) : ReduxViewModel<SettingsViewState>(SettingsViewState()) {

  private val events = ConflatedBroadcastChannel<UiEvent>()

  init {
    handleSaveEvent()
    handleGetCategoriesEvent()
  }

  private fun handleSaveEvent() {
    events.asFlow()
      .filterIsInstance<UiEvent.SaveEvent>()
      .flatMapLatest {
        useCase.saveCategories(
          currentState().newCategories.map { it.title }
            .zip(currentState().newCategories.map { it.checked })
            .toMap()
        )
      }
      .onEach {
        when (it) {
          SaveCategoriesResult.Loading -> {
            Timber.tag("AppDebug").d("Save categories - Loading")
          }

          SaveCategoriesResult.Success -> {
            Timber.tag("AppDebug").d("Save categories - Success")
            val categories = mutableListOf<CategoryUiModel>()
            currentState().newCategories.forEach {
              categories.add(it)
            }

            setState {
              copy(
                categories = categories,
                uiAction = UiAction.ShowCategories
              )
            }
          }

          is SaveCategoriesResult.Failure -> {
            Timber.tag("AppDebug").d("Save categories - Failure - ${it.message}")
            setState {
              copy(
                message = it.message,
                uiAction = UiAction.ShowCategories
              )
            }
          }
        }
      }
      .flowOn(Dispatchers.IO)
      .launchIn(viewModelScope)
  }

  private fun handleGetCategoriesEvent() {
    events.asFlow()
      .filterIsInstance<UiEvent.GetCategories>()
      .flatMapLatest {
        useCase.getCategories()
      }
      .onEach {
        when (it) {
          GetCategoriesResult.Loading -> {
            Timber.tag("AppDebug").d("Get categories - Loading")
          }

          is GetCategoriesResult.Success -> {
            Timber.tag("AppDebug").d("Get categories - Success - ${it.categories}")
            setState {
              copy(
                categories = it.categories,
                newCategories = it.categories,
                uiAction = UiAction.ShowCategories
              )
            }
          }

          is GetCategoriesResult.Failure -> {
            Timber.tag("AppDebug").d("Get categories - Failure - ${it.message}")
          }
        }
      }
      .flowOn(Dispatchers.IO)
      .launchIn(viewModelScope)
  }

  fun saveEvent() {
    events.offer(UiEvent.SaveEvent)
  }

  fun getCategoriesEvent() {
    events.offer(UiEvent.GetCategories)
  }

  fun categoryCheckToggle(category: String, isChecked: Boolean) {
    viewModelScope.launchSetState {
      val categories = mutableListOf<CategoryUiModel>()
      currentState().newCategories.forEach {
        if (it.title == category) {
          categories.add(CategoryUiModel(category, isChecked))
        } else {
          categories.add(CategoryUiModel(it.title, it.checked))
        }
      }

      copy(
        newCategories = categories
      )
    }
  }

}