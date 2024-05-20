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
            }
        }

        val buttonManyForOne: MaterialButton? = getView()?.findViewById<MaterialButton>(R.id.btnManyForOne)
        buttonManyForOne?.addOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {

                selectedPromotionType = PromotionTypes.MANY_FOR_ONE
                textFieldLayout?.hint = "Additional meals"
            }
        }

        if (promotionDialogActivityContract.getHasPromotion()) {

            buttonPercent?.isChecked = promotionDialogActivityContract.getPromotionType() == PromotionTypes.PERCENT
            buttonManyForOne?.isChecked = promotionDialogActivityContract.getPromotionType() == PromotionTypes.MANY_FOR_ONE

            val textFieldEditText: TextInputEditText? = getView()?.findViewById<TextInputEditText>(R.id.textFieldTextEdit)

            if ( promotionDialogActivityContract.getPromotionType() == PromotionTypes.PERCENT ) {

                textFieldLayout?.hint = "Percent"
                textFieldEditText?.setText(promotionDialogActivityContract.getPercent().toString())

            } else if ( promotionDialogActivityContract.getPromotionType() == PromotionTypes.MANY_FOR_ONE ) {

                textFieldLayout?.hint = "Additional meals"
                textFieldEditText?.setText(promotionDialogActivityContract.getAdditionalMeals().toString())
            }
        }
    }

    private fun onConfirm() {

        promotionDialogActivityContract.setHasPromotion(true)
        promotionDialogActivityContract.setPromotionType(selectedPromotionType)

        val textField: TextInputEditText? = getView()?.findViewById<TextInputEditText>(R.id.textFieldTextEdit)
        val value = textField?.text.toString().toInt()

        if ( selectedPromotionType == PromotionTypes.PERCENT ) {

            promotionDialogActivityContract.setPercent(value)

        } else if ( selectedPromotionType == PromotionTypes.MANY_FOR_ONE ) {

            promotionDialogActivityContract.setAdditionalMeals(value)
        }

        dismiss()
    }

    private fun onRemove() {

        QuestionDialogFragment("Are you sure?",
            "Yes",
            "No",
            onOkAction = { dialog, id ->

                promotionDialogActivityContract.setHasPromotion(false)
                dialog.dismiss()
            }
            , onCancelAction = { dialog, id ->

                dialog.dismiss()
            } ).show(parentFragmentManager, "QuestionDialog")
    }

}