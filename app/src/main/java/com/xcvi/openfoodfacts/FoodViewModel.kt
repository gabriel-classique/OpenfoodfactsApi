package com.xcvi.openfoodfacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xcvi.openfoodfacts.api.FoodApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val api: FoodApi
): ViewModel() {

    var foodSearch by mutableStateOf("")
    var foodDefault by mutableStateOf("")

    init {
        viewModelScope.launch {
            foodSearch = api.getProductByBarcode("8017596107473").body()?.product?.product_name.toString()
            //foodSearch = api.searchProduct("pane eurospin").body()?.products?.firstOrNull().toString()
        }
    }

}