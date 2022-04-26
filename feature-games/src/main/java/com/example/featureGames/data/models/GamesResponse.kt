package com.example.featureGames.data.models

import com.example.featureGames.domain.model.interfaces.Response
import com.google.gson.annotations.SerializedName

class GamesResponse : Response {
    var count: Int = 0
    var next: String? = null
    var previous: Any? = null

    @SerializedName("results")
    var rawGames: List<RAWGame>? = null

    @SerializedName("seo_title")
    var seoTitle: String? = null

    @SerializedName("seo_description")
    var seoDescription: String? = null

    @SerializedName("seo_keywords")
    var seoKeywords: String? = null

    @SerializedName("seo_h1")
    var seoH1: String? = null
    var noindex: Boolean = false
    var nofollow: Boolean = false
    var description: String? = null
    var filters: Filters? = null

    @SerializedName("nofollow_collections")
    var noFollowCollections: List<String>? = null
}

