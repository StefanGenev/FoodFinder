package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.ReviewCardBinding
import com.example.foodfinder11.model.Meal
import kotlin.random.Random

//TODO Change with reviews model
class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>(){
    inner class ReviewsViewHolder(val binding: ReviewCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.ReviewsViewHolder {
        return ReviewsViewHolder(
            ReviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: ReviewsAdapter.ReviewsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.binding.shopName.text = meal.name
        holder.binding.ratingbar.rating = Random.nextInt(1, 5).toFloat()
        holder.binding.tvReview.text = "A really nice place with affordable prices, but the service is not that great."
        Glide.with(holder.itemView).load(meal.image).into(holder.binding.imgShop)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}