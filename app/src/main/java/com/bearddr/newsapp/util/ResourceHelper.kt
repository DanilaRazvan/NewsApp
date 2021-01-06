package com.bearddr.newsapp.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
class ResourceHelper(
  private val context: Context
) {
  fun getString(id: Int) = context.getString(id)
}