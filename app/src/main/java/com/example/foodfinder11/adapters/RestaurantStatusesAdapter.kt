package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder11.databinding.RestaurantStatusItemBinding
import com.example.foodfinder11.model.RestaurantStatuses

class RestaurantStatusesAdapter: RecyclerView.Adapter<RestaurantStatusesAdapter.RestaurantStatusesViewHolder>(){

    private var onClickListener: OnClickListener? = null

    inner class RestaurantStatusesViewHolder(val binding: RestaurantStatusItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<RestaurantStatuses>() {

        override fun areItemsTheSame(oldItem: RestaurantStatuses, newItem: RestaurantStatuses): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RestaurantStatuses, newItem: RestaurantStatuses): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantStatusesAdapter.RestaurantStatusesViewHolder {
        return RestaurantStatusesViewHolder(
            RestaurantStatusItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: RestaurantStatusesAdapter.RestaurantStatusesViewHolder, position: Int) {

        val status = differ.currentList[position]
        holder.binding.name.text = status.getName()

        holder.binding.container.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(status)
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
        fun onClick(status: RestaurantStatuses)
    }

}