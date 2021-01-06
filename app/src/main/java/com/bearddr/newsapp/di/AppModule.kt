package com.bearddr.newsapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.bearddr.newsapp.BuildConfig
import com.bearddr.newsapp.data.CategoriesRepository
import com.bearddr.newsapp.data.CategoriesRepositoryImpl
import com.bearddr.newsapp.data.NewsRepositoryImpl
import com.bearddr.newsapp.data.NewsRepository
import com.bearddr.newsapp.data.network.NewsApi
import com.bearddr.newsapp.util.Constants
import com.bearddr.newsapp.util.DateFormatter
import com.bearddr.newsapp.util.LocalDateTimeJsonAdapter
import com.bearddr.newsapp.util.ResourceHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun provideDateFormatter(
    resourceHelper: ResourceHelper
  ): DateFormatter = DateFormatter(resourceHelper)

  @Provides
  @Singleton
  fun provideMoshi(
    dateFormatter: DateFormatter
  ): Moshi = Moshi.Builder()
    .add(LocalDateTimeJsonAdapter(dateFormatter))
    .build()

  @Provides
  @Singleton
  fun provideStringBooleanMapJsonAdapter(
    moshi: Moshi
  ) : JsonAdapter<Map<String, Boolean>> {
    val type = Types.newParameterizedType(
      Map::class.java,
      String::class.java,
      Boolean::class.javaObjectType
    )
    val jsonAdapter: JsonAdapter<Map<String, Boolean>> = moshi.adapter(type)
    return jsonAdapter
  }

  @Provides
  @Singleton
  fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build()
  } else OkHttpClient
    .Builder()
    .build()

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi
  ): Retrofit =
    Retrofit.Builder()
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .baseUrl(BuildConfig.BASE_URL)
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  fun provideNewsApi(
    retrofit: Retrofit
  ): NewsApi =
    retrofit.create(NewsApi::class.java)





  @Provides
  @Singleton
  fun provideNewsRepository(
    newsApi: NewsApi
  ): NewsRepository =
    NewsRepositoryImpl(newsApi)

  @Provides
  @Singleton
  fun provideCategoriesRepository(
    dataStore: DataStore<Preferences>,
    stringBooleanMapJsonAdapter: JsonAdapter<Map<String, Boolean>>
  ) : CategoriesRepository = CategoriesRepositoryImpl(dataStore, stringBooleanMapJsonAdapter)





  @Provides
  @Singleton
  fun provideResourceHelper(
    @ApplicationContext context: Context
  ): ResourceHelper = ResourceHelper(context)

  @Provides
  @Singleton
  fun provideDataStore(
    @ApplicationContext context: Context
  ): DataStore<Preferences> = context.createDataStore(Constants.categoriesDataStoreName)
}