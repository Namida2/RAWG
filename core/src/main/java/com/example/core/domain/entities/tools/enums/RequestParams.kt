package com.example.core.domain.entities.tools.enums

// TODO: Protect the api key //STOPPED// 
enum class RequestParams(val myName: String, val slug: String) {
    PARAM_KEY("Key","key"),
    API_KEY("API_KEY","eb93975e53414766bd5b734fae1502eb"),
    RAWG_BASE_URL("RAWG_BASE_URL","https://api.rawg.io/api/"),

    SEARCH("Search","search"),
    DATES("Dates","dates"),
    PLATFORMS("Platforms", "platforms"),
    STORES("Stores", "stores"),
    GENRES ("Genres", "genres"),
    DEVELOPERS ("Developers", "developers"),
    PUBLISHERS ("Publishers", "publishers"),
    TAGS ("Tags", "tags"),
    ORDERING ("Ordering","ordering"),
    METACRITIC ("Metacritic", "metacritic"),
    PAGE_SIZE ("Page_size", "page_size"),
    PAGE ("Page", "page"),
    NAME ("Name", "name"),
    RELEASED ("Released", "released"),
    ADDED ("Added", "added"),
    CREATED ("Created", "created"),
    UPDATED ("Updated", "updated"),
    RATING ("Rating", "rating"),
}