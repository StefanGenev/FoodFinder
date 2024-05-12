package com.example.foodfinder11.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.MenuItemCardBinding
import com.example.foodfinder11.model.Meal

class MenuItemsAdapter : RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>(){
    private lateinit var onItemClick: MenuItemsAdapter.OnMenuItemClicked

    inner class MenuItemsViewHolder(val binding: MenuItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
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
        Glide.with(holder.itemView).load(meal.imageUrl).into(holder.binding.menuImage)
        holder.binding.mealName.text = meal.name
        holder.binding.mealDescription.text = meal.description
        holder.binding.price.text = "${meal.price} lv."

        holder.binding.addButton.setOnClickListener{
            onItemClick.onClickListener(differ.currentList[position])
        }
    }

    fun onItemClicked(onItemClick: OnMenuItemClicked){
        this.onItemClick = onItemClick
    }

    interface OnMenuItemClicked {
        fun onClickListener(meal: Meal);
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
