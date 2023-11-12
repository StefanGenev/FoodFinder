package com.example.foodfinder11.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.adapters.MenuItemsAdapter
import com.example.foodfinder11.adapters.OrderItemsAdapter
import com.example.foodfinder11.databinding.ActivityOrderBinding


class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding

    private lateinit var orderedItemsArray: ArrayList<OrderItem>
    private lateinit var orderItemsAdapter: OrderItemsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Order"

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        prepareRecyclerView()
        getMealsInformation()
    }

    private fun prepareRecyclerView() {
        orderItemsAdapter = OrderItemsAdapter()

        binding.rvItems.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = orderItemsAdapter
        }
    }

    private fun getMealsInformation() {
        orderedItemsArray = ArrayList()

        val intent = intent

        orderedItemsArray = intent.extras?.getParcelableArrayList<OrderItem>(MealActivity.ORDERED_ITEMS_ARRAY)!!


        orderItemsAdapter.differ.submitList(orderedItemsArray)
    }
}