package com.bearddr.newsapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.bearddr.newsapp.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>,
  private val stringBooleanMapJsonAdapter: JsonAdapter<Map<String, Boolean>>
) : CategoriesRepository {

  override suspend fun saveCategories(categories: Map<String, Boolean>) {
    dataStore.edit { settings ->
      settings[Constants.categoriesDataStoreKey] = stringBooleanMapJsonAdapter.toJson(categories)
    }

    Timber.tag("AppDebug").d("Save categories - ${dataStore.data.first()[Constants.categoriesDataStoreKey]}")
  }

  override suspend fun getCategories(): Map<String, Boolean>? {
    val preferences = dataStore.data.first()

    return preferences[Constants.categoriesDataStoreKey]?.let {
      stringBooleanMapJsonAdapter.fromJson(it)
    } ?: Constants.allCategoriesOff
  }
}