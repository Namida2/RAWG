package com.example.featureFiltersDialog.domain.entities

import android.icu.util.Calendar

fun Long.toDateString(): String = run {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar[Calendar.YEAR].toString() + "." +
            calendar[Calendar.MONTH + 1].toString().addZeroToDate() + "." +
            calendar[Calendar.DATE].toString().addZeroToDate()
}

private fun String.addZeroToDate(): String =
    if (this.length == 1) "0$this" else this