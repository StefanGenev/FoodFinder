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
import com.example.foodfinder11.model.FoodType
import com.example.foodfinder11.model.PriceRanges
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout

class SearchRestaurantsFilterBottomSheet: BottomSheetDialogFragment() {

    private lateinit var searchRestaurantsFilterContract: SearchRestaurantsFilterContract

    private var selectedPriceRange: PriceRanges = PriceRanges.CHEAP
    private var hasSelectedPriceRange: Boolean = false

    private var selectedFoodType: FoodType = FoodType()


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

        val chipCheap = getView()?.findViewById<Chip>(R.id.chipCheap)
        chipCheap?.setOnClickListener {

            if (hasSelectedPriceRange && selectedPriceRange == PriceRanges.CHEAP) {
                hasSelectedPriceRange = false

            } else {
                selectedPriceRange = PriceRanges.CHEAP
            }
        }

        val chipMedium = getView()?.findViewById<Chip>(R.id.chipMedium)
        chipMedium?.setOnClickListener {

            if (hasSelectedPriceRange && selectedPriceRange == PriceRanges.MIDRANGE) {
                hasSelectedPriceRange = false

            } else {
                selectedPriceRange = PriceRanges.MIDRANGE
            }
        }

        val chipExpensive = getView()?.findViewById<Chip>(R.id.chipExpensive)
        chipExpensive?.setOnClickListener {

            if (hasSelectedPriceRange && selectedPriceRange == PriceRanges.EXPENSIVE) {
                hasSelectedPriceRange = false

            } else {
                selectedPriceRange = PriceRanges.EXPENSIVE
            }

        }

        var items = searchRestaurantsFilterContract.getFoodTypes().map { it -> it.name }.toMutableList()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)

        val foodTypesEditText = getView()?.findViewById<AutoCompleteTextView>(R.id.foodTypeTextEdit)
        foodTypesEditText?.setAdapter(adapter)
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
                val menuLayout = getView()?.findViewById<TextInputLayout>(R.id.menu)
                menuLayout?.endIconMode = if (s.isEmpty()) TextInputLayout.END_ICON_DROPDOWN_MENU else TextInputLayout.END_ICON_CLEAR_TEXT
            }
        })
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

        var filter = SearchRestaurantsFragment.Filter()

        val foodTypesEditText = getView()?.findViewById<AutoCompleteTextView>(R.id.foodTypeTextEdit)
        filter.foodType = foodTypesEditText?.text.toString() ?: ""
        filter.hasSelectedPriceRange = hasSelectedPriceRange
        filter.priceRange = selectedPriceRange

        searchRestaurantsFilterContract.onApplyFilter(filter)
    }

    private fun onClearFilter() {
        dismiss()
        searchRestaurantsFilterContract.onClearFilter()
    }

}