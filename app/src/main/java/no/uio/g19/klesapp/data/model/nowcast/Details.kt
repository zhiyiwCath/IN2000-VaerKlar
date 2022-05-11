package no.uio.g19.klesapp.data.model.nowcast

data class Details(
    val air_temperature: Double,
    val precipitation_rate: Double,
    val relative_humidity: Double,
    val wind_from_direction: Double,
    val wind_speed: Double,
    val wind_speed_of_gust: Double
)