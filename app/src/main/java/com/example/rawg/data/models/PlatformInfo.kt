package com.example.rawg.data.models

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