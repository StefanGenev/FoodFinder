package com.example.foodfinder11.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class QuestionDialogFragment(

    val dialogTitle: String,
    val onOkTitle: String,
    val cancelTitle: String,
    val onOkAction: DialogInterface.OnClickListener,
    val onCancelAction: DialogInterface.OnClickListener

) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)

            builder.setMessage(dialogTitle)
                .setPositiveButton(onOkTitle, onOkAction)
                .setNegativeButton(cancelTitle, onCancelAction)

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}