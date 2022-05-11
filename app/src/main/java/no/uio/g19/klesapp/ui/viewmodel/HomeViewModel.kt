package no.uio.g19.klesapp.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.g19.klesapp.data.model.wardrobe.Antrekk
import no.uio.g19.klesapp.data.model.wardrobe.Plagg
import no.uio.g19.klesapp.data.model.locationforecast.Locationforecast
import no.uio.g19.klesapp.data.model.nowcast.Data
import no.uio.g19.klesapp.data.model.nowcast.Nowcast
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity
import no.uio.g19.klesapp.data.model.sunrise.Sunrise
import no.uio.g19.klesapp.data.repository.MainRepository
import no.uio.g19.klesapp.data.repository.MainRepository.getGeocoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

/**
 * Fetches data from the repository, transforms it and serves LiveData to the views and fragments.
 *
 * This viewmodel serves fragments related to the Home screen.
 *
 * Documentation: https://developer.android.com/kotlin/coroutines/coroutines-best-practices
 *
 * It contains logic to adjust temperature by wind cooling [regnEffTemp]
 * @property allPlaces Serves a list of [PlaceRoomEntity] as livedata
 *
 * @property defaultDispatcherIO Serves a CouroutineDispatcher used as default Coroutine context
 *
 * @property nowcast Serves data from repository as livedata, used in transforms
 * @property locationforecast Serves data from repository as livedata, used in transforms
 * @property sunrise Serves data from repository as livedata, used in transforms
 *
 * @property sanntidsvarsel Transforms [Nowcast] into a [WeatherDataHolder] used to convey
 * information about the current weather in the top bar.
 *
 * @property timesvarsel Transforms [Locationforecast] into a [WeatherNext24Hrs] with next 24 hrs
 * @property dagsvarsel Transforms [Locationforecast] into a [DailyForecastHolder] with daily info
 * @property bakgrunn Combines [Locationforecast] and [Sunrise] to determine a proper background
 *
 * @property addressOnMapClick Serves data from repository as livedata, after being initialized in
 * @property addressOnDeviceLocation Serves data from repository as livedata, after being initialized
 */
class HomeViewModel : ViewModel() {
    val allPlaces: LiveData<List<PlaceRoomEntity>> = liveData { emit(MainRepository.getAllPlaces()) }

    private val defaultDispatcherIO: CoroutineDispatcher = Dispatchers.IO

    val nowcast : LiveData<Nowcast> = liveData {
        emit(MainRepository.getNowcast())
    }

    val locationforecast : LiveData<Locationforecast> = liveData {
        emit(MainRepository.getLocationforecast())
    }

