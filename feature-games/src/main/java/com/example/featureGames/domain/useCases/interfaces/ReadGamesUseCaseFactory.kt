package com.example.featureGames.domain.useCases.interfaces

import com.example.core.domain.entities.tools.enums.GameScreenTags
import kotlinx.coroutines.CoroutineScope

interface ReadGamesUseCaseFactory {
    fun create(screenTag: GameScreenTags, coroutineScope: CoroutineScope): ReadGamesUseCase
}