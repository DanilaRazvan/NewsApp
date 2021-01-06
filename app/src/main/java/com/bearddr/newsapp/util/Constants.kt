package com.bearddr.newsapp.util

import androidx.datastore.preferences.core.preferencesKey

object Constants {

  const val sortByDate = "publishedAt"
  const val sortByRelevancy = "relevancy"
  const val sortByPopularity = "popularity"

  const val categoriesDataStoreName: String = "categories_settings"
  val categoriesDataStoreKey = preferencesKey<String>("categories")

  const val BUSINESS_CAT = "business"
  const val ENTERTAINMENT_CAT = "entertainment"
  const val GENERAL_CAT = "general"
  const val HEALTH_CAT = "health"
  const val SCIENCE_CAT = "science"
  const val SPORTS_CAT = "sports"
  const val TECHNOLOGY_CAT = "technology"

  val allCategoriesOff: Map<String, Boolean> = mapOf(
    BUSINESS_CAT to false,
    ENTERTAINMENT_CAT to false,
    GENERAL_CAT to false,
    HEALTH_CAT to false,
    SCIENCE_CAT to false,
    SPORTS_CAT to false,
    TECHNOLOGY_CAT to false
  )
}