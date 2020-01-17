package com.example.kotlinapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.model.Product
import com.example.kotlinapp.model.Products
import com.example.kotlinapp.repository.ProductsRepository
import org.koin.standalone.KoinComponent
import androidx.lifecycle.MediatorLiveData



class ProductsViewModel(val productsRepository : ProductsRepository ) : ViewModel(), KoinComponent {

    var listOfProducts = MutableLiveData<List<Product>>()


    init {
        listOfProducts.value = listOf()
    }


    fun getProducts(gridSize : Int) {
        productsRepository.getProducts(object : ProductsRepository.OnProductsDataListener {
            override fun onSuccess(data: Products) {

                listOfProducts.value = getRandomProductsList(data.products, gridSize)
            }

            override fun onFailure(msg : String) {
                //REQUEST FAILED
            }
        })
    }

    fun getRandomProductsList(products : List<Product>, gridSize: Int) : List<Product> {
        val randomizedList : MutableList<Product> = mutableListOf()
        val productsListCopy = products.toMutableList()

        for(i in 1..(gridSize/2)) {
        val randomProduct = productsListCopy.random()
            randomizedList.add(randomProduct)
            productsListCopy.remove(randomProduct)
        }
        //double the list and shuffle
        randomizedList.addAll(randomizedList)
        randomizedList.shuffle()
        return randomizedList
    }
}