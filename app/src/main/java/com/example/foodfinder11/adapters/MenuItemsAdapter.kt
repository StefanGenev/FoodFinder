package com.example.foodfinder11.adapters;

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.MenuItemCardBinding
import com.example.foodfinder11.model.MenuItem
import com.example.foodfinder11.model.PromotionTypes


class MenuItemsAdapter : RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>(){
    private lateinit var onItemClick: MenuItemsAdapter.OnMenuItemClicked

    inner class MenuItemsViewHolder(val binding: MenuItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.meal.id == newItem.meal.id
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemsViewHolder {
        return MenuItemsViewHolder(
            MenuItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: MenuItemsViewHolder, position: Int) {

        val menuItem = differ.currentList[position]

        Glide.with(holder.itemView).load(menuItem.meal.imageUrl).into(holder.binding.menuImage)

        holder.binding.mealName.text = menuItem.meal.name
        holder.binding.mealDescription.text = menuItem.meal.description
        holder.binding.price.text = "${String.format("%.2f", menuItem.meal.price)} lv."

        if (menuItem.hasPromotion) {

            holder.binding.oldPrice.paintFlags = holder.binding.oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (menuItem.promotionType == PromotionTypes.PERCENT) {

                holder.binding.chipPromotion.text = "-${menuItem.promotionPercent}%"

            } else if (menuItem.promotionType == PromotionTypes.MANY_FOR_ONE) {

                holder.binding.chipPromotion.text = "${menuItem.additionalMealsCount + 1} for 1%"
            }

            holder.binding.chipPromotion

        } else {
            holder.binding.oldPrice.visibility = View.GONE;
            holder.binding.chipPromotion.visibility = View.GONE;
        }

        holder.binding.actionButton.setOnClickListener{
            onItemClick.onClickListener(differ.currentList[position])
        }
    }

    fun onItemClicked(onItemClick: OnMenuItemClicked){
        this.onItemClick = onItemClick
    }

    interface OnMenuItemClicked {
        fun onClickListener(menuItem: MenuItem);
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
