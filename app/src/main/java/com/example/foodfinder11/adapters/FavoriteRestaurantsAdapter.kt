package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.FavoriteRestaurantItemBinding
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.Restaurant

class FavoriteRestaurantsAdapter : RecyclerView.Adapter<FavoriteRestaurantsAdapter.FavoriteMealsAdapterViewHolder>() {

    private lateinit var onItemClick: OnFavoriteRestaurantItemClicked

    inner class FavoriteMealsAdapterViewHolder(val binding: FavoriteRestaurantItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealsAdapterViewHolder {
        return FavoriteMealsAdapterViewHolder(
            FavoriteRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealsAdapterViewHolder, position: Int) {

        val restaurant = differ.currentList[position]

        Glide.with(holder.itemView).load(restaurant.imageUrl).into(holder.binding.imgRestaurant)
        holder.binding.tvName.text = restaurant.name

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(differ.currentList[position])
        }
    }
    fun onItemClicked(onItemClick: OnFavoriteRestaurantItemClicked){
        this.onItemClick = onItemClick
    }

    interface OnFavoriteRestaurantItemClicked {
        fun onClickListener(restaurant: Restaurant);
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}


