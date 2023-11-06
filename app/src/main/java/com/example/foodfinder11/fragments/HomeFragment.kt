package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodfinder11.databinding.FragmentHomeBinding
import com.example.foodfinder11.pojo.Meal
import com.example.foodfinder11.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var homeMvvm:HomeViewModel

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

        homeMvvm.GetRandomMeal()
        ObserveRandomMeal()
    }

    private fun ObserveRandomMeal() {
        homeMvvm.ObserveRandomMealLiveData().observe(viewLifecycleOwner, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                Glide.with(this@HomeFragment)
                    .load(value!!)
                    .into(binding.imgRandomMeal)
            }

        })
    }
}