    val sunrise : LiveData<Sunrise> = liveData {
        emit(MainRepository.getSunrise())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val sanntidsvarsel : LiveData<WeatherDataHolder> = Transformations.map(nowcast) {
        val forecast: Data = it.properties.timeseries[0].data

        val precipitation = forecast.next_1_hours.details.precipitation_amount
        val temperature = forecast.instant.details.air_temperature
        val wind = forecast.instant.details.wind_speed
        val effTemp = regnEffTemp(temperature, wind)
        val symbolCode = forecast.next_1_hours.summary.symbol_code

        //Denne dataklassen brukes til å holde på relevant værdata i valg av antrekk
        val nyDataHolder = WeatherDataHolder(effTemp, temperature, wind, precipitation, symbolCode, setBackground(symbolCode))
        nyDataHolder
    }

    val dagsvarsel : LiveData<DailyForecastHolder> = Transformations.map(locationforecast) {
        val firstForecast = it.properties.timeseries[0]
        val time = firstForecast.time
        val instant = firstForecast.data.instant.details
        val next1hours = firstForecast.data.next_1_hours
        val next6hours = firstForecast.data.next_6_hours
        val next12hours = firstForecast.data.next_12_hours

        DailyForecastHolder(
                next6hours.details.air_temperature_min,
                next6hours.details.air_temperature_max,
                0.0,
                next6hours.details.precipitation_amount,
                instant.relative_humidity,
                instant.wind_speed,
                degreesToCode(instant.wind_from_direction))
    }

    val timesvarsel : LiveData<WeatherNext24Hrs> = Transformations.map(locationforecast) {
        val weather24hrs = mutableListOf<HourForcastHolder>()
        var teller = 0
        for (info in it.properties.timeseries) {
            if (teller < 24) {
                val time: String = info.time
                Log.v("Klesapp", "tidsobj: Time $time ")
                val temp: Double = info.data.instant.details.air_temperature
                Log.v("Klesapp", "tidsobj: temp  $temp ")
                val symbCode: String = info.data.next_1_hours.summary.symbol_code
                Log.v("Klesapp", "tidsobj: symbcode$symbCode ")

                val weatherHour = HourForcastHolder(time, temp, symbCode)
                weather24hrs.add(weatherHour)
                teller++
            } else {
                break
            }
        }

        WeatherNext24Hrs(weather24hrs)
    }

    val langtidsvarsel : LiveData<Array<DailyWeatherHolder>> = Transformations.map(locationforecast) {
        val melding = mutableListOf<DailyWeatherHolder>()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no","NO","nb_NO" ))
        val weekDayToString = mapOf(2 to "Mandag", 3 to "Tirsdag", 4 to "Onsdag", 5 to "Torsdag", 6 to "Fredag", 7 to "Lørdag", 1 to "Søndag")
        for (info in it.properties.timeseries) {
            val cal = Calendar.getInstance()
            val time = sdf.parse(info.time)
            if (time != null) cal.time = time else continue
            if (cal.get(Calendar.HOUR_OF_DAY) != 12) continue

            val weekday : String = weekDayToString[cal.get(Calendar.DAY_OF_WEEK)] ?: ""
            with (info.data.next_6_hours) {
                melding.add(DailyWeatherHolder(
                        summary.symbol_code,
                        weekday,
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH),
                        details.precipitation_amount,
                        details.probability_of_precipitation,
                        details.air_temperature_max,
                        details.air_temperature_min))
            }
        }

        melding.toTypedArray()
    }

    val bakgrunn : LiveData<String> = makeBackgroundLiveData(sunrise, nowcast)


    val addressOnMapClick : MutableLiveData<String> = MutableLiveData<String>()

    val addressOnDeviceLocation : MutableLiveData<String> = MutableLiveData<String>()

    /**
     * Function that fetches geocoderAddres and fetches relevant address string
     *
     * Removes address number and checks wether location is in Norway
     * Post to relevant MutableLiveData variable
     *
     * If the string value is out of bounds due to country or location an empty string is posted
     * and the user is notified
     *
     * @param latitude of new request
     * @param longitude of new request
     * @param deviceLocorMapClick decides which MutableLiveData to be posted to based on boolean value
     */
    fun getGeocoderAddressess( latitude: Double, longitude: Double, deviceLocorMapClick : Boolean) {
        CoroutineScope(defaultDispatcherIO).launch {
            try {
                val geocoderAddress = getGeocoder(latitude, longitude)
                Log.i("HomeViewModel","$latitude,$longitude ${geocoderAddress.results[0].formatted_address}")
                val addressName = geocoderAddress.results[0].formatted_address
                val nameValues: List<String> = addressName.split(",").map { it.trim() }
                var shortAddressName = nameValues[0]
                if (shortAddressName == "Unnamed Road"){
                    shortAddressName = nameValues[1]
                }else{
                    val removeNo = Regex("[^A-Za-z ÆØÅæøå]")
                    val addresCity = removeNo.replace(nameValues[1], "").trim()
                    shortAddressName = removeNo.replace(shortAddressName, "").trim()
                    shortAddressName = "$shortAddressName, $addresCity "
                }

                var inNorway = false
                for (i in nameValues) {
                    if (i == "Norway") {
                        inNorway = true
                    }
                }
                if(deviceLocorMapClick && addressName.isNotEmpty() && inNorway ){
                    addressOnDeviceLocation.postValue(shortAddressName)
                    Log.i("HomeViewModel","addressOnDeviceLocation: $addressOnDeviceLocation")
                }else if (!deviceLocorMapClick && addressName.isNotEmpty() && inNorway){
                    addressOnMapClick.postValue(shortAddressName)
                    Log.i("HomeViewModel ","addressOnMapClick: $addressOnMapClick")

                }else if (deviceLocorMapClick && !inNorway  ) {
                    addressOnDeviceLocation.postValue("")
                    Log.i("HomeViewModel: ","Sted utenfor rekkevidde.")
                }else if (!deviceLocorMapClick && !inNorway){
                    addressOnMapClick.postValue("")
                    Log.i("HomeViewModel: ","Sted utenfor rekkevidde:")
                }

            }catch (exception: Exception){
                addressOnMapClick.postValue("")
                Log.w("HomeViewModel ","Sted utenfor rekkevidde: ${exception.message}")
            }
        }


    }

