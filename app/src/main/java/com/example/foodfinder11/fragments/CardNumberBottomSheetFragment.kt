package com.example.foodfinder11.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.foodfinder11.R
import com.example.foodfinder11.activities.CardNumberDialogActivityContract
import com.example.foodfinder11.activities.ChoosePaymentMethodActivity
import com.example.foodfinder11.model.PaymentMethods
import com.example.foodfinder11.utils.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class CardNumberBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var cardNumberDialogActivityContract: CardNumberDialogActivityContract

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_number_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        cardNumberDialogActivityContract = (activity as ChoosePaymentMethodActivity)

        val confirmButton = getView()?.findViewById<Button>(R.id.continueButton)
        confirmButton?.setOnClickListener {
            onConfirm()
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

        val textField: TextInputEditText? = getView()?.findViewById<TextInputEditText>(R.id.textFieldTextEdit)
        val value = textField?.text.toString()

        var order = SessionManager.fetchOrder()
        order.paymentMethod = PaymentMethods.CARD
        order.cardNumber = value
        SessionManager.saveOrder(order)

        dismiss()
        cardNumberDialogActivityContract.onConfirmPromotion()
    }

}