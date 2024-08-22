package com.xcvi.openfoodfacts.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Query

interface FoodApi {

//    @GET("https://world.openfoodfacts.net/api/v2/search?categories_tags=Nutella")
//    suspend fun getProduct(): Response<SearchResponse>



    @GET(SEARCH_URL)
    suspend fun searchProduct(
        @Query("search_terms") name: String,
        @Query("json") json: Int = 1,
    ): Response<SearchResponse>

    @GET(BARCODE_URL)
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): Response<BarcodeResponse>

    companion object{
        const val search =
            "https://world.openfoodfacts.org/cgi/search.pl?search_terms=banania&search_simple=1&action=process&json=1"

        const val RICE_NOODLES = "737628064502"
        const val BARCODE_URL = "api/v1/product/{barcode}"
        const val SEARCH_URL = "cgi/search.pl"
        const val URL = "https://world.openfoodfacts.org/"
    }

}