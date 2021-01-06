package com.bearddr.newsapp.data

interface CategoriesRepository {
  suspend fun saveCategories(categories: Map<String, Boolean>)

  suspend fun getCategories() : Map<String, Boolean>?
}