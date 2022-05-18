package com.example.core.presentaton.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.core.R
import com.example.core.databinding.DialogClosedQuestionBinding

class ClosedQuestionDialog<A>(
    private val title: String? = null,
    private val message: String? = null,
    private val onNegative: () -> Unit = {},
    private val onPositive: (argument: A?) -> Unit
) : DialogFragment() {

    var argument: A? = null
    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogClosedQuestionBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context, R.style.alertDialogStyle)
        with(binding) {
            this@ClosedQuestionDialog.title?.let { title.text = it }
            this@ClosedQuestionDialog.message?.let { message.text = it }
            cancelButton.setOnClickListener {
                onNegative.invoke()
                dismiss()
            }
            acceptButton.setOnClickListener {
                onPositive.invoke(argument)
                dismiss()
            }
        }
        isCancelable = false
        return builder.setView(binding.root).create()
    }

//    private fun myDismiss() {
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(requireContext().resources.getInteger(R.integer.defaultDismissDialogTime).toLong())
//            dismiss()
//        }
//    }
}