package com.example.foodfinder11.activities


import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.foodfinder11.OrderItem
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.ActivityOrderItemBinding
import com.example.foodfinder11.model.Meal
import com.example.foodfinder11.model.PromotionTypes
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.utils.getParcelableExtraProvider

class OrderItemActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityOrderItemBinding

    private var meal: Meal = Meal()
    private var count: Int = 1

    companion object {
        const val MINIMUM_COUNT = 1
    }

    override fun initializeActivity() {

        binding = ActivityOrderItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initializeData() {

        val intent = intent
        meal = intent.getParcelableExtraProvider<Meal>(RestaurantActivity.MEAL)!!
    }

    override fun initializeViews() {

        binding.addButton.setOnClickListener {
            onAddTap()
        }

        binding.removeButton.setOnClickListener {
            onRemoveTap()
        }

        fillMealData()
    }

    override fun commitData(): Boolean {

        var orderItem = OrderItem(mealId = meal.id, count = count)
        SessionManager.saveOrderItem(orderItem)

        finish()
        return true
    }

    private fun fillMealData() {

        Glide.with(this@OrderItemActivity)
            .load(meal.imageUrl)
            .into(binding.mealPhoto)

        binding.mealName.text = meal.name
        binding.mealDescription.text = meal.description
        binding.mealPrice.text = "${String.format("%.2f", meal.price)} lv."

        if (meal.hasPromotion) {

            binding.oldMealPrice.text = "${String.format("%.2f", meal.price)} lv."
            binding.oldMealPrice.paintFlags = binding.oldMealPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            if (meal.promotionType == PromotionTypes.PERCENT) {

                binding.chipPromotion.text = "-${meal.promotionPercent}%"

            } else if (meal.promotionType == PromotionTypes.MANY_FOR_ONE) {

                binding.oldMealPrice.visibility = View.GONE;
                binding.chipPromotion.text = "${meal.additionalMealsCount + 1} for 1"
            }

        } else {
            binding.oldMealPrice.visibility = View.GONE;
            binding.chipPromotion.visibility = View.GONE;
        }

        updatePriceOnButton()
    }

    private fun onAddTap() {

        count += 1
        binding.count.text = count.toString()

        if ( count > MINIMUM_COUNT ) {

            binding.removeButton.foregroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.forest))
            binding.removeButton.isEnabled = true

        } else {

            binding.removeButton.isEnabled = false
            binding.removeButton.foregroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.grey))
        }

        updatePriceOnButton()
    }

    private fun onRemoveTap() {

        if ( count <= MINIMUM_COUNT ) {
            return
        }

        count -= 1
        binding.count.text = count.toString()

        updatePriceOnButton()
    }

    private fun updatePriceOnButton() {

        binding.continueButton.text = "Order for ${meal.getActualPrice(count)} lv."
    }
}