    /**
     * This function sets up a MediatorLiveData to enable evaluation of which background to use when
     * both [Sunrise] and [Nowcast] are non-null.
     *
     * Adds observers to both needed livedata and checks whether both are present. When both are, it
     * calls [setBackground] and updates the livedata
     *
     * @param sun The sunrise-livedata to observe
     * @param weather The nowcast-livedata to observe
     * @return A livedata combining both sources to output a path to the background resource
     */
    private fun makeBackgroundLiveData(sun : LiveData<Sunrise>, weather : LiveData<Nowcast>) : LiveData<String> {
        val combinedLiveData = MediatorLiveData<String>()

        combinedLiveData.addSource(sun) {
            if (sun.value != null && weather.value != null) {
                val symbolCode =
                    nowcast.value!!.properties.timeseries[0].data.next_1_hours.summary.symbol_code
                combinedLiveData.value = setBackground(symbolCode)
            }
        }

        combinedLiveData.addSource(weather) {
            if (sun.value != null && weather.value != null) {
                val symbolCode =
                    nowcast.value!!.properties.timeseries[0].data.next_1_hours.summary.symbol_code
                combinedLiveData.value = setBackground(symbolCode)
            }

        }

        return combinedLiveData
    }

    /**
     * This function evaluates effective temperature based on wind and air temperature
     *
     * For warm weather or little wind, it simply returns the input temp without adjustment.
     * Formula based on article by yr.no
     * source: https://hjelp.yr.no/hc/no/articles/360001695513-Effektiv-temperatur-og-f%C3%B8les-som-
     *
     * @param temp Air temperature measured in celsius 2 meters above sea level
     * @param wind Wind measured in meters per second
     *
     * @return Temperature adjusted for actual cooling
     */
    fun regnEffTemp(temp: Double, wind: Double): Double{
        if (temp > 5 || wind < 1.5)
            return temp

        val windKmH = wind * 3.6

        return 13.12 + 0.6215*temp - 11.37*windKmH.pow(0.16) + 0.3965*temp*windKmH.pow(0.16)
    }

    /** This function converts degrees on a compass to cardinal directions
     *
     * @param dir The direction in degrees
     *
     * @return cardinal direction
     */
    fun degreesToCode(dir: Double) : String {
        return when (dir.toInt()) {
            in 0..15 -> "N"
            in 15..35 -> "N/NE"
            in 35..55 -> "NE"
            in 55..75 -> "E/NE"
            in 75..105 -> "E"
            in 105..125 -> "E/SE"
            in 125..145 -> "SE"
            in 145..165 -> "S/SE"
            in 165..195 -> "S"
            in 195..215 -> "S/SW"
            in 215..235 -> "SW"
            in 235..255 -> "W/SW"
            in 255..285 -> "W"
            in 285..305 -> "W/NW"
            in 305..325 -> "NW"
            in 325..345 -> "N/NW"
            else -> "N"

        }
    }

