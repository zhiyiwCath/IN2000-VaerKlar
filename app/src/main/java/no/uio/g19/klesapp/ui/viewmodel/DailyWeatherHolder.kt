package no.uio.g19.klesapp.ui.viewmodel

data class DailyWeatherHolder(
        val weatherSymbol: String,
        val day : String,
        val date : Int,
        val month : Int,
        val precipitation : Double,
        val precipitationProbability : Double,
        val tempMax : Double,
        val tempMin : Double
)
