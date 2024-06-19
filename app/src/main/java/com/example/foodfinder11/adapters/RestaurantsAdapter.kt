package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.RestaurantCardBinding
import com.example.foodfinder11.model.Restaurant

class RestaurantsAdapter : RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>() {

    private lateinit var onItemClick: RestaurantsAdapter.OnItemClicked

    inner class RestaurantsViewHolder(val binding: RestaurantCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Restaurant>() {

        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsAdapter.RestaurantsViewHolder {
        return RestaurantsViewHolder(
            RestaurantCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: RestaurantsAdapter.RestaurantsViewHolder, position: Int) {

        val restaurant = differ.currentList[position]

        holder.binding.name.text = restaurant.name
        holder.binding.description.text = restaurant.foodType.getLocalName()
        holder.binding.priceRange.text = restaurant.priceRange.getName(holder.binding.priceRange.context)
        Glide.with(holder.itemView).load(restaurant.imageUrl).into(holder.binding.image)

        holder.binding.cardView.setOnClickListener {
            onItemClick.onClickListener(restaurant)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun onItemClicked(onItemClick: OnItemClicked) {
        this.onItemClick = onItemClick
    }

    interface OnItemClicked {
        fun onClickListener(restaurant: Restaurant);
    }

}