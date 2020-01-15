package com.example.kotlinapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.R
import com.example.kotlinapp.model.Product
import com.example.kotlinapp.viewModel.ProductsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val productListModel: ProductsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val apiService = ApiService()

        GlobalScope.launch(Dispatchers.Main) {
            val response = apiService.getProducts(1).await();
            txtResponse.text = response.products.toString()
        }*/

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView!!.layoutManager = GridLayoutManager(applicationContext,   3)


        productListModel.getProducts()

        productListModel.listOfProducts.observe(this, Observer(function = fun(productList: List<Product>?) {
            productList?.let {

                var productListAdapter: ProductListAdapter = ProductListAdapter(productList)
                recyclerView.adapter = productListAdapter
                productListAdapter.setItemClickListener(object : ProductListAdapter.ItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                    }
                })
            }
        }))
    }
}
