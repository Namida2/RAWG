package com.example.core.domain.interfaces

interface Mapper<T, R> {
    fun map(value: T): R
}