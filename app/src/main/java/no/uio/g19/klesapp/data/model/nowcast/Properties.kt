package no.uio.g19.klesapp.data.model.nowcast

data class Properties(
    val meta: Meta,
    val timeseries: List<Timeseries>
)