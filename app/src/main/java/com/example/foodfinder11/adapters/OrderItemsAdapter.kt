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
    private lateinit var onPlusClicked: OrderItemsAdapter.OnPlusClicked
    private lateinit var onMinusClick: OrderItemsAdapter.OnMinusClick

    inner class OrderItemsViewHolder(val binding: OrderItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.mealId == newItem.mealId
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onBindViewHolder(holder: OrderItemsViewHolder, position: Int) {

        val meal = differ.currentList[position]
        //Glide.with(holder.itemView).load(meal.imageUrl).into(holder.binding.menuImage)
        //holder.binding.mealName.text = meal.strMealName
        holder.binding.count.text = meal.count.toString()

        holder.binding.addButton.setOnClickListener{
            onPlusClicked.onClickListener(meal)
            meal.count++
            holder.binding.count.text = meal.count.toString()
        }

        holder.binding.removeButton.setOnClickListener{
            meal.count--
            holder.binding.count.text = meal.count.toString()

            var isLast = false
            if ( meal.count <= 0 )
            {
                var list:MutableList <OrderItem> = differ.currentList.toMutableList()
                list.removeAt(holder.adapterPosition)
                differ.submitList(list)

                isLast = list.isEmpty()

            }

           onMinusClick.onClickListener(meal, isLast)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemsViewHolder {
        return OrderItemsViewHolder(
            OrderItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnPlusClicked {
        fun onClickListener(orderItem: OrderItem)
    }

    fun onPlusClick(onPlusClicked: OnPlusClicked)
    {
        this.onPlusClicked = onPlusClicked
    }

    interface OnMinusClick {
        fun onClickListener(orderItem: OrderItem, isLast: Boolean)
    }

    fun onMinusClick(onMinusClick: OnMinusClick)
    {
        this.onMinusClick = onMinusClick
    }

}
