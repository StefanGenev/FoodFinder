package com.example.foodfinder11.activities


import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.adapters.OrderItemsAdapter
import com.example.foodfinder11.databinding.ActivityOrderBinding


class OrderActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityOrderBinding

    private lateinit var orderedItemsArray: ArrayList<OrderItem>
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private var totalPrice: Float = 0.0F

    override fun initializeActivity() {

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {
    }


}