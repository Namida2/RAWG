package com.example.featureGames.domain.tools

import com.example.featureGames.domain.model.GamesGetRequest
import com.example.featureGames.domain.model.GamesGetRequest.Builder.Companion.defaultTimeZone
import java.util.*

object TopPicksDameScreenSetting {
    private const val metacriticMin = 70
    private const val metacriticMax = 100
    private const val startDay = 1
    private const val startMonth = 0
    private const val yearDifferences = 3
    val defaultRequestForTopPicksScreen: GamesGetRequest =
        GamesGetRequest.Builder()
            .setDates(
                Calendar.getInstance().also { it.timeZone = defaultTimeZone },
                let {
                    Calendar.getInstance().apply {
                        this.set(
                            this.get(Calendar.YEAR) - yearDifferences,
                            startMonth, startDay
                        )
                        this.timeZone = defaultTimeZone
                    }
                }
            )
            .setMetacritic(metacriticMin, metacriticMax)
            .addOrdering(OrderedFields.METACRITIC.reverseOrder)
            .build()
}

object NewReleasesDameScreenSetting {
//    private val startDay = 1
//    private val startMonth = 1
//    private val yearDifferencesInTopPicksScconst
}
