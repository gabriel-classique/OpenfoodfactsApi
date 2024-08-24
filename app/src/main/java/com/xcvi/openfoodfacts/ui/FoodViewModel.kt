package com.xcvi.openfoodfacts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xcvi.openfoodfacts.data.FoodRepository
import com.xcvi.openfoodfacts.data.FoodDto
import com.xcvi.openfoodfacts.domain.FoodModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    var foodName by mutableStateOf("")
    var kcal by mutableStateOf("")
    var fat by mutableStateOf("")
    var carbs by mutableStateOf("")
    var protein by mutableStateOf("")

    data class FoodScreenState(
        val searchResult: List<FoodModel> = emptyList(),
        val currentFood: FoodModel? = null
    )

    private val _state = MutableStateFlow(FoodScreenState())
    val state = _state.asStateFlow()

    fun catch(barcode: String) {
        viewModelScope.launch {
            val data = repository.catchBarcode(barcode)
            if (data != null) {
                _state.update {
                    it.copy(searchResult = emptyList(), currentFood = data)
                }
            }
        }
    }

    fun search(name: String, page: Int = 1) {
        viewModelScope.launch {
            val data = repository.searchByName(name, page)
            if (data.isNotEmpty()) {
                _state.update {
                    it.copy(searchResult = data, currentFood = null)
                }
            }
        }
    }

}