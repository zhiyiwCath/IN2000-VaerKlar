package no.uio.g19.klesapp.data.repository

import android.content.Context
import android.util.Log
import no.uio.g19.klesapp.data.FileDatabase
import no.uio.g19.klesapp.data.api.ApiHandler
import no.uio.g19.klesapp.data.api.ApiInterface
import no.uio.g19.klesapp.data.model.wardrobe.Antrekk
import no.uio.g19.klesapp.data.model.wardrobe.Plagg
import no.uio.g19.klesapp.data.model.geocoder.GeocoderAddress
import no.uio.g19.klesapp.data.model.locationforecast.Locationforecast
import no.uio.g19.klesapp.data.model.nowcast.Nowcast
import no.uio.g19.klesapp.data.model.placesDB.PlacesDao
import no.uio.g19.klesapp.data.model.placesDB.PlacesDatabase
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity
import no.uio.g19.klesapp.data.model.sunrise.Sunrise
import no.uio.g19.klesapp.data.repository.MainRepository.cacheTimeOut
import no.uio.g19.klesapp.data.repository.MainRepository.currentPlace
import no.uio.g19.klesapp.test.ApiDummyStub
import no.uio.g19.klesapp.test.testStederStub
import java.text.SimpleDateFormat
import java.util.*

/**
 * Single source of truth for the project. Coordinates and caches data from three sources: (1) API,
 * (2) a file database and (3) a room database.
 *
 * Contains mostly logic for evaluating whether or not to redo an API-call
 *
 * @property apiInterface The access point for API related requests
 * @property staticFilesFileDatabase The access point for the wardrobe class
 * @property roomDAO the data access object for the placesDB
 *
 * @property nowcastCache a cache for Nowcast
 * @property locationforecastCache a cache for LocationForecast
 * @property sunriseCache a cache for Sunrise
 *
 * @property cacheTimeOut How long an API-call is considered valid. Initialized to 15 minutes.
 * @property currentPlace The location chosen by the user. API-calls are made based on this property.
 */
object MainRepository {
    private var apiInterface: ApiInterface = ApiHandler()
    private lateinit var staticFilesFileDatabase: FileDatabase
    private lateinit var roomDAO : PlacesDao

    private var nowcastCache = mutableMapOf<PlaceRoomEntity, Pair<Calendar, Nowcast>>()
    private var locationforecastCache = mutableMapOf<PlaceRoomEntity, Pair<Calendar, Locationforecast>>()
    private var sunriseCache = mutableMapOf<PlaceRoomEntity, Pair<Calendar, Sunrise>>()

    var cacheTimeOut = 15
    var currentPlace: PlaceRoomEntity = PlaceRoomEntity("Oslo", 59.9114f, 10.7579f)

    /**
     * Gets a list of places
     *
     * @return either a list with results from the database or an empty list if the database is not
     * initialized
     */
    suspend fun getAllPlaces() :  List<PlaceRoomEntity>{
        if(this::roomDAO.isInitialized){
            return roomDAO.getAllList()
        }
        return listOf()
    }

    /**
     * Initializes a database from a context.
     *
     * The Room-database requires a context to be initialized. A better solution would be dependency
     * injection or something similar. Alas there was not enough time.
     *
     * @param context An application context giving the Room-database access to the environment
     */
    fun initializePlacesDatabase(context: Context)  {
        if (!this@MainRepository::roomDAO.isInitialized)
            roomDAO = PlacesDatabase.getInstance(context.applicationContext).placesDao

    }

    /**
     * Insert place information to the database.
     *
     * @param place The object to be inserted
     */
    suspend fun insert(place: PlaceRoomEntity) {
        roomDAO.insertAll(place)
    }

    /**
     * Function that clears the database.
     *
     */
    suspend fun clearPLaceDatabase(){
        roomDAO.clearTable()
    }

    /**
     * Function that checks whether a certain time has elapsed since a timeStamp. Check whether the
     * time is greater than cacheTimeOut in minutes.
     *
     * @param timeStamp a Calendar instance, can be generated with Calendar.getInstance()
     *
     * @return true if the elapsed time in minutes is lower than the set timeout in cacheTimeOut
     */
    private fun upToDate(timeStamp : Calendar) : Boolean {
        val now = Calendar.getInstance()

        val duration = (now.timeInMillis - timeStamp.timeInMillis) / (1000 * 60)
        Log.v("Duration", "Time lapsed since API-call: $duration minutes")

        return duration < cacheTimeOut
    }

