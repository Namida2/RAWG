package com.example.core.data.imageLoaders.interfaces

interface ImageUrlInfo {
    val url: String
    val width: Int
    val height: Int
    val needToCenterCrop: Boolean
}