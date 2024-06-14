package com.example.foodfinder11.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.MealInfoActivity
import com.example.foodfinder11.activities.PromotionDialogActivityContract
import com.example.foodfinder11.model.PromotionTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class PromotionBottomSheetFragment : BottomSheetDialogFragment() {

    private var selectedPromotionType: PromotionTypes = PromotionTypes.PERCENT
    private lateinit var promotionDialogActivityContract: PromotionDialogActivityContract

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.promotion_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        promotionDialogActivityContract = (activity as MealInfoActivity)

        initToggleButtons()

        val confirmButton = getView()?.findViewById<Button>(R.id.continueButton)
        confirmButton?.setOnClickListener {
            onConfirm()
        }

        val removeButton = getView()?.findViewById<Button>(R.id.removeButton)

        if (!promotionDialogActivityContract.getHasPromotion()) {

            removeButton?.visibility = View.GONE
        }

        removeButton?.setOnClickListener {
            onRemove()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog);

    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun initToggleButtons() {

        val textFieldLayout: TextInputLayout? = getView()?.findViewById<TextInputLayout>(R.id.textFieldTextLayout)
        textFieldLayout?.hint = "Percent"

        val buttonPercent: MaterialButton? = getView()?.findViewById<MaterialButton>(R.id.btnPercent)
        buttonPercent?.addOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {

                selectedPromotionType = PromotionTypes.PERCENT
                textFieldLayout?.hint = "Percent"
                textFieldLayout?.visibility = View.VISIBLE
            }
        }

        val buttonManyForOne: MaterialButton? = getView()?.findViewById<MaterialButton>(R.id.btnManyForOne)
        buttonManyForOne?.addOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {

                selectedPromotionType = PromotionTypes.TWO_FOR_ONE
                textFieldLayout?.visibility = View.GONE
            }
        }

        if (promotionDialogActivityContract.getHasPromotion()) {

            buttonPercent?.isChecked = promotionDialogActivityContract.getPromotionType() == PromotionTypes.PERCENT
            buttonManyForOne?.isChecked = promotionDialogActivityContract.getPromotionType() == PromotionTypes.TWO_FOR_ONE

            val textFieldEditText: TextInputEditText? = getView()?.findViewById<TextInputEditText>(R.id.textFieldTextEdit)

            if ( promotionDialogActivityContract.getPromotionType() == PromotionTypes.PERCENT ) {

                textFieldLayout?.hint = "Percent"
                textFieldLayout?.visibility = View.VISIBLE

                textFieldEditText?.setText(promotionDialogActivityContract.getPercent().toString())

            } else if ( promotionDialogActivityContract.getPromotionType() == PromotionTypes.TWO_FOR_ONE ) {

                textFieldLayout?.visibility = View.GONE
            }
        }
    }

    private fun onConfirm() {

        promotionDialogActivityContract.setHasPromotion(true)
        promotionDialogActivityContract.setPromotionType(selectedPromotionType)

        val textField: TextInputEditText? = getView()?.findViewById<TextInputEditText>(R.id.textFieldTextEdit)

        if ( selectedPromotionType == PromotionTypes.PERCENT ) {

            val value = textField?.text.toString()
            if (!validatePercent(value))
                return

            promotionDialogActivityContract.setPercent(value.toInt())
        }

        promotionDialogActivityContract.onConfirmPromotion()
        dismiss()
    }

    private fun validatePercent(percent: String) : Boolean {

        val textInputLayout: TextInputLayout? = getView()?.findViewById<TextInputLayout>(R.id.textFieldTextLayout)

        textInputLayout?.error = ""

        if (percent.isEmpty() || percent.toInt() <= 0.0)
        {
            textInputLayout?.error = getString(R.string.invalid_percent)
            textInputLayout?.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            textInputLayout?.invalidate()

            return false
        }

        return true
    }

    private fun onRemove() {

        QuestionDialogFragment(getString(R.string.are_you_sure),
            getString(R.string.yes),
            getString(R.string.no),
            onOkAction = { dialog, id ->

                promotionDialogActivityContract.setHasPromotion(false)
                promotionDialogActivityContract.onRemovePromotion()
                dismiss()
            }
            , onCancelAction = { dialog, id ->

                dismiss()
            } ).show(parentFragmentManager, "QuestionDialog")

    }

}