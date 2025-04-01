package com.example.dunibazar.ui.features.categoryScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dunibazar.model.data.Product
import com.example.dunibazar.model.repository.product.ProductRepository
import kotlinx.coroutines.launch


class CategoryViewModel(
    private val productRepository: ProductRepository,
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())


    fun loadDataByCategory(category: String) {

        viewModelScope.launch {

            val dataFromLocal = productRepository.getAllDataByCategory(category)
            dataProducts.value = dataFromLocal
        }

    }


}