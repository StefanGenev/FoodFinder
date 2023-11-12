package com.example.foodfinder11.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.OrderItemCardBinding
import com.example.foodfinder11.OrderItem


class OrderItemsAdapter : RecyclerView.Adapter<OrderItemsAdapter.OrderItemsViewHolder>(){

    inner class OrderItemsViewHolder(val binding: OrderItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onBindViewHolder(holder: OrderItemsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.menuImage)
        holder.binding.mealName.text = meal.strMealName
        holder.binding.count.text = meal.intMealCount.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemsViewHolder {
        return OrderItemsViewHolder(
            OrderItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
