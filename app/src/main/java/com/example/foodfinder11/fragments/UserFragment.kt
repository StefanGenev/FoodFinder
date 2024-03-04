package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder11.activities.HistoryActivity
import com.example.foodfinder11.activities.ReviewsActivity
import com.example.foodfinder11.databinding.FragmentUserBinding
import com.example.foodfinder11.viewModel.MainViewModel

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var homeMvvm: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onOrdersClick()
        onReviewsClick()
        onLogoutClick()
    }

    private fun onReviewsClick() {
        binding.reviewsButton.setOnClickListener {
            showReviews()
        }
    }

    private fun showReviews() {
        val intent = Intent(activity, ReviewsActivity::class.java)
        startActivity(intent)
    }

    private fun onOrdersClick() {
        binding.ordersButton.setOnClickListener {
            val intent = Intent(activity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onLogoutClick() {
        binding.logoutButton.setOnClickListener {
            activity?.finish()
        }
    }


}