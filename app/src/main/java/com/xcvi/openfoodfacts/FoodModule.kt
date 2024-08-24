package com.xcvi.openfoodfacts

import com.google.gson.GsonBuilder
import com.xcvi.openfoodfacts.data.FoodApi
import com.xcvi.openfoodfacts.data.FoodApi.Companion.URL
import com.xcvi.openfoodfacts.data.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FoodModule {
    @Provides
    @Singleton
    fun providesFoodRepository(foodApi: FoodApi) = FoodRepository(foodApi)


    @Provides
    @Singleton
    fun providesFoodApi(): FoodApi{
        val gson = GsonBuilder().create()
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(FoodApi::class.java)
    }




}