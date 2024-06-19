package com.example.foodfinder11.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.AboutUsActivity
import com.example.foodfinder11.activities.ChangeLanguageActivity
import com.example.foodfinder11.activities.FoodTypesActivity
import com.example.foodfinder11.activities.WelcomeActivity
import com.example.foodfinder11.databinding.FragmentAdminSettingsBinding
import com.example.foodfinder11.utils.SessionManager

class AdminSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAdminSettingsBinding

    private val startLanguagesActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                activity?.recreate()
            }
        }

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

        binding.foodTypesButton.setOnClickListener {
            onFoodTypes()
        }

        binding.languageButton.setOnClickListener {
            onChangeLanguage()
        }

        binding.aboutUsButton.setOnClickListener {
            onAboutUs()
        }

        binding.signOutButton.setOnClickListener {
            onSignOut()
        }
    }

    private fun onFoodTypes() {
        val intent = Intent(activity, FoodTypesActivity::class.java)
        startActivity(intent)
    }

    private fun onChangeLanguage() {
        val intent = Intent(activity, ChangeLanguageActivity::class.java)
        startLanguagesActivityForResult.launch(intent)
    }

    private fun onAboutUs() {
        val intent = Intent(activity, AboutUsActivity::class.java)
        startActivity(intent)
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