package com.bearddr.newsapp.usecases.settings

import com.bearddr.newsapp.data.CategoriesRepository
import com.bearddr.newsapp.presentation.models.CategoryUiModel
import com.bearddr.newsapp.usecases.settings.result.CategoriesResult
import com.bearddr.newsapp.usecases.settings.result.GetCategoriesResult
import com.bearddr.newsapp.usecases.settings.result.SaveCategoriesResult
import com.bearddr.newsapp.util.Constants
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
  private val categoriesRepository: CategoriesRepository
) {

  suspend fun saveCategories(categories: Map<String, Boolean>): Flow<CategoriesResult> =
    flow<SaveCategoriesResult> {
      categoriesRepository.saveCategories(categories)
      emit(SaveCategoriesResult.Success)
    }
      .onStart {
        emit(SaveCategoriesResult.Loading)
      }
      .catch { err ->
        Timber.tag("AppDebug").d("UseCase catch - ${err.message}")
        emit(SaveCategoriesResult.Failure(err.message ?: ""))
      }

  suspend fun getCategories(): Flow<CategoriesResult> =
    flow<GetCategoriesResult> {
      val categories = categoriesRepository.getCategories() ?: Constants.allCategoriesOff

      Timber.tag("AppDebug").d("Categories UseCase - $categories")

      val uiModels = mutableListOf<CategoryUiModel>()
      categories.forEach { (category, checked) ->
        uiModels.add(CategoryUiModel(category, checked))
      }

      emit(GetCategoriesResult.Success(uiModels))
    }
      .onStart {
        emit(GetCategoriesResult.Loading)
      }
      .catch { err ->
        emit(GetCategoriesResult.Failure(err.message ?: ""))
      }
}
