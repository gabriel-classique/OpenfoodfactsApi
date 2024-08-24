package com.xcvi.openfoodfacts.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Query

interface FoodApi {

    @GET(SEARCH_URL)
    suspend fun getByName(
        @Query("search_terms") name: String,
        @Query("json") json: Int = 1,
        @Query("page") page: Int = 1,
    ): Response<SearchResponse>

    @GET(BARCODE_URL)
    suspend fun getByBarcode(@Path("barcode") barcode: String): Response<BarcodeResponse>

    companion object{
        const val BARCODE_URL = "api/v1/product/{barcode}"
        const val SEARCH_URL = "cgi/search.pl"
        const val URL = "https://world.openfoodfacts.org/"
    }

}