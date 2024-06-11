package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.HistoryItemCardBinding
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Order

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.HistoryItemsViewHolder>() {

    private lateinit var onItemClick: OrdersAdapter.OnItemClicked

    inner class HistoryItemsViewHolder(val binding: HistoryItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {

        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.HistoryItemsViewHolder {
        return HistoryItemsViewHolder(
            HistoryItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: OrdersAdapter.HistoryItemsViewHolder, position: Int) {

        val order = differ.currentList[position]

        holder.binding.name.text = order.restaurant.name
        Glide.with(holder.itemView).load(order.restaurant.imageUrl).into(holder.binding.image)

        var description = ""
        for (orderItem in order.orderItems) {

            description += "${orderItem.toString()} \n"
        }

        holder.binding.price.text = "${String.format("%.2f", order.getOrderPrice())} lv."

        holder.binding.cardView.setOnClickListener {
            onItemClick.onClickListener(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun onItemClicked(onItemClick: OnItemClicked) {
        this.onItemClick = onItemClick
    }

    interface OnItemClicked {
        fun onClickListener(order: Order);
    }

}