package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.HistoryItemCardBinding
import com.example.foodfinder11.databinding.ReviewCardBinding
import com.example.foodfinder11.pojo.Meal
import kotlin.random.Random
import kotlin.random.nextInt

class HistoryItemsAdapter : RecyclerView.Adapter<HistoryItemsAdapter.HistoryItemsViewHolder>(){
    inner class HistoryItemsViewHolder(val binding: HistoryItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemsAdapter.HistoryItemsViewHolder {
        return HistoryItemsViewHolder(
            HistoryItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: HistoryItemsAdapter.HistoryItemsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.binding.shopName.text = meal.strMeal + " Shop"
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgShop)
        holder.binding.tvPrice.text = "${String.format("%.2f", Random.nextDouble(10.0, 25.0))} lv."
        holder.binding.tvDate.text = "${Random.nextInt(1, 30)} May, ${Random.nextInt(0, 23)}:${Random.nextInt(10, 59)}"
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}