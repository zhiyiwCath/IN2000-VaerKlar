package no.uio.g19.klesapp.data.model.geocoder

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class GeocoderAddress(
    val plus_code: PlusCode,
    val results: List<Result>,
    val status: String
){
    class Deserializer : ResponseDeserializable<GeocoderAddress> {
        override fun deserialize(content: String) : GeocoderAddress =
            Gson().fromJson(content, GeocoderAddress::class.java)
    }
}