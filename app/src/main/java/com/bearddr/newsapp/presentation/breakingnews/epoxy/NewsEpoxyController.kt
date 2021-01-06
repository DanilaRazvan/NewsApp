package com.bearddr.newsapp.presentation.breakingnews.epoxy

import com.airbnb.epoxy.EpoxyController
import com.bearddr.newsapp.presentation.models.NewsUiModel

class NewsEpoxyController(
  private val expandItem: (position: Int) -> Unit,
  private val openDetails: (articleUrl: String) -> Unit,
): EpoxyController() {

  private val uiModels = mutableListOf<Pair<NewsUiModel.Header, NewsUiModel.Details>>()

  fun setUiModels(newUiModels: List<Pair<NewsUiModel.Header, NewsUiModel.Details>>) {
    uiModels.clear()
    uiModels.addAll(newUiModels)
    this.requestModelBuild()
  }

  override fun buildModels() {
    uiModels.forEachIndexed { index, newsModel ->
      newsHeader {
        id("id_uiModel_header_${index}")
        newsHeaderModel(newsModel.first)
        myCallback { model, parentView, clickedView, position ->
          expandItem(index)
        }
      }

      if (newsModel.first.expanded) {
        newsDetails {
          id("id_uiModel_details_${index}")
          newsDetailsModel(newsModel.second)
          myCallback { model, parentView, clickedView, position ->
            openDetails(newsModel.first.articleUrl)
          }
        }
      }
    }
  }
}