package com.example.featureGames.domain.tools

import com.example.featureGames.domain.model.GamesGetRequest
import com.example.featureGames.domain.model.GamesGetRequest.Builder.Companion.defaultTimeZone
import java.util.*
interface GameScreensSettings {
    val request: GamesGetRequest
}
object TopPicksGameScreenSetting: GameScreensSettings {
    private const val metacriticMin = 70
    private const val metacriticMax = 100
    private const val startDay = 1
    private const val startMonth = 0
    private const val yearDifferences = 3
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

object AllGamesGameScreenSetting: GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}
