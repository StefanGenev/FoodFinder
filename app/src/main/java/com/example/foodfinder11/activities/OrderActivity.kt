package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.adapters.OrderItemsAdapter
import com.example.foodfinder11.databinding.ActivityOrderBinding
import com.example.foodfinder11.pojo.Meal


class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding

    private lateinit var orderedItemsArray: ArrayList<OrderItem>
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private var totalPrice: Float = 0.0F

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

    override fun onSupportNavigateUp(): Boolean {
        passItemsToPreviousActivity()
        return true
    }

    private fun prepareRecyclerView() {
        orderItemsAdapter = OrderItemsAdapter()

        orderItemsAdapter.onPlusClick(object : OrderItemsAdapter.OnPlusClicked {
            override fun onClickListener(orderItem: OrderItem) {
                totalPrice += 10
                binding.tvSubtotalPrice.text = "${String.format("%.2f", totalPrice)} lv."
                binding.tvTotalPrice.text = "${String.format("%.2f", totalPrice + 3)} lv."
            }

        })

        orderItemsAdapter.onMinusClick(object : OrderItemsAdapter.OnMinusClick {
            override fun onClickListener(orderItem: OrderItem, isLast: Boolean) {
                if (isLast)
                {
                    passItemsToPreviousActivity()
                }

                if (orderItem.intMealCount <= 0)
                {
                    orderedItemsArray.remove(orderItem)
                }

                totalPrice -= 10
                binding.tvSubtotalPrice.text = "${String.format("%.2f", totalPrice)} lv."
                binding.tvTotalPrice.text = "${String.format("%.2f", totalPrice + 3)} lv."
            }

        })

        binding.rvItems.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = orderItemsAdapter
        }
    }

    private fun passItemsToPreviousActivity() {
        val data = Intent()
        data.putExtra(MealActivity.ORDERED_ITEMS_ARRAY, orderedItemsArray);
        setResult(Activity.RESULT_OK, data)

        finish()
    }

    private fun getMealsInformation() {
        orderedItemsArray = ArrayList()

        val intent = intent
        orderedItemsArray = intent.extras?.getParcelableArrayList<OrderItem>(MealActivity.ORDERED_ITEMS_ARRAY)!!
        orderItemsAdapter.differ.submitList(orderedItemsArray)

        for (item in orderedItemsArray)
            totalPrice += 10 * item.intMealCount

        binding.tvSubtotalPrice.text = "${String.format("%.2f", totalPrice)} lv."
        binding.tvTotalPrice.text = "${String.format("%.2f", totalPrice + 3)} lv."
    }

}