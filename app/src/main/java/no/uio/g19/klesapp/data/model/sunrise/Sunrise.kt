package no.uio.g19.klesapp.data.model.sunrise

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Sunrise(
    val location: Location,
    val meta: Meta
) {
    class Deserializer : ResponseDeserializable<Sunrise> {
        override fun deserialize(content: String) : Sunrise =
                Gson().fromJson(content, Sunrise::class.java)
    }
}