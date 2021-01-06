package com.bearddr.newsapp.presentation.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.bearddr.newsapp.presentation.base.ReduxViewModel
import com.bearddr.newsapp.usecases.search.SearchNewsUseCase
import com.bearddr.newsapp.usecases.search.result.SearchNewsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(
  private val searchNewsUseCase: SearchNewsUseCase
) : ReduxViewModel<SearchViewState>(SearchViewState()) {

  private val events = ConflatedBroadcastChannel<UiEvents>()

  init {
    handleSearchEvent()
  }

  private fun handleSearchEvent() {
    events.asFlow()
      .filterIsInstance<UiEvents.SearchEvent>()
      .flatMapLatest {
        searchNewsUseCase.searchNews(it.query)
      }
      .onEach {
        when (it) {
          SearchNewsResult.DateSorted.Loading -> {
            setState {
              copy(
                uiAction = UiAction.NewsDate.Loading
              )
            }
          }

          is SearchNewsResult.DateSorted.Success -> {
            setState {
              copy(
                news = it.news,
                uiAction = UiAction.NewsDate.DisplayNews
              )
            }
          }

          is SearchNewsResult.DateSorted.Failure -> {
            setState {
              copy(
                message = "${it.message} - ${it.code}",
                uiAction = UiAction.NewsDate.ShowMessage
              )
            }
          }


          SearchNewsResult.PopularitySorted.Loading -> {
            setState {
              copy(
                uiAction = UiAction.NewsPopularity.Loading
              )
            }
          }

          is SearchNewsResult.PopularitySorted.Success -> {
            setState {
              copy(
                news = it.news,
                uiAction = UiAction.NewsPopularity.DisplayNews
              )
            }
          }

          is SearchNewsResult.PopularitySorted.Failure -> {
            setState {
              copy(
                message = "${it.message} - ${it.code}",
                uiAction = UiAction.NewsPopularity.ShowMessage
              )
            }
          }


          SearchNewsResult.RelevancySorted.Loading -> {
            setState {
              copy(
                uiAction = UiAction.NewsRelevancy.Loading
              )
            }
          }

          is SearchNewsResult.RelevancySorted.Success -> {
            setState {
              copy(
                news = it.news,
                uiAction = UiAction.NewsRelevancy.DisplayNews
              )
            }
          }

          is SearchNewsResult.RelevancySorted.Failure -> {
            setState {
              copy(
                message = "${it.message} - ${it.code}",
                uiAction = UiAction.NewsRelevancy.ShowMessage
              )
            }
          }
        }
      }
      .flowOn(Dispatchers.IO)
      .launchIn(viewModelScope)
  }

  fun searchNews(query: String) {
    events.offer(UiEvents.SearchEvent(query))
  }
}