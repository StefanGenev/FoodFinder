package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodfinder11.activities.MealActivity
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.pojo.Meal
import com.example.foodfinder11.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel

    private lateinit var randomMeal: Meal

    companion object {
        const val MEAL_ID = "com.example.foodfinder11.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodfinder11.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodfinder11.fragments.thumbMeal"
        const val MEAL_CATEGORY = "com.example.foodfinder11.fragments.categoryMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeMvvm.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()
        onDiscardClick()
        onDetailsClick()
    }

    private fun showCurrentMeal(){
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra(MEAL_ID, randomMeal.idMeal)
        intent.putExtra(MEAL_NAME, randomMeal.strMeal + " Shop")
        intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
        intent.putExtra(MEAL_CATEGORY, randomMeal.strCategory)
        startActivity(intent)
    }

    private fun observeRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner,
            { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)
                binding.tvMealName.text = meal.strMeal + " Shop"
                this.randomMeal = meal
            })
    }

    private fun changeMeal(){
        homeMvvm.getRandomMeal()
        observeRandomMeal()
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            showCurrentMeal()
        }
    }

    private fun onDiscardClick() {
        binding.discardButton.setOnClickListener {
            changeMeal()
        }
    }

    private fun onDetailsClick() {
        binding.detailsButton.setOnClickListener {
            showCurrentMeal()
        }
    }
}