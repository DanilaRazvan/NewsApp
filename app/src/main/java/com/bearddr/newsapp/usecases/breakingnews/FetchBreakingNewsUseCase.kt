package com.bearddr.newsapp.usecases.breakingnews

import com.bearddr.newsapp.data.CategoriesRepository
import com.bearddr.newsapp.data.NewsRepository
import com.bearddr.newsapp.data.mapper.TopHeadlineResponseToNewsUiModel
import com.bearddr.newsapp.usecases.breakingnews.result.BreakingNewsResult
import com.bearddr.newsapp.usecases.breakingnews.result.FetchBreakingNewsResult
import com.bearddr.newsapp.usecases.settings.CategoriesUseCase
import com.bearddr.newsapp.util.NetworkHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FetchBreakingNewsUseCase @Inject constructor(
  private val newsRepository: NewsRepository,
  private val categoriesRepository: CategoriesRepository,
  private val networkHelper: NetworkHelper,
  private val topHeadlineResponseToNewsUiModel: TopHeadlineResponseToNewsUiModel
) {

  suspend fun fetchBreakingNews(): Flow<BreakingNewsResult> = flow {
    val categories = categoriesRepository.getCategories()
    categories?.let {
      emit(handleFetchTopHeadlinesResponse(it))
    } ?: emit(FetchBreakingNewsResult.Failure(-1, "No categories found"))
  }
    .onStart { FetchBreakingNewsResult.Loading }

  private suspend fun handleFetchTopHeadlinesResponse(categories: Map<String, Boolean>): BreakingNewsResult {
    if (networkHelper.isNetworkConnected()) {
      try {
        val topHeadlinesResponse =
          newsRepository.getTopHeadlines(categories.filter { it.value }.keys.toList())

        topHeadlinesResponse.articles?.let {
          return FetchBreakingNewsResult.Success(it.map { newsDto ->
            topHeadlineResponseToNewsUiModel.map(
              newsDto
            )
          })
        } ?: return FetchBreakingNewsResult.Failure(-1, "null articles")

      } catch (e: Exception) {
        Timber.tag("AppDebug").d(e)
        if (e !is HttpException && e !is SocketTimeoutException)
          return FetchBreakingNewsResult.Failure(-1, e.message.toString())

        return when (e) {
          is HttpException -> {
            when (e.code()) {
              400 -> FetchBreakingNewsResult.Error400()
              401 -> FetchBreakingNewsResult.Error401()
              403 -> FetchBreakingNewsResult.Error403()
              500 -> FetchBreakingNewsResult.Error500()
              else -> FetchBreakingNewsResult.Failure(e.code(), e.message())
            }
          }

          else -> FetchBreakingNewsResult.Failure(-1, "Time out")
        }
      }
    } else {
      return FetchBreakingNewsResult.Failure(-1, "No internet connection")
    }
  }
}