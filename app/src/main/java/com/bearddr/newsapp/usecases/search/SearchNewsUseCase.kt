package com.bearddr.newsapp.usecases.search

import com.bearddr.newsapp.data.NewsRepository
import com.bearddr.newsapp.presentation.models.NewsUiModel
import com.bearddr.newsapp.usecases.search.result.SearchNewsResult
import com.bearddr.newsapp.usecases.search.result.SearchResult
import com.bearddr.newsapp.util.Constants.sortByDate
import com.bearddr.newsapp.util.Constants.sortByPopularity
import com.bearddr.newsapp.util.Constants.sortByRelevancy
import com.bearddr.newsapp.util.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject

@FlowPreview
class SearchNewsUseCase @Inject constructor(
  private val repository: NewsRepository,
  private val networkHelper: NetworkHelper,
) {

  private val uiModels = arrayListOf<NewsUiModel?>(
    NewsUiModel.NewsSearchCategoryTitle("Most Relevant"),
    null,
    NewsUiModel.NewsSearchCategoryTitle("Most Recent"),
    null,
    NewsUiModel.NewsSearchCategoryTitle("Most Popular"),
    null,
  )

  suspend fun searchNews(query: String): Flow<SearchResult> = handleSearchNewsResponse(query)


  private suspend fun searchNewsByFilter(query: String, filter: String): SearchResult {
    if (networkHelper.isNetworkConnected()) {
      try {
        val searchNewsResponse = repository.searchNews(
          mapOf(
            "q" to query,
            "sortBy" to filter
          )
        )
        searchNewsResponse.articles?.let { news ->
          return when (filter) {
            sortByDate -> {
              uiModels[1] = NewsUiModel.ListOfNewsHeader(news.map { newsDto ->
                NewsUiModel.Header(
                  newsDto.title ?: "",
                  newsDto.imageUrl ?: "",
                  newsDto.articleUrl ?: "https://www.google.com/"
                )
              })
              SearchNewsResult.DateSorted.Success(uiModels.mapNotNull { it })
            }

            sortByPopularity -> {
              uiModels[5] = NewsUiModel.ListOfNewsHeader(news.map { newsDto ->
                NewsUiModel.Header(
                  newsDto.title ?: "",
                  newsDto.imageUrl ?: "",
                  newsDto.articleUrl ?: "https://www.google.com/"
                )
              })
              SearchNewsResult.PopularitySorted.Success(uiModels.mapNotNull { it })
            }

            else -> {
              uiModels[3] = NewsUiModel.ListOfNewsHeader(news.map { newsDto ->
                NewsUiModel.Header(
                  newsDto.title ?: "",
                  newsDto.imageUrl ?: "",
                  newsDto.articleUrl ?: "https://www.google.com/"
                )
              })
              SearchNewsResult.RelevancySorted.Success(uiModels.mapNotNull { it })
            }
          }
        } ?: return when (filter) {
          sortByDate -> SearchNewsResult.DateSorted.Failure(-1, "null articles")
          sortByPopularity -> SearchNewsResult.PopularitySorted.Failure(-1, "null articles")
          sortByRelevancy -> SearchNewsResult.RelevancySorted.Failure(-1, "null articles")
          else -> SearchNewsResult.RelevancySorted.Failure(-1, "null articles")
        }

      } catch (e: Exception) {
        Timber.tag("AppDebug").d(e)
        if (e !is HttpException && e !is SocketTimeoutException)
          return when (filter) {
            sortByDate -> SearchNewsResult.DateSorted.Failure(-1, e.message.toString())
            sortByPopularity -> SearchNewsResult.PopularitySorted.Failure(-1, e.message.toString())
            sortByRelevancy -> SearchNewsResult.RelevancySorted.Failure(-1, e.message.toString())
            else -> SearchNewsResult.RelevancySorted.Failure(-1, e.message.toString())
          }

        return when (e) {
          is HttpException -> {
            when (e.code()) {
              400 -> when (filter) {
                sortByDate -> SearchNewsResult.DateSorted.Error400()
                sortByPopularity -> SearchNewsResult.PopularitySorted.Error400()
                sortByRelevancy -> SearchNewsResult.RelevancySorted.Error400()
                else -> SearchNewsResult.RelevancySorted.Error400()
              }
              401 -> when (filter) {
                sortByDate -> SearchNewsResult.DateSorted.Error401()
                sortByPopularity -> SearchNewsResult.PopularitySorted.Error401()
                sortByRelevancy -> SearchNewsResult.RelevancySorted.Error401()
                else -> SearchNewsResult.RelevancySorted.Error401()
              }
              403 -> when (filter) {
                sortByDate -> SearchNewsResult.DateSorted.Error403()
                sortByPopularity -> SearchNewsResult.PopularitySorted.Error403()
                sortByRelevancy -> SearchNewsResult.RelevancySorted.Error403()
                else -> SearchNewsResult.RelevancySorted.Error403()
              }
              500 -> when (filter) {
                sortByDate -> SearchNewsResult.DateSorted.Error500()
                sortByPopularity -> SearchNewsResult.PopularitySorted.Error500()
                sortByRelevancy -> SearchNewsResult.RelevancySorted.Error500()
                else -> SearchNewsResult.RelevancySorted.Error500()
              }
              else -> when (filter) {
                sortByDate -> SearchNewsResult.DateSorted.Failure(e.code(), e.message())
                sortByPopularity -> SearchNewsResult.PopularitySorted.Failure(e.code(), e.message())
                sortByRelevancy -> SearchNewsResult.RelevancySorted.Failure(e.code(), e.message())
                else -> SearchNewsResult.RelevancySorted.Failure(e.code(), e.message())
              }
            }
          }

          else -> when (filter) {
            sortByDate -> SearchNewsResult.DateSorted.Failure(-1, "Time out")
            sortByPopularity -> SearchNewsResult.PopularitySorted.Failure(-1, "Time out")
            sortByRelevancy -> SearchNewsResult.RelevancySorted.Failure(-1, "Time out")
            else -> SearchNewsResult.RelevancySorted.Failure(-1, "Time out")
          }
        }
      }
    } else {
      return when (filter) {
        sortByDate -> SearchNewsResult.DateSorted.Failure(-1, "No internet connection")
        sortByPopularity -> SearchNewsResult.PopularitySorted.Failure(-1, "No internet connection")
        sortByRelevancy -> SearchNewsResult.RelevancySorted.Failure(-1, "No internet connection")
        else -> SearchNewsResult.RelevancySorted.Failure(-1, "No internet connection")
      }
    }
  }


  private suspend fun handleSearchNewsResponse(query: String): Flow<SearchResult> {
    val searchByDateResult =
      flowOf(searchNewsByFilter(query, sortByDate)).flowOn(Dispatchers.IO)

    val searchByRelevancyResult =
      flowOf(searchNewsByFilter(query, sortByRelevancy)).flowOn(Dispatchers.IO)

    val searchByPopularityResult =
      flowOf(searchNewsByFilter(query, sortByPopularity))
        .onEach {
          if (it is SearchNewsResult.PopularitySorted.Success)
            Timber.tag("AppDebug").d("${it.news.filterIsInstance<NewsUiModel.ListOfNewsHeader>().map { it.list } }}}")
        }
        .flowOn(Dispatchers.IO)

    return flowOf(
      searchByDateResult,
      searchByPopularityResult,
      searchByRelevancyResult
    ).flattenMerge()

  }
}
