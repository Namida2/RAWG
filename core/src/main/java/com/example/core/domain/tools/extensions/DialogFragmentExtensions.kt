package com.example.core.domain.tools.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.showIfNotAdded(fragmentManager: FragmentManager, tag: String) {
    val onAlreadyAddedMessage = "already added."
    if(!this.isAdded) this.show(fragmentManager, tag)
    else logD("$this: $onAlreadyAddedMessage")
}

fun DialogFragment.dismissIfAdded() {
    if (this.isAdded) this.dismiss()
}