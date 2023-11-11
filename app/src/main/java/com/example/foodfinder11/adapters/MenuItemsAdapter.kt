package com.example.foodfinder11.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.MenuItemCardBinding
import com.example.foodfinder11.databinding.OfferCardBinding;
import com.example.foodfinder11.pojo.Meal

class MenuItemsAdapter : RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>(){

    inner class MenuItemsViewHolder(val binding: MenuItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemsViewHolder {
        return MenuItemsViewHolder(
            MenuItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: MenuItemsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.menuImage)
        holder.binding.mealName.text = meal.strMeal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
