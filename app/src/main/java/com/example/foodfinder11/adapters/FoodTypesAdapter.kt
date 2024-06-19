package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder11.databinding.FoodtypeItemBinding
import com.example.foodfinder11.model.FoodType

class FoodTypesAdapter : RecyclerView.Adapter<FoodTypesAdapter.FoodTypesViewHolder>(){

    private var onClickListener: OnClickListener? = null

    inner class FoodTypesViewHolder(val binding: FoodtypeItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<FoodType>() {
        override fun areItemsTheSame(oldItem: FoodType, newItem: FoodType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodType, newItem: FoodType): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypesAdapter.FoodTypesViewHolder {
        return FoodTypesViewHolder(
            FoodtypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: FoodTypesAdapter.FoodTypesViewHolder, position: Int) {

        val foodType = differ.currentList[position]
        holder.binding.tvName.text = foodType.getLocalName()

       holder.binding.container.setOnClickListener {
           if (onClickListener != null) {
               onClickListener!!.onClick(position, foodType)
           }
       }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: FoodType)
    }

}