    /**
     * initializeFileDatabase is a function to help initialising the Database-class from the View
     *
     * The function is a gateway between View and repository.
     * It is called upon in HomeFragment, and inherits HomeFragments context and the file name from the view
     * The function exists so that the Fragment and repository do not directly communicate
     *
     * @param context Needed to read the file
     */
    fun initializeFileDatabase(context: Context, filename: String){
        MainRepository.initializeFileDatabase(context, filename)
    }

    /**
     * initializePlaceDatabase is a function to help initialising the Database-class from the View
     *
     * The function is a gateway between View and repository.
     * It is called upon in HomeFragment, and inherits HomeFragments context and the file name from the view
     * The function exists so that the Fragment and repository do not directly communicate
     * A Couroutine is used for thread-safety
     *
     * @param context The application context needed to initialize the database
     */

    fun initializePlacesDatabase(context: Context){
        CoroutineScope(defaultDispatcherIO).launch {
            try {
                MainRepository.initializePlacesDatabase(context)
            }catch (exception: Exception){
                Log.e("Error: HomeViewModel ","Error in MainRepository.initializePlacesDatabase(context): ${exception.message}")
            }
        }
    }

    /**
     * insertPlaceRoomEntity forwards a place to be added to the database
     * The function is called upon from SearchNewLoctionFragment
     * A Couroutine is used for thread-safety
     *
     * @param place the entity to be added to the database
     */
    fun insertPlaceRoomEntity(place: PlaceRoomEntity){
        CoroutineScope(defaultDispatcherIO).launch {
            try {
                MainRepository.insert(place)
            }catch (exception: Exception){
                Log.e("Error: HomeViewModel ","Error in MainRepository.insert(place)): ${exception.message}")
            }
        }

    }


    /**
     * getCurrentLocation fetches the location
     *
     * @return the current location chosen by the user
     */
    fun getCurrentLocation() : String {
        val stedsnavn = MainRepository.currentPlace.name
        if (stedsnavn == "") Log.d("KLESAPP", "Ingen sted funnet")
        return stedsnavn
    }

    /**
     * Changes the current location
     *
     * Triggers livedata-changes
     *
     * @param newLocation the chosen location
     */
    fun setCurrentLocation(newLocation: PlaceRoomEntity) {
        MainRepository.currentPlace = newLocation
    }

    /**
     * velgAntrekk is a function that is used by observers to return an outfit based on weather data
     *
     * The function takes a WeatherDataHolder-object from sanntidsdata and returns
     * an outfit object
     * The function goes through the list of all outfits in the database/wardrobe,
     * and chooses the outfit that have the lowest acceptable values on
     * temperature, wind and percipitation.
     *
     * In the last selection, temperature and percipitation are compared together,
     * but temperature is weighed so that it counts more.
     * This method has a theoretical weakness in that it might end up choosing
     * the wrong outfit in certain situations. This is something we should work to
     * improve
     *
     * @return An outfit-object
     */
    fun velgAntrekk(): Antrekk? {
        val vaerdata : WeatherDataHolder? = sanntidsvarsel.value
        if (vaerdata == null){
            Log.d("velgAntrekk(): ", "Ingen værdata funnet.")
            return null
        }

        val antrekkListe = MainRepository.getOutfits() //Henter antrekksliste
        Log.d("velgAntrekk() liste ", antrekkListe.toString())

        val output: Antrekk? = antrekkListe
            .filter { it.vind || vaerdata.vind < 1.5 }
            .also{ it -> Log.d("velgAntrekk():", "Etter vind: ${it.map{it.id}}")}
            // Velger kun varme nok antrekk
            .filter { vaerdata.effTemp >= it.tempMin }
            .also{ it -> Log.d("velgAntrekk():", "Etter effTemp: ${it.map{it.id}}")}
            // Velger kun antrekk som tåler meldt regn
            .filter { vaerdata.nedbor <= it.maxNedbor }
            .also{ it -> Log.d("velgAntrekk():", "Etter nedbor: ${it.map{it.id}}")}
            // Vektfunksjon som velger det letteste antrekket, og vurderer regn ved uavgjort
            .minByOrNull { kotlin.math.abs(vaerdata.effTemp - it.tempMin) * 117 + it.maxNedbor }

        if (output != null) {
            Log.d("velgAntrekk(): ", "Valgt antrekk: $output")
        } else {
            Log.d("velgAntrekk(): ", "Intet antrekk funnet. For vind: ${vaerdata.vind}, effektiv temperatur: ${vaerdata.effTemp}, nedbor: ${vaerdata.nedbor}")
        }
        return output
    }

