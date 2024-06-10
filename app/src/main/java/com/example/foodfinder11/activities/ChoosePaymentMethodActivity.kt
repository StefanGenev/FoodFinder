package com.example.foodfinder11.activities

import android.app.Activity
import android.content.Intent
import com.example.foodfinder11.databinding.ActivityChoosePaymentMethodBinding
import com.example.foodfinder11.fragments.CardNumberBottomSheetFragment
import com.example.foodfinder11.model.PaymentMethods
import com.example.foodfinder11.utils.SessionManager

interface CardNumberDialogActivityContract {

    fun onConfirmPromotion()
}

class ChoosePaymentMethodActivity : BaseNavigatableActivity(), CardNumberDialogActivityContract {

    private lateinit var binding: ActivityChoosePaymentMethodBinding
    private lateinit var cardNumberBottomSheetDialog: CardNumberBottomSheetFragment

    override fun initializeActivity() {

        binding = ActivityChoosePaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.cashLayout.setOnClickListener {

            var order = SessionManager.fetchOrder()
            order.paymentMethod = PaymentMethods.CASH
            SessionManager.saveOrder(order)
            setOKResult()
        }

        binding.cardLayout.setOnClickListener {
            openCardNumberBottomSheet()
        }
    }

    private fun setOKResult() {

        val data = Intent()
        setResult(Activity.RESULT_OK, data)

        finish()
    }

    private fun openCardNumberBottomSheet() {

        cardNumberBottomSheetDialog = CardNumberBottomSheetFragment()
        cardNumberBottomSheetDialog.show(supportFragmentManager, "BottomSheetDialog")
    }

    override fun onConfirmPromotion() {
        setOKResult()
        finish()
    }
}