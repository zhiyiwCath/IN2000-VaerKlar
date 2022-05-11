package no.uio.g19.klesapp.data.model.nowcast

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Nowcast(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
) {
    class Deserializer : ResponseDeserializable<Nowcast> {
        override fun deserialize(content: String): Nowcast =
                Gson().fromJson(content, Nowcast::class.java)
    }
}