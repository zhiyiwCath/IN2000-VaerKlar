package no.uio.g19.klesapp.data.model.sunrise

data class Time(
    val date: String,
    val high_moon: HighMoon,
    val low_moon: LowMoon,
    val moonphase: Moonphase,
    val moonposition: Moonposition,
    val moonrise: Moonrise,
    val moonset: Moonset,
    val moonshadow: Moonshadow,
    val solarmidnight: Solarmidnight,
    val solarnoon: Solarnoon,
    val sunrise: SunriseX,
    val sunset: Sunset
)