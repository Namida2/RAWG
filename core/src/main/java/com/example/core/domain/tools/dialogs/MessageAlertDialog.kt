package com.example.core.domain.tools.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.core.R
import com.example.core.databinding.DialogMessageBinding
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MessageAlertDialog(
    private val title: String,
    private val message: String,
    private val action: (() -> Unit)? = null,
) : DialogFragment() {

    companion object {
        private const val dialogAlreadyExistsMessage = "Dialog already exists."
        val isExist: AtomicBoolean = AtomicBoolean(false)
        fun getNewInstance(
            title: String,
            message: String,
            action: (() -> Unit)? = null,
        ): MessageAlertDialog? {
            if (isExist.get()) {
                logD("$this: $dialogAlreadyExistsMessage")
                return null
            }
            return MessageAlertDialog(title, message, action)
        }
    }
    override fun onAttach(context: Context) {
        isExist.set(true)
        super.onAttach(context)
    }

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            logE("$this: coroutineContext: $coroutineContext, throwable: ${throwable.message}")
        }

    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogMessageBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext(), R.style.alertDialogStyle)
        with(binding) {
            titleTextView.text = title
            messageTextView.text = message
            actionButton.setOnClickListener {
                action?.let { notNullAction -> notNullAction() }
                CoroutineScope(Main).launch(coroutineExceptionHandler) {
                    delay(requireContext().resources.getInteger(R.integer.dismissDialogTime).toLong())
                    dismiss()
                }
            }
        }
        isCancelable = false
        return builder.setView(binding.root).create()
    }

    override fun onDetach() {
        super.onDetach()
        isExist.set(false)
    }
}

