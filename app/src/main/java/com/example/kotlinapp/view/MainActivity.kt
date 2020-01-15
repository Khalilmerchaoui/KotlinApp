package com.example.kotlinapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinapp.R
import com.example.kotlinapp.model.Product
import com.example.kotlinapp.viewModel.ProductsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val productListModel: ProductsViewModel by viewModel()
    private var firstCardClicked = false
    private var clickedPos = 0
    private lateinit var clickedImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val apiService = ApiService()

        GlobalScope.launch(Dispatchers.Main) {
            val response = apiService.getProducts(1).await();
            txtResponse.text = response.products.toString()
        }*/

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView!!.layoutManager = GridLayoutManager(applicationContext,   4)


        productListModel.getProducts()

        productListModel.listOfProducts.observe(this, Observer(function = fun(productList: List<Product>?) {
            productList?.let {

                var productListAdapter = ProductListAdapter(productList)
                recyclerView.adapter = productListAdapter
                productListAdapter.setItemClickListener(object : ProductListAdapter.ItemClickListener {
                    override fun onItemClick(imageView: ImageView, position: Int) {

                        if(imageView.getTag() == false) {
                            flipCard(imageView, productList, position)
                        }

                        //Todo() Check secondCardImageView issue

                        if(firstCardClicked) {
                            Log.i("tagged", "Second card clicked")
                            if(productList.get(position).title.equals(productList.get(clickedPos).title)) {
                                Log.i("tagged", "match between $position and $clickedPos")
                            } else {
                                Log.i("tagged", "no match between $position and $clickedPos")
                                putBackCards(imageView, clickedImageView)
                            }
                            firstCardClicked = false
                        } else {
                            Log.i("tagged", "First card clicked")
                            firstCardClicked = true
                            clickedPos = position
                            clickedImageView.findViewById<ImageView>(imageView.id)
                        }

                    }
                })
            }
        }))
    }


    fun putBackCards(secondImageView : ImageView, firstImageView : ImageView) {
        firstImageView.setTag(false)
        firstImageView.setImageResource(R.drawable.shopify_logo)

        secondImageView.setTag(false)
        secondImageView.setImageResource(R.drawable.shopify_logo)

    }

    fun flipCard(imageView : ImageView, productList : List<Product>, position : Int) {
        imageView.setTag(true)
        val imageUrl = productList[position].image.src
        Glide.with(imageView.context).load(imageUrl).into(imageView)
    }
}
