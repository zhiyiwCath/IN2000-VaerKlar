package no.uio.g19.klesapp.data.model.placesDB.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//Source: https://developer.android.com/training/data-storage/room

//Our only table and object that holds place information
@Entity(tableName = "Places")
data class PlaceRoomEntity (
    @PrimaryKey
    val name: String,
    val lat: Float,
    val lon: Float,
)
