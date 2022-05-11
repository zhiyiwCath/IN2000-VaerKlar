package no.uio.g19.klesapp.data.model.locationforecast

data class Properties(
    val meta: Meta,
    val timeseries: List<Timeseries>
)