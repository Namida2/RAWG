package com.example.featureGameDetails.data

class AddedByStatus {
    var yet = 0
    var owned = 0
    var beaten = 0
    var dropped = 0
    var playing = 0
}

class Developer {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var games_count = 0
    var image_background: String? = null
}

class EsrbRating {
    var id = 0
    var name: String? = null
    var slug: String? = null
}

class Genre {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var games_count = 0
    var image_background: String? = null
}

class ParentPlatform {
    var platform: Platform? = null
}

class Platform {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var image: Any? = null
    var year_end: Any? = null
    var year_start: Any? = null
    var games_count = 0
    var image_background: String? = null
}

class Platform2 {
    var platform: Platform? = null
    var released_at: String? = null
    var requirements: Requirements? = null
}

class Publisher {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var games_count = 0
    var image_background: String? = null
}

class Rating {
    var id = 0
    var title: String? = null
    var count = 0
    var percent = 0.0
}

class Requirements {
    var minimum: String? = null
}

class Root {
    var id = 0
    var slug: String? = null
    var name: String? = null
    var name_original: String? = null
    var description: String? = null
    var metacritic: Any? = null
    var metacritic_platforms: ArrayList<Any>? = null
    var released: String? = null
    var tba = false
    var updated: String? = null
    var background_image: String? = null
    var background_image_additional: String? = null
    var website: String? = null
    var rating = 0.0
    var rating_top = 0
    var ratings: ArrayList<Rating>? = null
    var reactions: Any? = null
    var added = 0
    var added_by_status: AddedByStatus? = null
    var playtime = 0
    var screenshots_count = 0
    var movies_count = 0
    var creators_count = 0
    var achievements_count = 0
    var parent_achievements_count = 0
    var reddit_url: String? = null
    var reddit_name: String? = null
    var reddit_description: String? = null
    var reddit_logo: String? = null
    var reddit_count = 0
    var twitch_count = 0
    var youtube_count = 0
    var reviews_text_count = 0
    var ratings_count = 0
    var suggestions_count = 0
    var alternative_names: ArrayList<Any>? = null
    var metacritic_url: String? = null
    var parents_count = 0
    var additions_count = 0
    var game_series_count = 0
    var user_game: Any? = null
    var reviews_count = 0
    var saturated_color: String? = null
    var dominant_color: String? = null
    var parent_platforms: ArrayList<ParentPlatform>? = null
    var platforms: ArrayList<Platform>? = null
    var stores: ArrayList<Store>? = null
    var developers: ArrayList<Developer>? = null
    var genres: ArrayList<Genre>? = null
    var tags: ArrayList<Tag>? = null
    var publishers: ArrayList<Publisher>? = null
    var esrb_rating: EsrbRating? = null
    var clip: Any? = null
    var description_raw: String? = null
}

class Store {
    var id = 0
    var url: String? = null
    var store: Store? = null
}

class Store2 {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var domain: String? = null
    var games_count = 0
    var image_background: String? = null
}

class Tag {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var language: String? = null
    var games_count = 0
    var image_background: String? = null
}