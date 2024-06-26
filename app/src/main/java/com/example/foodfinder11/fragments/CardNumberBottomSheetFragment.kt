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
import com.google.android.material.textfield.TextInputLayout

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

        val textField: TextInputEditText? = getView()?.findViewById<TextInputEditText>(R.id.textFieldTextEdit)
        //TODO: textField?.setText(SessionManager.fetchOrder().cardNumber)
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

        if (!validateCardNumber(value)) {
            return
        }


        var order = SessionManager.fetchOrder()
        order.paymentMethod = PaymentMethods.CARD
        //TODO: order.cardNumber = value
        SessionManager.saveOrder(order)

        dismiss()
        cardNumberDialogActivityContract.onConfirmPromotion()
    }

    private fun validateCardNumber(value: String): Boolean {

        if (value.isEmpty() || !isValidCardNumber(value)) {

            val textInputLayout: TextInputLayout? = getView()?.findViewById<TextInputLayout>(R.id.textFieldTextLayout)
            textInputLayout?.error = getString(R.string.invalid_card_number)
            textInputLayout?.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            textInputLayout?.invalidate()

            return false
        }

        return true
    }

    fun isValidCardNumber(cardNumber: String): Boolean {
        var s1 = 0
        var s2 = 0
        val reverse = StringBuffer(cardNumber).reverse().toString()
        for (i in reverse.indices) {
            val digit = Character.digit(reverse[i], 10)
            when {
                i % 2 == 0 -> s1 += digit
                else -> {
                    s2 += 2 * digit
                    when {
                        digit >= 5 -> s2 -= 9
                    }
                }
            }
        }
        return (s1 + s2) % 10 == 0
    }

}