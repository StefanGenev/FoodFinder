package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.OrdersAdapter
import com.example.foodfinder11.databinding.FragmentBusinessOrdersBinding
import com.example.foodfinder11.model.Order
import com.example.foodfinder11.viewModel.BusinessMainViewModel


class BusinessOrdersFragment : Fragment() {

    private lateinit var binding: FragmentBusinessOrdersBinding
    private val mainViewModel: BusinessMainViewModel by activityViewModels()

    private lateinit var ordersAdapter: OrdersAdapter

    private var orders: List<Order> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBusinessOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        loadOrders()

        ordersAdapter.onItemClicked(object : OrdersAdapter.OnItemClicked {

            override fun onClickListener(order: Order) {
                onOrderTapped(order)
            }

        })
    }

    override fun onResume() {
        super.onResume()

        loadOrders()
    }

    private fun onOrderTapped(order: Order) {

    }

    private fun loadOrders() {
        mainViewModel.loadOrders()

        mainViewModel.getOrdersLiveData()
            .observe(viewLifecycleOwner, Observer { orders ->

                if (orders.isEmpty()) {

                    showEmptyState()

                } else {

                    this.orders = orders
                    initOrdersData()
                }

            })
    }

    private fun initOrdersData() {

        ordersAdapter.differ.submitList(orders)

        val totalOrders = orders.size.toString()
        binding.tvTotal.text = totalOrders
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.tvTotal.visibility = View.GONE
    }

    private fun prepareRecyclerView() {

        ordersAdapter = OrdersAdapter()

        binding.rvOrders.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = ordersAdapter
        }
    }
}