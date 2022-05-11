package no.uio.g19.klesapp.data.api


import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitObjectResponse
import no.uio.g19.klesapp.BuildConfig.MAPS_API_KEY
import no.uio.g19.klesapp.data.model.geocoder.GeocoderAddress
import no.uio.g19.klesapp.data.model.locationforecast.Locationforecast
import no.uio.g19.klesapp.data.model.nowcast.Nowcast
import no.uio.g19.klesapp.data.model.sunrise.Sunrise

/**
 * An implementation of the ApiInterface
 *
 * Documentation by Metereologisk institutt: in2000.met.no
 *
 * @see ApiInterface
 *
 * @property endpointNowcast Defines the target of the [Nowcast]-API calls
 * @property endpointLocationforecast Defines the target of the [Locationforecast]-API calls
 * @property endpointSunrise Defines the target of the [Sunrise]-API calls
 * @property endpointGeocoderAPI Defines the target of the Geocoder-API calls
 * @property header User-agent to allow the server to recognise and aggregate the apps API calls
 */
class ApiHandler : ApiInterface {
    private val endpointNowcast = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete?"
    private val endpointLocationforecast = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/complete?"
    private val endpointSunrise = "https://in2000-apiproxy.ifi.uio.no/weatherapi/sunrise/2.0/.json?"
    private val endpointGeocoderAPI = "https://maps.googleapis.com/maps/api/geocode/json?"
    private val header = "no.uio.g19.klesapp/1.0.0"


    /**
     * Fetches a Nowcast-response and returns a parsed version
     *
     * Uses a Deserializer defined in the [Nowcast]-dataclass to parse the JSON-response.
     *
     * @param lat target latitude for the forecast
     * @param lon target longitude for the forecast
     *
     * @return Parsed Nowcast-object
     */
    override suspend fun getNowcast(lat: Float, lon: Float): Nowcast =
            Fuel.get(endpointNowcast, listOf("lat" to lat.toString(), "lon" to lon.toString()))
                    .appendHeader(Pair(Headers.USER_AGENT, header))
                    .awaitObjectResponse(Nowcast.Deserializer())
                    .third
    /**
     * Fetches a Locationforecast-response and returns a parsed version
     *
     * Uses a Deserializer defined in the [Locationforecast]-dataclass to parse the JSON-response.
     *
     * @param lat target latitude for the forecast
     * @param lon target longitude for the forecast
     *
     * @return Parsed Locationforecast-object
     */
    override suspend fun getLocationforecast(lat: Float, lon: Float): Locationforecast =
            Fuel.get(endpointLocationforecast, listOf("lat" to lat.toString(), "lon" to lon.toString()))
                    .appendHeader(Pair(Headers.USER_AGENT, header))
                    .awaitObjectResponse(Locationforecast.Deserializer())
                    .third
    /**
     * Fetches a Sunrise-response and returns a parsed version
     *
     * Uses a Deserializer defined in the [Sunrise]-dataclass to parse the JSON-response.
     *
     * @param lat target latitude for the forecast
     * @param lon target longitude for the forecast
     *
     * @return Parsed Sunrise-object
     */
    override suspend fun getSunrise(lat: Float, lon: Float, date: String, offset: String): Sunrise =
            Fuel.get(endpointSunrise, listOf("lat" to lat.toString(), "lon" to lon.toString(), "date" to date, "offset" to offset))
                    .appendHeader(Pair(Headers.USER_AGENT, header))
                    .awaitObjectResponse(Sunrise.Deserializer())
                    .third

    /**
     * Fetches a Geocoder-response and returns a parsed version
     *
     * Uses a Deserializer defined in the [GeocoderAddress]-dataclass to parse the JSON-response.
     *
     * @param lat target latitude for the forecast
     * @param lon target longitude for the forecast
     *
     * @return Parsed GeocoderAddress-object
     */
    override suspend fun getAddress(lat : Double, lon : Double) : GeocoderAddress =
        Fuel.get(endpointGeocoderAPI, listOf("latlng" to "$lat,$lon", "key" to MAPS_API_KEY))
            .appendHeader(Pair(Headers.USER_AGENT, header))
            .awaitObjectResponse(GeocoderAddress.Deserializer())
            .third


}