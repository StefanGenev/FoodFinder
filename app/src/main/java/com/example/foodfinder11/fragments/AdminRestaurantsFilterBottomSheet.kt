package com.example.foodfinder11.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foodfinder11.R
import com.example.foodfinder11.model.PaymentMethods
import com.example.foodfinder11.model.RestaurantStatuses
import com.example.foodfinder11.utils.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText

class AdminRestaurantsFilterBottomSheet: BottomSheetDialogFragment() {

    private lateinit var adminRestaurantsFilterContract: AdminRestaurantsFilterContract
    private var allStatusesChosen: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_restaurants_filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val navHostFragment: Fragment? = parentFragment
        val fragments = navHostFragment?.childFragmentManager?.fragments
        val adminRestaurantsFragment = fragments?.get(0)

        adminRestaurantsFilterContract = (adminRestaurantsFragment as AdminRestaurantsFragment)

        val applyButton = getView()?.findViewById<Button>(R.id.applyButton)
        applyButton?.setOnClickListener {
            onConfirm()
        }

        val clearButton = getView()?.findViewById<Button>(R.id.clearFilterButton)
        clearButton?.setOnClickListener {
            onClearFilter()
        }

        val chipAll = getView()?.findViewById<Chip>(R.id.chipAll)
        chipAll?.setOnClickListener {
            allStatusesChosen = true
        }

        val chipRegistered = getView()?.findViewById<Chip>(R.id.chipRegistered)
        chipRegistered?.setOnClickListener {
            adminRestaurantsFilterContract.setRestaurantStatusFilter(RestaurantStatuses.REGISTERED)
            allStatusesChosen = false
        }

        val chipApproved = getView()?.findViewById<Chip>(R.id.chipApproved)
        chipApproved?.setOnClickListener {
            adminRestaurantsFilterContract.setRestaurantStatusFilter(RestaurantStatuses.APPROVED)
            allStatusesChosen = false
        }

        val chipHidden = getView()?.findViewById<Chip>(R.id.chipHidden)
        chipHidden?.setOnClickListener {
            adminRestaurantsFilterContract.setRestaurantStatusFilter(RestaurantStatuses.HIDDEN)
            allStatusesChosen = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog);

    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun onConfirm() {

        if (allStatusesChosen) {
            onClearFilter()
            return
        }

        dismiss()
        adminRestaurantsFilterContract.onApplyFilter()
    }

    private fun onClearFilter() {
        dismiss()
        adminRestaurantsFilterContract.onClearFilter()
    }


}