    /**
     * Function that gets and caches an API-kall to the nowcast-API by met
     *
     * Documentation: https://schema.api.met.no/doc/nowcast/datamodel
     *
     * Evaluates whether the cached call is older than cacheTimeOut minutes
     *
     * @return Parsed data from the Nowcast-api, not older than cacheTimeOut minutes
     */
    suspend fun getNowcast(): Nowcast {
        return if (currentPlace in nowcastCache.keys && upToDate(nowcastCache[currentPlace]!!.first)) {
            nowcastCache[currentPlace]!!.second

        } else {
            apiInterface.getNowcast(currentPlace.lat, currentPlace.lon)
                    .also{ nowcastCache[currentPlace] = Pair(Calendar.getInstance(), it)}
        }
    }

    /**
     * Function that gets and caches an API-kall to the locationforecast-API by met
     *
     * Documentation: https://schema.api.met.no/doc/locationforecast/datamodel
     *
     * Evaluates whether the cached call is older than cacheTimeOut minutes
     *
     * @return Parsed data from the Locationforecast-api, not older than cacheTimeOut minutes
     */
    suspend fun getLocationforecast(): Locationforecast {
        return if (currentPlace in locationforecastCache.keys && upToDate(locationforecastCache[currentPlace]!!.first)) {
            locationforecastCache[currentPlace]!!.second

        } else {
            apiInterface.getLocationforecast(currentPlace.lat, currentPlace.lon)
                    .also{ locationforecastCache[currentPlace] = Pair(Calendar.getInstance(), it)}
        }
    }

    /**
     * Function that gets and caches an API-kall to the sunrise-API
     *
     * Evaluates whether the cached call is older than cacheTimeOut minutes
     *
     * @return Parsed data from the Sunrise-api, not older than cacheTimeOut minutes
     */
    suspend fun getSunrise(): Sunrise {
        return if (currentPlace in sunriseCache.keys && upToDate(sunriseCache[currentPlace]!!.first)) {
            sunriseCache[currentPlace]!!.second

        } else {
            apiInterface.getSunrise(
                    currentPlace.lat,
                    currentPlace.lon,
                    SimpleDateFormat("yyyy-MM-dd", Locale("no","NO","nb_NO" )).format(Date()),
                    SimpleDateFormat("ZZZZZ", Locale("no","NO","nb_NO" )).format(Date()))
                    .also{ sunriseCache[currentPlace] = Pair(Calendar.getInstance(), it)}
        }
    }

    /**
     * Initialize file-database. Passes the needed context and filename to propagate the
     * staticFilesDatabase with outfits.
     *
     * @param context Application context needed to read files
     * @param filename the path to the file to be read
     */
    fun initializeFileDatabase(context: Context, filename : String){
        if (!this::staticFilesFileDatabase.isInitialized)
            staticFilesFileDatabase = FileDatabase(context, filename)
    }

    /**
     * Get outfits from the files database.
     *
     * @return A list of outfits (Antrekk)
     */
    fun getOutfits() : List<Antrekk> = staticFilesFileDatabase.getOutfits()

    /**
     * Get clothes from the files database.
     *
     * @return A list of clothes (Plagg)
     */
    fun getClothes() : List<Plagg> = staticFilesFileDatabase.getClothes()

    /**
     * Function used to set the repository in a testing state. Required by HomeViewModelTest.
     *
     * This is a bad solution, and should rather be solved by using a more robust pattern such as
     * dependency incjection. It creates a tight coupling between this repository and the test,
     * which
     * makes the code base less maintainable, clear and readable.
     */
    fun testMode() {
        apiInterface = ApiDummyStub()
        currentPlace = testStederStub()[0]
    }

    /**
     * Function that gets GeocoderAddress from API-response
     *
     * @return Parsed data from Geocoder API
     */
    suspend fun getGeocoder(lat : Double, lon : Double): GeocoderAddress {
        return  apiInterface.getAddress(lat, lon)

    }


}

