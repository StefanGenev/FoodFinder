package com.example.foodfinder11.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder11.databinding.OfferCardBinding;
import com.example.foodfinder11.pojo.Meal
import kotlin.random.Random
import kotlin.random.nextInt

class OffersAdapter : RecyclerView.Adapter<OffersAdapter.OfferViewHolder>(){

        inner class OfferViewHolder(val binding:OfferCardBinding) : RecyclerView.ViewHolder(binding.root)

        private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
                override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                        return oldItem.idMeal == newItem.idMeal
                }

                override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                        return oldItem == newItem
                }
        }

        val differ = AsyncListDiffer(this, diffUtil)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
                return OfferViewHolder(
                        OfferCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
        }

        override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
                val meal = differ.currentList[position]
                holder.binding.tvOffer.text = "${Random.nextInt(15, 50)} off on all " + meal.strMeal
        }

        override fun getItemCount(): Int {
                return differ.currentList.size
        }

}
