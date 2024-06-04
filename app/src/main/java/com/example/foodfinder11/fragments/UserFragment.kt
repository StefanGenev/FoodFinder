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
import com.example.foodfinder11.activities.WelcomeActivity
import com.example.foodfinder11.databinding.FragmentSettingsBinding
import com.example.foodfinder11.databinding.FragmentUserBinding
import com.example.foodfinder11.utils.SessionManager
import com.example.foodfinder11.viewModel.MainViewModel

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val userData = SessionManager.fetchUserData()
        binding.tvProfileName.text = userData.name

        binding.signOutButton.setOnClickListener {
            onSignOut()
        }
    }

    private fun onSignOut() {

        QuestionDialogFragment("Are you sure?",
            "Yes",
            "No",
            onOkAction = { dialog, id ->

                activity?.finish()
                SessionManager.logoutOperations()

                val intent = Intent(activity, WelcomeActivity::class.java)
                startActivity(intent)
            }
            , onCancelAction = { dialog, id ->
            } ).show(parentFragmentManager, "QuestionDialog")
    }

}