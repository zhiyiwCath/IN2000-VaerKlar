package no.uio.g19.klesapp.test

import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity

fun testStederStub() : List<PlaceRoomEntity> {
    return listOf(
            PlaceRoomEntity("Oslo", 59.9114f, 10.7579f),
            PlaceRoomEntity("Bergen", 60.3929f,5.3241f),
            PlaceRoomEntity("Trondheim", 63.4304f,10.3950f),
            PlaceRoomEntity("Kristiansand", 58.1467f,7.9956f),
            PlaceRoomEntity("Tromsø",69.6489f,18.9550f),
            PlaceRoomEntity("Alta",69.9688f,23.2716f)
    )

}
