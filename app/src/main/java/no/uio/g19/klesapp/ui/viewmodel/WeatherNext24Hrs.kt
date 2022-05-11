package no.uio.g19.klesapp.ui.viewmodel

//Burde lage en klasse som gjoer det enklere med testing
data class WeatherNext24Hrs(
    val weatherList: MutableList<HourForcastHolder>
)