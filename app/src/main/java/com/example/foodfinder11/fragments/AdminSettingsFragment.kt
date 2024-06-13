package com.example.foodfinder11.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.WelcomeActivity
import com.example.foodfinder11.databinding.FragmentAdminSettingsBinding
import com.example.foodfinder11.utils.SessionManager

class AdminSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAdminSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProfileName.text = "admin"

        binding.signOutButton.setOnClickListener {
            onSignOut()
        }
    }

    private fun onSignOut() {

        QuestionDialogFragment(getString(R.string.are_you_sure),
            getString(R.string.yes),
            getString(R.string.no),
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