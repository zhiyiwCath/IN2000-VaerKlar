package no.uio.g19.klesapp.data.model.geocoder

data class Geometry(
    val bounds: Bounds,
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)