package com.xcvi.openfoodfacts.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xcvi.openfoodfacts.data.FoodRepository
import com.xcvi.openfoodfacts.domain.FoodModel
import com.xcvi.openfoodfacts.domain.FoodResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    data class FoodScreenState(
        val searchResult: List<FoodModel> = emptyList(),
        val currentFood: FoodModel? = null,
        val isLoading: Boolean = false,
    )

    private val _state = MutableStateFlow(FoodScreenState())
    val state = _state.asStateFlow()

    fun catch(barcode: String, context: Context) {
        toggleLoading(true)
        viewModelScope.launch {
            val result = repository.searchBarcode(barcode)
            if (result.isSuccessful) {
                _state.update {
                    it.copy(
                        searchResult = emptyList(),
                        currentFood = result.result
                    )
                }
            } else {
                //handle errors as desired
                if(result.error.isBlank()){
                    Toast.makeText(context, "Failed To Load, Try Again!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }
            }
            toggleLoading(false)
        }
    }


    fun search(name: String, page: Int = 1, context: Context) {
        toggleLoading(true)
        viewModelScope.launch {
            val result = repository.searchByName(name, page)
            if (result.isSuccessful) {
                _state.update {
                    it.copy(
                        searchResult = result.result,
                        currentFood = null
                    )
                }
            } else {
                //handle errors as desired
                if(result.error.isBlank()){
                    Toast.makeText(context, "Failed To Load, Try Again!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }

            }
            delay(500)
            toggleLoading(false)
        }
    }

    private fun toggleLoading(boolean: Boolean) {
        _state.update {
            it.copy(isLoading = boolean)
        }
    }

}