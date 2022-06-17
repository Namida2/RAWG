package com.example.core.domain.entities.tools.enums

import com.example.core.R

enum class PlatformsEnum(val platformName: String, val slug: String, val iconId: Int) {
    WINDOWS("PC","pc", R.drawable.ic_pc),
    PLAYSTATION5("Playstation 5","playstation5", R.drawable.ic_pc),
    PLAYSTATION4("Playstation 5","playstation4", R.drawable.ic_playstation),
    PLAYSTATION3("Playstation 5","playstation3", R.drawable.ic_playstation),
    PLAYSTATION2("Playstation 5","playstation2", R.drawable.ic_playstation),
    PLAYSTATION("Playstation","playstation", R.drawable.ic_playstation),
    XBOX_ONE("Xbox One","xbox-one", R.drawable.ic_xbox),
    XBOX_360("Xbox 360","xbox360", R.drawable.ic_xbox),
    XBOX("Xbox","xbox-old", R.drawable.ic_xbox),
    XBOX_SERIES_X("Xbox Series S/X","xbox-series-x", R.drawable.ic_xbox),
    NINTENDO_SWITCH("Nintendo Switch","nintendo-switch", R.drawable.ic_nintendo_switch),
    IOS("iOS","ios", R.drawable.ic_ios),
    ANDROID("Android","android", R.drawable.ic_android),;
}