package com.xcvi.openfoodfacts

import com.google.gson.GsonBuilder
import com.xcvi.openfoodfacts.api.FoodApi
import com.xcvi.openfoodfacts.api.FoodApi.Companion.SEARCH_URL
import com.xcvi.openfoodfacts.api.FoodApi.Companion.URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FoodModule {
    @Provides
    @Singleton
    fun providesFoodApi(): FoodApi{
        val gson = GsonBuilder().create()
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(FoodApi::class.java)
    }


}