package com.example.foodfinder11.activities


import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.adapters.OrderItemsAdapter
import com.example.foodfinder11.databinding.ActivityOrderBinding
import com.example.foodfinder11.fragments.QuestionDialogFragment
import com.example.foodfinder11.utils.Constants
import com.example.foodfinder11.utils.SessionManager


class OrderActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityOrderBinding

    private lateinit var orderItemsAdapter: OrderItemsAdapter

    override fun initializeActivity() {

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeViews() {

        initializeAdapters()
        orderItemsAdapter.differ.submitList( SessionManager.fetchOrderItems() )
        updateTotal()
    }

    override fun commitData(): Boolean {

        val intent = Intent(this@OrderActivity, ConfirmOrderActivity::class.java)
        startActivity(intent)

        return true
    }

    private fun initializeAdapters() {

        orderItemsAdapter = OrderItemsAdapter()
        initAdapterClickListeners(orderItemsAdapter)

        resetAdapters()
    }

    private fun initAdapterClickListeners(adapter: OrderItemsAdapter) {

        adapter.onPlusClick(object : OrderItemsAdapter.OnPlusClicked {

            override fun onClickListener(orderItem: OrderItem) {
                updateTotal()
                resetAdapters()
            }

        })

        adapter.onMinusClick(object : OrderItemsAdapter.OnMinusClick {

            override fun onClickListener(orderItem: OrderItem, position: Int): Boolean {

                var result = true

                if (orderItem.count <= 1) {

                    QuestionDialogFragment(
                        getString(R.string.are_you_sure_you_want_to_remove_this_meal_out_of_your_order),
                        getString(R.string.yes),
                        getString(R.string.no),
                        onOkAction = { dialog, id ->

                            onMinusTap(orderItem, position)
                        }
                        , onCancelAction = { dialog, id -> result = false } )
                        .show(supportFragmentManager, "QuestionDialog")

                } else {
                    onMinusTap(orderItem, position)
                }

                return result
            }

        })
    }

    private fun onMinusTap(orderItem: OrderItem, position: Int) {

        orderItem.count--
        SessionManager.saveOrderItem(orderItem)

        updateTotal()

        if (orderItem.count <= 0) {

            orderItemsAdapter.notifyItemRemoved(position)

        } else {

            orderItemsAdapter.notifyItemChanged(position)
        }
    }

    private fun updateTotal() {

        val order = SessionManager.fetchOrder()

        if (order.orderItems.isEmpty())
            finish()

        binding.subtotalPrice.text = "${String.format("%.2f", order.getOrderPrice())} ${binding.subtotalPrice.context.getString(
            R.string.lev)}"
        binding.deliveryPrice.text = "${String.format("%.2f", Constants.DEFAULT_DELIVERY_PRICE)} ${binding.subtotalPrice.context.getString(
            R.string.lev)}"
        binding.discountPrice.text = "0.0 lv." //TODO: Add discount functionality

        val totalPrice = order.getOrderPrice() + Constants.DEFAULT_DELIVERY_PRICE
        binding.totalPrice.text = "${String.format("%.2f", totalPrice)} ${binding.subtotalPrice.context.getString(
            R.string.lev)}"
    }

    private fun resetAdapters() {

        binding.rvOrderItems.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = orderItemsAdapter
        }
    }
}