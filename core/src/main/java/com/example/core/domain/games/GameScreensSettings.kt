package com.example.core.domain.games

import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.requests.GamesGetRequest.Builder.Companion.defaultTimeZone
import com.example.core.domain.entities.tools.constants.Constants.START_DAY
import com.example.core.domain.entities.tools.constants.Constants.START_MONTH
import com.example.core.domain.entities.tools.enums.OrderedFields
import com.example.core.domain.entities.tools.enums.reverseOrder
import java.util.*

interface GameScreensSettings {
    val request: GamesGetRequest
}

object TopPicksGameScreenSetting : GameScreensSettings {
    private const val metacriticMin = "70"
    private const val metacriticMax = "100"
    private const val yearDifferences = 3
    override val request: GamesGetRequest =
        GamesGetRequest.Builder()
            .setDates(
                Calendar.getInstance().also { it.timeZone = defaultTimeZone },
                let {
                    Calendar.getInstance().apply {
                        this.set(
                            this.get(Calendar.YEAR) - yearDifferences,
                            START_MONTH, START_DAY
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
    private const val yearDifferences = 5
    override val request: GamesGetRequest =
        GamesGetRequest.Builder()
            .setDates(
                let {
                    Calendar.getInstance().apply {
                        this.set(
                            this.get(Calendar.YEAR) + yearDifferences,
                            START_MONTH, START_DAY
                        )
                        this.timeZone = defaultTimeZone
                    }
                },
                let {
                    Calendar.getInstance().apply {
                        this.set(
                            this.get(Calendar.YEAR),
                            START_MONTH, START_DAY
                        )
                        this.timeZone = defaultTimeZone
                    }
                }
            )
            .addOrdering(OrderedFields.RATING.reverseOrder)
            .build()
}

object BestOfTheYearGameScreenSetting : GameScreensSettings {
    private const val yearDifferences = 1
    override val request: GamesGetRequest =
        GamesGetRequest.Builder()
            .setDates(
                let {
                    Calendar.getInstance().apply {
                        this.set(
                            this.get(Calendar.YEAR) + yearDifferences,
                            START_MONTH, START_DAY
                        )
                        this.timeZone = defaultTimeZone
                    }
                },
                let {
                    Calendar.getInstance().apply {
                        this.set(
                            this.get(Calendar.YEAR),
                            START_MONTH, START_DAY
                        )
                        this.timeZone = defaultTimeZone
                    }
                }
            )
            .addOrdering(OrderedFields.METACRITIC.reverseOrder)
            .build()
}

object AllGameScreenSetting : GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}

object MyLikesGameScreenSetting : GameScreensSettings {
    override val request: GamesGetRequest =
        GamesGetRequest.Builder().build()
}
