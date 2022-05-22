package com.example.core.domain.entities.tools.extensions

fun isEmptyField(vararg strings: String): Boolean {
    strings.forEach {
        if (it.isEmpty()) return true
        if (it.replace(" ", "").isEmpty()) return true
    }
    return false
}