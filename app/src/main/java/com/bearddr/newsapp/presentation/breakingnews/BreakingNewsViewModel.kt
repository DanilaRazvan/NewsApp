package com.bearddr.newsapp.presentation.breakingnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.bearddr.newsapp.presentation.base.ReduxViewModel
import com.bearddr.newsapp.presentation.models.NewsUiModel
import com.bearddr.newsapp.usecases.breakingnews.FetchBreakingNewsUseCase
import com.bearddr.newsapp.usecases.breakingnews.result.FetchBreakingNewsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class BreakingNewsViewModel @ViewModelInject constructor(
  private val fetchBreakingNewsUseCase: FetchBreakingNewsUseCase
): ReduxViewModel<BreakingNewsViewState>(BreakingNewsViewState()) {

  private val events = ConflatedBroadcastChannel<UiEvents>()

  init {
    handleFetchNewsEvent()
  }

  private fun handleFetchNewsEvent() {
    events.asFlow()
      .filterIsInstance<UiEvents.FetchBreakingNewsEvent>()
      .flatMapLatest {
        fetchBreakingNewsUseCase.fetchBreakingNews()
      }
      .onEach {
        when(it) {
          FetchBreakingNewsResult.Loading -> {
            setState {
              copy(
                uiAction = UiAction.Loading
              )
            }
          }

          is FetchBreakingNewsResult.Success -> {
            setState {
              copy(
                feeds = it.news,
                uiAction = UiAction.DisplayNews
              )
            }
          }

          is FetchBreakingNewsResult.Failure -> {
            setState {
              copy(
                message = "${it.message} - ${it.code}",
                uiAction = UiAction.ShowMessage
              )
            }
          }
        }
      }
      .flowOn(Dispatchers.IO)
      .launchIn(viewModelScope)
  }

  fun fetchBreakingNewsEvent() {
    events.offer(UiEvents.FetchBreakingNewsEvent)
  }

  fun expandItem(position: Int) {

    viewModelScope.launchSetState {
      val newFeeds = mutableListOf<Pair<NewsUiModel.Header, NewsUiModel.Details>>()
      currentState().feeds.forEachIndexed { index, pair ->
        if (index == position)
          newFeeds.add(pair.copy(first = pair.first.copy(expanded = !pair.first.expanded)))
        else
          newFeeds.add(pair)
      }

      copy(
        feeds = newFeeds,
        uiAction = UiAction.DisplayNews,
        message = "a"
      )
    }
  }
}