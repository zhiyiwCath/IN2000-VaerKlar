package no.uio.g19.klesapp.data.model.locationforecast

data class DetailsX(
    val air_temperature_max: Double,
    val air_temperature_min: Double,
    val precipitation_amount: Double,
    val precipitation_amount_max: Double,
    val precipitation_amount_min: Double,
    val probability_of_precipitation: Double,
    val probability_of_thunder: Double,
    val ultraviolet_index_clear_sky_max: Int
)