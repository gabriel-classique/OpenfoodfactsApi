package com.xcvi.openfoodfacts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xcvi.openfoodfacts.api.FoodApi
import com.xcvi.openfoodfacts.api.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val api: FoodApi
): ViewModel() {

    var foodName by mutableStateOf("")
    var kcal by mutableStateOf("")
    var fat by mutableStateOf("")
    var carbs by mutableStateOf("")
    var protein by mutableStateOf("")

    data class SearchState(
        val foods: List<Product>
    )

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState(emptyList()))
    val searchState = _searchState.asStateFlow()

    fun catch(barcode: String){
        viewModelScope.launch {
            api.getByBarcode(barcode).body()?.product?.let {
                foodName = it.product_name
                kcal = it.nutriments?.calories.toString()
                fat = it.nutriments?.fats.toString()
                carbs = it.nutriments?.carbs.toString()
                protein = it.nutriments?.protein.toString()
            }
        }
    }

    fun search(name: String){
        viewModelScope.launch {
            api.getByName(name).body()?.products?.let { data->
                _searchState.update { state ->
                    state.copy(foods = data)
                }
            }
        }
    }

}