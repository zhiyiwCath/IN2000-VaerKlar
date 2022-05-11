package no.uio.g19.klesapp.data.model.nowcast

data class Data(
    val instant: Instant,
    val next_1_hours: Next1Hours
)