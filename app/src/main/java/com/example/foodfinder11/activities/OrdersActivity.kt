package com.example.foodfinder11.activities

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.OrderItemsAdapter
import com.example.foodfinder11.adapters.OrdersAdapter
import com.example.foodfinder11.databinding.ActivityOrdersBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.Order
import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private var orders: ArrayList<Order> = ArrayList()

    private lateinit var ordersAdapter: OrdersAdapter


    override fun initializeActivity() {
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun loadData(): Boolean {

        loadOrdersRequest()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun loadOrdersRequest() {

        val dto = IdentifierDto(id = SessionManager.fetchUserData().id)

        RetrofitInstance.getApiService().getOrders(dto)
            .enqueue(object : Callback<ResponseWrapper<List<Order>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<Order>>>,
                    response: Response<ResponseWrapper<List<Order>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        orders.addAll(responseData)

                        initOrders()

                    } else {
                        Toast.makeText(this@OrdersActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<Order>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@OrdersActivity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun initOrders() {

        binding.emptyStateLayout.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE

        ordersAdapter = OrdersAdapter()

        ordersAdapter.onItemClicked(object : OrdersAdapter.OnItemClicked {

            override fun onClickListener(order: Order) {

            }

        })

        resetAdapters()
        ordersAdapter.differ.submitList( orders )
    }

    private fun resetAdapters() {

        binding.rvOrders.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = ordersAdapter
        }
    }

}