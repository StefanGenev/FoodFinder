package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.MealItemBinding
import com.example.foodfinder11.model.Meal

class FavoriteRestaurantsAdapter : RecyclerView.Adapter<FavoriteRestaurantsAdapter.FavoriteMealsAdapterViewHolder>() {

    private lateinit var onItemClick: OnFavoriteMealItemClicked

    inner class FavoriteMealsAdapterViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealsAdapterViewHolder {
        return FavoriteMealsAdapterViewHolder(
            MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.image).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.name

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(differ.currentList[position])
        }
    }
    fun onItemClicked(onItemClick: OnFavoriteMealItemClicked){
        this.onItemClick = onItemClick
    }

    interface OnFavoriteMealItemClicked {
        fun onClickListener(meal: Meal);
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}


