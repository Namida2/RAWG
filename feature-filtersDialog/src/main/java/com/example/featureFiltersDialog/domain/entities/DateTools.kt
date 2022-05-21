package com.example.featureFiltersDialog.domain.entities

import android.icu.util.Calendar
import com.example.core.domain.entities.tools.constants.StringConstants.DASH_SIGN

fun Long.toDateString(separator: String = DASH_SIGN): String = run {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar[Calendar.YEAR].toString() + separator +
            (calendar[Calendar.MONTH] + 1).toString().addZeroToDate() + separator +
            calendar[Calendar.DATE].toString().addZeroToDate()
}

private fun String.addZeroToDate(): String =
    if (this.length == 1) "0$this" else this