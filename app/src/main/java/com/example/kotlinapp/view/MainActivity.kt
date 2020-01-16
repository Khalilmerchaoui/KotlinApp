package com.example.kotlinapp.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinapp.R
import com.example.kotlinapp.model.Product
import com.example.kotlinapp.viewModel.ProductsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import android.os.Vibrator
import android.content.Context
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kotlinapp.model.Utility
import kotlinx.android.synthetic.main.settings_dialog.view.*


class MainActivity : AppCompatActivity() {

    private val productListModel: ProductsViewModel by viewModel()
    private var firstCardClicked = false
    private var secondCardClicked = false
    private var clickedPos = 0
    private var moves = 0
    private var score = 0
    private var v : Vibrator? = null

    private var gridSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        var nbrOfColumns  = Utility.calculateNoOfColumns(applicationContext, 96.0f);
        nbrOfColumns = roundEven(nbrOfColumns)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView!!.layoutManager = GridLayoutManager(applicationContext,   nbrOfColumns)

        initGame()

        imgShuffle.setOnClickListener(View.OnClickListener {
            initGame()
            Toast.makeText(applicationContext, "Pictures shuffled !",Toast.LENGTH_SHORT).show()
        })
        productListModel.listOfProducts.observe(this, Observer(function = fun(productList: List<Product>?) {
            productList?.let {

                var productListAdapter = ProductListAdapter(productList)
                recyclerView.adapter = productListAdapter
                productListAdapter.setItemClickListener(object : ProductListAdapter.ItemClickListener {
                    override fun onItemClick(view : View, imageView: ImageView, position: Int) {

                        updateMoves()

                        if(secondCardClicked == false) {
                            if (imageView.getTag() == false) {
                                flipCard(imageView, productList, position)
                            }
                            //Todo() Check secondCardImageView issue

                            if (firstCardClicked) {
                                Log.i("tagged", "Second card clicked")
                                secondCardClicked = true
                                if (productList.get(position).title.equals(
                                        productList.get(
                                            clickedPos
                                        ).title
                                    )
                                ) {
                                    Log.i("tagged", "match between $position and $clickedPos")
                                    secondCardClicked = false
                                    updateScore()

                                } else {
                                    Log.i("tagged", "no match between $position and $clickedPos")
                                    // 1 second timer and freeze the recyclerview
                                    Handler().postDelayed({
                                        putBackCards(imageView)
                                        putBackCards(productListAdapter.getImageView(clickedPos))
                                        secondCardClicked = false
                                    }, 1000)


                                }
                                firstCardClicked = false
                            } else {
                                Log.i("tagged", "First card clicked")
                                firstCardClicked = true
                                clickedPos = position
                            }

                        }

                        if(isGameOver(productList.size)) {
                            showWinDialog()
                        }
                    }
                })
            }
        }))
    }


    fun putBackCards(imageView : ImageView) {
        imageView.setTag(false)
        Glide.with(imageView.context).load(R.drawable.shopify_logo).into(imageView)

    }

    fun flipCard(imageView : ImageView, productList : List<Product>, position : Int) {
        imageView.setTag(true)
        val imageUrl = productList[position].image.src
        Glide.with(imageView.context).load(imageUrl).into(imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_settings)
            showSettingsDialog()
        return super.onOptionsItemSelected(item)
    }

    fun updateMoves() {
        moves = moves.inc()
        txtMoves.text = "Moves : $moves"
    }

    fun updateScore() {
        score = score.inc()
        txtScore.text = "Score : $score"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v!!.vibrate(200);
        }
    }

    fun showWinDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("You Won !!")

        builder.setMessage("Start new game?")

        builder.setPositiveButton("YES"){dialog, which ->
            initGame()
        }


        builder.setNeutralButton("Cancel"){_,_ ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    fun initGame() {
         firstCardClicked = false
         secondCardClicked = false
         clickedPos = 0
         moves = 0
         score = 0

        productListModel.getProducts()
        txtScore.text = "Score : $score"
        txtMoves.text = "Moves : $moves"
    }

    fun isGameOver(size : Int) : Boolean{
        return score * 2 == size
    }

    fun showSettingsDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.settings_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Settings")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout

        mDialogView.gridSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                gridSize = seekBar!!.progress
            }

        })

    }

    fun roundEven(d : Int) : Int {
    return (Math.round(d / 2.0) * 2).toInt();
}
}
