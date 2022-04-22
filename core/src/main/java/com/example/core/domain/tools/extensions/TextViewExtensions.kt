package com.example.core.domain.tools.extensions

import android.widget.TextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun TextView.precomputeAndSetText(text: String?) {
    val contentText = text ?: ""
    val params: PrecomputedTextCompat.Params =
        TextViewCompat.getTextMetricsParams(this)
    CoroutineScope(Dispatchers.Unconfined).launch {
        val precomputedText =
            PrecomputedTextCompat.create(contentText, params)
        withContext(Dispatchers.Main) {
            TextViewCompat.setPrecomputedText(this@precomputeAndSetText, precomputedText)
        }
    }
}