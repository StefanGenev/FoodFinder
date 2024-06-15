package com.example.foodfinder11.activities


import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.adapters.LanguagesAdapter
import com.example.foodfinder11.databinding.ActivityChangeLanguageBinding
import com.example.foodfinder11.utils.AppPreferences
import com.example.foodfinder11.dataObjects.Languages
import com.example.foodfinder11.utils.SessionManager

class ChangeLanguageActivity : BaseNavigatableActivity() {

    private lateinit var binding: ActivityChangeLanguageBinding

    private lateinit var languagesAdapter: LanguagesAdapter

    override fun initializeActivity() {

        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        // Applying OnClickListener to our Adapter
        languagesAdapter.setOnClickListener(object :

            LanguagesAdapter.OnClickListener {

            override fun onClick(language: Languages) {

                if (SessionManager.fetchLanguageLocale() != language.getLocale()) {
                    AppPreferences.language = language.getLocale()
                    returnOkIntent()
                }

                finish()
            }

        })
    }

    override fun initializeViews() {
        fillLanguages()
    }

    private fun prepareRecyclerView() {

        languagesAdapter = LanguagesAdapter()

        binding.rvLanguages.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = languagesAdapter
        }
    }

    private fun fillLanguages() {
        languagesAdapter.differ.submitList(Languages.values().toMutableList())
    }
}