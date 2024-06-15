package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ReviewCardBinding
import com.example.foodfinder11.model.Review
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.SessionManager

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>(){
    inner class ReviewsViewHolder(val binding: ReviewCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Review>() {

        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.ReviewsViewHolder {
        return ReviewsViewHolder(
            ReviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: ReviewsAdapter.ReviewsViewHolder, position: Int) {

        val review = differ.currentList[position]

        val userData = SessionManager.fetchUserData()

        if (userData.role == Roles.CUSTOMER) {

            holder.binding.title.text = review.restaurant.name
            Glide.with(holder.itemView).load(review.restaurant.imageUrl).into(holder.binding.image)

        } else {
            holder.binding.title.text = review.user.name
            holder.binding.image.setImageDrawable(

                ContextCompat.getDrawable(
                    holder.binding.image.context,
                    R.drawable.user
            ))
        }

        holder.binding.feedback.text = if (review.feedback.isEmpty()) holder.binding.feedback.context.getString(R.string.no_feedback_given) else review.feedback
        showRatingBar(holder, review.rating)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun showRatingBar(holder: ReviewsAdapter.ReviewsViewHolder, rating: Int) {

        holder.binding.ratingButton1.isChecked = rating >= 1
        holder.binding.ratingButton2.isChecked = rating >= 2
        holder.binding.ratingButton3.isChecked = rating >= 3
        holder.binding.ratingButton4.isChecked = rating >= 4
        holder.binding.ratingButton5.isChecked = rating >= 5
        holder.binding.ratingToggleGroup.isEnabled = false
    }

}