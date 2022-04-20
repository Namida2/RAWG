package com.example.featureGames.data.models

import com.google.gson.annotations.SerializedName

class Root {
    var count = 0
    var next: String? = null
    var previous: Any? = null
    var responseGames: ArrayList<ResponseGame>? = null
    @SerializedName("seo_title")
    var seoTitle: String? = null
    @SerializedName("seo_description")
    var seoDescription: String? = null
    @SerializedName("seo_keywords")
    var seoKeywords: String? = null
    var seoH1: String? = null
    var noindex = false
    var nofollow = false
    var description: String? = null
    var filters: Filters? = null
    @SerializedName("nofollow_collections")
    var noFollowCollections: ArrayList<String>? = null
}