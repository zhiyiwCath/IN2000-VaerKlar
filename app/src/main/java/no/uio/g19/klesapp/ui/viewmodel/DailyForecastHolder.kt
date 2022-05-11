package no.uio.g19.klesapp.ui.viewmodel

data class DailyForecastHolder(
    val minTemp : Double,
    val maxTemp : Double,
    val effTemp : Double,
    val precipitation : Double,
    val humidity : Double,
    val wind : Double,
    val windDir : String
    )
