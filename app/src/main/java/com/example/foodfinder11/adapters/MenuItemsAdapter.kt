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
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.PromotionTypes


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

        val menuItem = differ.currentList[position]

        Glide.with(holder.itemView).load(menuItem.imageUrl).into(holder.binding.menuImage)

        holder.binding.mealName.text = menuItem.name
        holder.binding.mealDescription.text = menuItem.description
        holder.binding.price.text = "${String.format("%.2f", menuItem.getActualPrice())} lv."

        if (menuItem.hasPromotion) {

            holder.binding.oldPrice.text = "${String.format("%.2f", menuItem.price)} lv."
            holder.binding.oldPrice.paintFlags = holder.binding.oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (menuItem.promotionType == PromotionTypes.PERCENT) {

                holder.binding.chipPromotion.text = "-${menuItem.promotionPercent}%"

            } else if (menuItem.promotionType == PromotionTypes.MANY_FOR_ONE) {

                holder.binding.oldPrice.visibility = View.GONE;
                holder.binding.chipPromotion.text = "${menuItem.additionalMealsCount + 1} for 1"
            }

        } else {
            holder.binding.oldPrice.visibility = View.GONE;
            holder.binding.chipPromotion.visibility = View.GONE;
        }

        holder.binding.cardView.setOnClickListener{
            onItemClick.onClickListener(differ.currentList[position])
        }
    }

    fun onItemClicked(onItemClick: OnMenuItemClicked){
        this.onItemClick = onItemClick
    }

    interface OnMenuItemClicked {
        fun onClickListener(menuItem: Meal);
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
