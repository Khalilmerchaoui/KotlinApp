package com.example.kotlinapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.model.Products
import com.example.kotlinapp.repository.ProductsRepository
import org.koin.standalone.KoinComponent

class ProductsViewModel(val productsRepository : ProductsRepository ) : ViewModel(), KoinComponent {

    var listOfProducts = MutableLiveData<Products>()

    init {
        listOfProducts.value!!.products = listOf()
    }

    fun getProducts() {
        productsRepository.getProducts(object : ProductsRepository.OnProductsDataListener {
            override fun onSuccess(data: Products) {
                listOfProducts.value = data
            }

            override fun onFailure() {
                //REQUEST FAILED
            }
        })
    }
}