    /**
     * getClothingFromOutfit takes in an outfit and returns a mutable list of the clothes it contains
     *
     * The function uses the IDs stored in the outfit, at uses them to get a list
     * of plagg-objects from the Database in MainRepository.
     *
     * @return a mutableList of plagg-objects
     */

    fun getClothingFromOutfit(outfit:Antrekk): List<Plagg> {
        //Find clothing id from outfit
        val outfitClothing = outfit.plagg
        //Find all clothing in database
        val allClothing : List<Plagg> = MainRepository.getClothes()
        //Find clothing id

        val clothingInOutfit : MutableList<Plagg> = mutableListOf()

        for (clothingID in outfitClothing){
            for (dbClothing in allClothing){
                if (dbClothing.id == clothingID) {
                    clothingInOutfit.add(dbClothing)
                }
            }
        }
        return clothingInOutfit
    }

    /**
     * A function that evaluates which background to set. Requires [sunrise] to be non-null.
     *
     * Evaluates whether or not the sun is up and season based on current time.
     *
     * @param symbolCode a summary of the current weather available in [Nowcast] and [Locationforecast]
     *
     * @return a path to the correct background-resource
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    fun setBackground(symbolCode: String) : String {

        try {
            val sunriseTime = sunrise.value!!.location.time[0].sunrise.time
            val sunsetTime = sunrise.value!!.location.time[0].sunset.time

            var background = symbolCode

            val c = Calendar.getInstance()
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)
            val now = ("$month.$day").toDouble()//5.4

            var season = ""

            Log.v("Date", "Today is $now")

            if (3.20 <= now && now < 6.21) {//vår
                season = "Spring"
                Log.v("Date", "Spring")
            } else if (6.21 <= now && now < 9.23) {//sommer
                season = "Summer"
                Log.v("Date", "Summer")
            } else if (9.23 <= now && now < 12.21) {//høst
                season = "Autumn"
                Log.v("Date", "Autumn")
            } else {
                season = "Winter"
                Log.v("Date", "Winter")//vinter
            }


            val rise = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(sunriseTime)
            val set = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(sunsetTime)

            if ("_polartwilight" !in background) {
                if (Calendar.getInstance().time.after(rise) && Calendar.getInstance().time.before(set)) {
                    if ("_day" !in background && "_night" !in background) {
                        background += "_day"
                    } else if ("_night" in background) {
                        background.replace("_night", "_day")
                    }
                    when (season) {
                        "Spring" -> background += "_spring"
                        "Summer" -> background += "_summer"
                        "Autumn" -> background += "_autumn"
                        "Winter" -> background += "_winter"
                    }
                } else {
                    if ("_day" !in background && "_night" !in background) {
                        background += "_night"
                    } else if ("_day" in background) {
                        background = background.replace("_day", "_night")
                    }
                    when (season) {
                        "Spring", "Summer", "Autumn" -> background += "_common"
                        "Winter" -> background += "_winter"
                    }
                }
            } else {
                background += "_b"
            }

            return background
        } catch (np: NullPointerException) {
            return "fair_day_spring"
        }
    }
}
