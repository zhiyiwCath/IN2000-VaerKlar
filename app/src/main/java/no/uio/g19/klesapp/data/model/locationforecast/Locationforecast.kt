package no.uio.g19.klesapp.data.model.locationforecast

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Locationforecast(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
) {
    class Deserializer : ResponseDeserializable<Locationforecast> {
        override fun deserialize(content: String): Locationforecast =
                Gson().fromJson(content, Locationforecast::class.java)
    }
}