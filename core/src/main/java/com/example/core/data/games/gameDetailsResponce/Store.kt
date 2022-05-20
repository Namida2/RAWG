package com.example.core.data.games.gameDetailsResponce

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
class Store {
    @PrimaryKey
    var id: Int = 0
    var name: String? = null
    var slug: String? = null
    var url: String? = null
    @Ignore
    var store: StoreDetails? = null
}