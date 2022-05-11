package no.uio.g19.klesapp.data.model.sunrise

data class Location(
    val height: String,
    val latitude: String,
    val longitude: String,
    val time: List<Time>
)