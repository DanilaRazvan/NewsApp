package com.bearddr.newsapp.presentation.search.epoxy

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.bearddr.newsapp.presentation.models.NewsUiModel

class SearchNewsEpoxyController(
  private val openDetails: (articleUrl: String) -> Unit
) : EpoxyController() {

  private val uiModels: MutableList<NewsUiModel> = mutableListOf()

  fun setUiModels(newUiModels: List<NewsUiModel>) {
    uiModels.clear()
    uiModels.addAll(newUiModels)
    this.requestModelBuild()
  }

  override fun buildModels() {
    uiModels.forEachIndexed { index, uiModel ->
      when (uiModel) {
        is NewsUiModel.NewsSearchCategoryTitle -> {
          searchCategory {
            id("id_uiModel_search_category_$index")
            categoryTitle(uiModel.categoryTitle)
          }
        }

        is NewsUiModel.ListOfNewsHeader -> {
          val carouselModels = mutableListOf<NewsHeaderEpoxyModel_>()
          uiModel.list.mapTo(carouselModels) {
            NewsHeaderEpoxyModel_()
              .id(it.toString())
              .newsHeaderModel(it)
              .myCallback { model, parentView, clickedView, position ->
                openDetails(it.articleUrl)
              }
          }

          carousel {
            id("id_uiModels_search_category_$index")
            numViewsToShowOnScreen(1.1f)
            models(carouselModels)
          }
        }
      }
    }
  }
}