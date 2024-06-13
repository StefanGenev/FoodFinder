package com.example.foodfinder11.adapters;

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.MenuItemCardBinding
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.OrderItem
import com.example.foodfinder11.model.PromotionTypes
import com.example.foodfinder11.model.Roles
import com.example.foodfinder11.utils.SessionManager


class MenuItemsAdapter : RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>() {

    private lateinit var onItemClick: MenuItemsAdapter.OnMenuItemClicked
    private lateinit var onPlusClick: MenuItemsAdapter.OnPlusClicked
    private lateinit var onMinusClick: MenuItemsAdapter.OnMinusClicked

    inner class MenuItemsViewHolder(val binding: MenuItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)

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
            MenuItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MenuItemsViewHolder, position: Int) {

        val menuItem = differ.currentList[position]

        Glide.with(holder.itemView).load(menuItem.imageUrl).into(holder.binding.menuImage)

        holder.binding.mealName.text = menuItem.name
        holder.binding.mealDescription.text = menuItem.description
        holder.binding.price.text = "${String.format("%.2f", menuItem.getActualPrice())} " +
                "${holder.binding.price.context.getString(
            R.string.lev)}"

        if (menuItem.hasPromotion) {

            holder.binding.oldPrice.text = "${String.format("%.2f", menuItem.price)} lv."
            holder.binding.oldPrice.paintFlags =
                holder.binding.oldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (menuItem.promotionType == PromotionTypes.PERCENT) {

                holder.binding.chipPromotion.text = "-${menuItem.promotionPercent}%"

            } else if (menuItem.promotionType == PromotionTypes.TWO_FOR_ONE) {

                holder.binding.oldPrice.visibility = View.GONE;
                holder.binding.chipPromotion.text = "2 for 1"
            }

        } else {
            holder.binding.oldPrice.visibility = View.GONE;
            holder.binding.chipPromotion.visibility = View.GONE;
        }

        holder.binding.cardView.setOnClickListener {
            onItemClick.onClickListener(differ.currentList[position])
        }

        initOrderData(holder, menuItem, position)
    }

    private fun initOrderData(holder: MenuItemsViewHolder, menuItem: Meal, position: Int) {

        val user = SessionManager.fetchUserData()

        if (user.role == Roles.CUSTOMER) {

            val orderItem = SessionManager.fetchOrderByItemMealId(menuItem.id)

            if (orderItem != null) {

                fillOrderData(holder, orderItem)

                holder.binding.removeButton.setOnClickListener {
                    updateOrderCount(holder, menuItem, -1, position)
                    onMinusClick.onClickListener(menuItem)
                }

                holder.binding.addButton.setOnClickListener {
                    updateOrderCount(holder, menuItem, 1, position)
                    onPlusClick.onClickListener(menuItem)
                }
            }
        }
    }

    private fun fillOrderData(holder: MenuItemsViewHolder, orderItem: OrderItem) {
        holder.binding.orderInfoLayout.visibility = if (orderItem.count > 0) View.VISIBLE else View.GONE
        holder.binding.itemCount.text = "${orderItem.count}x"
        holder.binding.orderPrice.text ="${String.format("%.2f", orderItem.meal.getActualPrice(orderItem.count))} lv."
    }

    private fun updateOrderCount(holder: MenuItemsViewHolder, menuItem: Meal, addToCount: Int, position: Int) {

        var orderItem = SessionManager.fetchOrderByItemMealId(menuItem.id)!!
        orderItem.count += addToCount

        if (orderItem.count <= 0) {
            holder.binding.orderInfoLayout.visibility = View.GONE
        }

        fillOrderData(holder, orderItem)
        SessionManager.saveOrderItem(orderItem)
    }

    fun onItemClicked(onItemClick: OnMenuItemClicked) {
        this.onItemClick = onItemClick
    }

    interface OnMenuItemClicked {
        fun onClickListener(menuItem: Meal);
    }

    fun onPlusClicked(onPlusClick: OnPlusClicked) {
        this.onPlusClick = onPlusClick
    }

    interface OnPlusClicked {
        fun onClickListener(menuItem: Meal);
    }

    fun onMinusClicked(onMinusClick: OnMinusClicked) {
        this.onMinusClick = onMinusClick
    }

    interface OnMinusClicked {
        fun onClickListener(menuItem: Meal);
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
