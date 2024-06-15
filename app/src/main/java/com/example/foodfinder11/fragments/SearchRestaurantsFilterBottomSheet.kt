package com.example.foodfinder11.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.foodfinder11.R
import com.example.foodfinder11.dataObjects.RestaurantsFilter
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout

class SearchRestaurantsFilterBottomSheet: BottomSheetDialogFragment() {

    private lateinit var searchRestaurantsFilterContract: SearchRestaurantsFilterContract

    private var filter: RestaurantsFilter = RestaurantsFilter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_restaurants_filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val navHostFragment: Fragment? = parentFragment
        val fragments = navHostFragment?.childFragmentManager?.fragments
        val searchRestaurantsFragment = fragments?.get(0)

        searchRestaurantsFilterContract = (searchRestaurantsFragment as SearchRestaurantsFilterContract)

        initButtons()
        initFilterFields()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog);
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun onConfirm() {

        dismiss()

        val foodTypesEditText = getView()?.findViewById<AutoCompleteTextView>(R.id.foodTypeTextEdit)
        filter.foodType = foodTypesEditText?.text.toString() ?: ""

        searchRestaurantsFilterContract.onApplyFilter(filter)
    }

    private fun onClearFilter() {
        dismiss()
        searchRestaurantsFilterContract.onClearFilter()
    }

    private fun setMenuEndIcon(text: String) {
        val menuLayout = getView()?.findViewById<TextInputLayout>(R.id.menu)
        menuLayout?.endIconMode = if (text.isEmpty()) TextInputLayout.END_ICON_DROPDOWN_MENU else TextInputLayout.END_ICON_CLEAR_TEXT
    }

    private fun initButtons() {

        val applyButton = getView()?.findViewById<Button>(R.id.applyButton)
        applyButton?.setOnClickListener {
            onConfirm()
        }

        val clearButton = getView()?.findViewById<Button>(R.id.clearFilterButton)
        clearButton?.setOnClickListener {
            onClearFilter()
        }
    }

    private fun initFilterFields() {

        filter = searchRestaurantsFilterContract.getFilter()

        initChips()
        initFoodTypesMenu()
    }

    private fun initFoodTypesMenu() {

        var items = searchRestaurantsFilterContract.getFoodTypes().map { it -> it.name }.toMutableList()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)

        val foodTypesEditText = getView()?.findViewById<AutoCompleteTextView>(R.id.foodTypeTextEdit)
        foodTypesEditText?.setAdapter(adapter)

        foodTypesEditText?.setText(filter.foodType)
        setMenuEndIcon(filter.foodType)

        foodTypesEditText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                setMenuEndIcon(s.toString())
            }
        })
    }

    private fun initChips() {

        val chipCheap = getView()?.findViewById<Chip>(R.id.chipCheap)
        chipCheap?.isChecked = filter.hasSelectedPriceRange && filter.priceRange == PriceRanges.CHEAP
        chipCheap?.setOnClickListener {

            if (filter.hasSelectedPriceRange && filter.priceRange == PriceRanges.CHEAP) {
                filter.hasSelectedPriceRange = false

            } else {
                filter.hasSelectedPriceRange = true
                filter.priceRange = PriceRanges.CHEAP
            }
        }

        val chipMedium = getView()?.findViewById<Chip>(R.id.chipMedium)
        chipMedium?.isChecked = filter.hasSelectedPriceRange && filter.priceRange == PriceRanges.MIDRANGE
        chipMedium?.setOnClickListener {

            if (filter.hasSelectedPriceRange && filter.priceRange == PriceRanges.MIDRANGE) {
                filter.hasSelectedPriceRange = false

            } else {
                filter.hasSelectedPriceRange = true
                filter.priceRange = PriceRanges.MIDRANGE
            }
        }

        val chipExpensive = getView()?.findViewById<Chip>(R.id.chipExpensive)
        chipExpensive?.isChecked = filter.hasSelectedPriceRange && filter.priceRange == PriceRanges.EXPENSIVE
        chipExpensive?.setOnClickListener {

            if (filter.hasSelectedPriceRange && filter.priceRange == PriceRanges.EXPENSIVE) {
                filter.hasSelectedPriceRange = false

            } else {
                filter.hasSelectedPriceRange = true
                filter.priceRange = PriceRanges.EXPENSIVE
            }

        }
    }

}