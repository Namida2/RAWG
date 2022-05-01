package com.example.core.domain.tools.constants

import com.example.core.R
import com.example.core.domain.entities.Message

object Messages {
    val defaultErrorMessage = Message(
        R.string.defaultTitle,
        R.string.defaultMessage
    )
    val checkNetworkConnectionMessage = Message(
        R.string.defaultTitle,
        R.string.networkConnectionMessage
    )
    val allGamesHaveBeenLoadedMessage = Message(
        R.string.pageNotFoundTitle,
        R.string.pageNotFoundMessage
    )
    val badGatewayMessage = Message(
        R.string.pageNotFoundTitle,
        R.string.pageNotFoundMessage
    )
}