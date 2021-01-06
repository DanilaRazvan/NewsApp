package com.bearddr.newsapp.presentation.settings.epoxy

import com.airbnb.epoxy.EpoxyController
import com.bearddr.newsapp.presentation.models.CategoryUiModel

class CategoriesEpoxyController(
  private val checkToggle: (category: String, checked: Boolean) -> Unit
) : EpoxyController() {

  private val uiModels = mutableListOf<CategoryUiModel>()

  fun setUiModels(newUiModels: List<CategoryUiModel>) {
    uiModels.clear()
    uiModels.addAll(newUiModels)
    this.requestModelBuild()
  }

  override fun buildModels() {
    uiModels.forEachIndexed { index, model ->
      category {
        id("id_category_$index")
        category(model)
        myCallback { buttonView, isChecked ->
          checkToggle(model.title, isChecked)
        }
      }
    }
  }
}