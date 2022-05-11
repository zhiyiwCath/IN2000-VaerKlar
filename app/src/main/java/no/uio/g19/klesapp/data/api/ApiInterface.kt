package no.uio.g19.klesapp.data.api

import no.uio.g19.klesapp.data.model.geocoder.GeocoderAddress
import no.uio.g19.klesapp.data.model.locationforecast.Locationforecast
import no.uio.g19.klesapp.data.model.nowcast.Nowcast
import no.uio.g19.klesapp.data.model.sunrise.Sunrise

/**
 * Interface for Api-calls
 */
interface ApiInterface {
    /**
     * Abstract function for the Nowcast-API by met
     *
     * @param lat The latitude, rounded to at most 4 digits
     * @param lon The longitude, rounded to at most 4 digits
     *
     * @return A parsed Nowcast object
     */
    suspend fun getNowcast(lat : Float, lon : Float) : Nowcast

    /**
     * Abstract function for the Locationforecast-API by met
     *
     * @param lat The latitude, rounded to at most 4 digits
     * @param lon The longitude, rounded to at most 4 digits
     *
     * @return A parsed Locationforecast object
     */
    suspend fun getLocationforecast(lat : Float, lon : Float) : Locationforecast

    /**
     * Abstract function for the Sunrise-API
     *
     * @param lat The latitude
     * @param lon The longitude
     *
     * @return A parsed Sunrise object
     */
    suspend fun getSunrise(lat : Float, lon : Float, date: String, offset: String) : Sunrise

    /**
     * Abstract function for the Geocoder-API
     *
     * @param lat The latitude
     * @param lon The longitude
     *
     * @return A parsed GeocoderAddress object
     */
    suspend fun getAddress(lat : Double, lon : Double) : GeocoderAddress
}
