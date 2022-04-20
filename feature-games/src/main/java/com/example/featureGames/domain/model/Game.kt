package com.example.featureGames.domain.model

import android.graphics.Bitmap
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureGames.data.models.*

data class Game(
    val id: Int,
    val name: String?,
    var released: String?,
    var backgroundImage: Bitmap?,
    var rating: Double = 0.0,
    var ratingTop: Int = 0,
    var ratings: ArrayList<Rating>?,
    var added: Int = 0,
    var addedByStatus: AddedByStatus?,
    var metacritic: Int = 0,
    var platformsInfo: ArrayList<PlatformInfo>?,
    var genres: ArrayList<Genre>?,
    var stores: ArrayList<StoreInfo>,
    var tags: ArrayList<Tag>?,
    var shortScreenshots: ArrayList<ShortScreenshot>?
) : BaseRecyclerViewType
