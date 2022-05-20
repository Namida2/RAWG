package com.example.core.data.games.rawGameResponse

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "added_by_status")
class AddedByStatus {
    var gameId: Int = 0
    var yet = 0
    var owned = 0
    var beaten = 0
    var toplay = 0
    var dropped = 0
    var playing = 0
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}