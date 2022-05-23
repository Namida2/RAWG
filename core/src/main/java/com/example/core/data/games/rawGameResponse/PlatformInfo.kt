package com.example.core.data.games.rawGameResponse

import com.example.core.data.games.gameDetailsResponce.Platform
import com.google.gson.annotations.SerializedName

class PlatformInfo {
    var platform: Platform? = null
    @SerializedName("released_at")
    var releasedAt: String? = null
    @SerializedName("requirements_en")
    var requirementsRn: RequirementsEn? = null
    @SerializedName("requirements_ru")
    var requirementsRu: RequirementsRu? = null
}