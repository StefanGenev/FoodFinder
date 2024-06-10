package com.example.foodfinder11.adapters;

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.WelcomeActivity
import com.example.foodfinder11.databinding.OrderItemCardBinding
import com.example.foodfinder11.fragments.QuestionDialogFragment
import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.utils.SessionManager


class OrderItemsAdapter : RecyclerView.Adapter<OrderItemsAdapter.OrderItemsViewHolder>() {

    private lateinit var onPlusClicked: OrderItemsAdapter.OnPlusClicked
    private lateinit var onMinusClick: OrderItemsAdapter.OnMinusClick

    inner class OrderItemsViewHolder(val binding: OrderItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.meal.id == newItem.meal.id
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onBindViewHolder(holder: OrderItemsViewHolder, position: Int) {

        val orderItem = differ.currentList[position]

        Glide.with(holder.itemView).load(orderItem.meal.imageUrl).into(holder.binding.menuImage)
        holder.binding.mealName.text = orderItem.meal.name

        updateOrderItemData(holder, orderItem)

        holder.binding.addButton.setOnClickListener {

            orderItem.count++
            updateOrderItemData(holder, orderItem)

            onPlusClicked.onClickListener(orderItem)
        }

        holder.binding.removeButton.setOnClickListener {
            onMinusTap(holder, orderItem, position)
        }
    }

    private fun onMinusTap(holder: OrderItemsViewHolder, orderItem: OrderItem, position: Int) {

        if (!onMinusClick.onClickListener(orderItem, position))
            return
    }

    private fun updateOrderItemData(holder: OrderItemsViewHolder, orderItem: OrderItem) {

        holder.binding.price.text =
            "${String.format("%.2f", orderItem.meal.getActualPrice(orderItem.count))} lv."
        holder.binding.count.text = orderItem.count.toString()

        SessionManager.saveOrderItem(orderItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemsViewHolder {
        return OrderItemsViewHolder(
            OrderItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnPlusClicked {
        fun onClickListener(orderItem: OrderItem)
    }

    fun onPlusClick(onPlusClicked: OnPlusClicked) {
        this.onPlusClicked = onPlusClicked
    }

    interface OnMinusClick {
        fun onClickListener(orderItem: OrderItem, position: Int): Boolean
    }

    fun onMinusClick(onMinusClick: OnMinusClick) {
        this.onMinusClick = onMinusClick
    }

}
