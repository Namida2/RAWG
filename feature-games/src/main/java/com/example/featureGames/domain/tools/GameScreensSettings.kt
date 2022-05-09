package com.example.featureGames.domain.tools

import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.requests.GamesGetRequest.Builder.Companion.defaultTimeZone
import com.example.core.domain.tools.enums.OrderedFields
import com.example.core.domain.tools.enums.reverseOrder
import java.util.*

interface GameScreensSettings {
    val request: GamesGetRequest
}

object TopPicksGameScreenSetting : GameScreensSettings {
    private const val metacriticMin = "85"
    private const val metacriticMax = "100"
    private const val startDay = 1
    private const val startMonth = 0
    private const val yearDifferences = 2
    override val request: GamesGetRequest =
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

object NewReleasesGameScreenSetting : GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}

object BestOfTheYearGameScreenSetting : GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}

object AllGameScreenSetting : GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}

object MyLikesGameScreenSetting : GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}
