package no.uio.g19.klesapp.data.model.geocoder

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val plus_code: PlusCodeX,
    val types: List<String>
)