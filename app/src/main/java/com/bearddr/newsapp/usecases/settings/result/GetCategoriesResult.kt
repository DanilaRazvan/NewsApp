package com.bearddr.newsapp.usecases.settings.result

import com.bearddr.newsapp.presentation.models.CategoryUiModel

sealed class GetCategoriesResult : CategoriesResult {
  object Loading : GetCategoriesResult()

  data class Success(
    val categories: List<CategoryUiModel>
  ) : GetCategoriesResult()

  data class Failure(
    val message: String
  ) : GetCategoriesResult()
}