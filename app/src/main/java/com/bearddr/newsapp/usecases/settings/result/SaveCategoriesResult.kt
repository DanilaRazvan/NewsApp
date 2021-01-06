package com.bearddr.newsapp.usecases.settings.result

sealed class SaveCategoriesResult : CategoriesResult {
  object Loading: SaveCategoriesResult()

  object Success: SaveCategoriesResult()

  data class Failure(
    val message: String
  ): SaveCategoriesResult()
}