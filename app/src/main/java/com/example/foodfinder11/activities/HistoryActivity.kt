package com.example.foodfinder11.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.HistoryItemsAdapter
import com.example.foodfinder11.adapters.OrderItemsAdapter
import com.example.foodfinder11.databinding.ActivityHistoryBinding
import com.example.foodfinder11.databinding.ActivityOrderBinding
import com.example.foodfinder11.viewModel.HomeViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyItemsAdapter: HistoryItemsAdapter

    private lateinit var homeMvvm: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
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
        getOrdersInformation()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun prepareRecyclerView() {
        historyItemsAdapter = HistoryItemsAdapter()

        binding.rvOrders.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = historyItemsAdapter
        }
    }

    private fun getOrdersInformation() {
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
        homeMvvm.getAllMealsByRandomLetter()

        homeMvvm.observeAllMealsLiveData().observe(this, Observer { meals ->
            historyItemsAdapter.differ.submitList(meals)
        })
    }
}