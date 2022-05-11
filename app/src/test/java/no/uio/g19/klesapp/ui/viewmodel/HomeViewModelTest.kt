package no.uio.g19.klesapp.ui.viewmodel

import android.os.Build
import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import no.uio.g19.klesapp.data.model.locationforecast.Locationforecast
import no.uio.g19.klesapp.data.model.nowcast.Nowcast
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity
import no.uio.g19.klesapp.data.model.sunrise.Sunrise
import no.uio.g19.klesapp.data.repository.MainRepository
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@Config(manifest=Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val vm = HomeViewModel()

    private val nowcastObs = Observer<Nowcast> {}
    private val locationObs = Observer<Locationforecast> {}
    private val sunriseObs = Observer<Sunrise> {}
    private val sannObs = Observer<WeatherDataHolder> {}
    private val dagsObs = Observer<DailyForecastHolder> {}
    private val timesObs = Observer<WeatherNext24Hrs> {}
    private val bakgrunnObs = Observer<String> {}

    @Before
    fun setUp() {
        MainRepository.testMode()

        vm.nowcast.observeForever(nowcastObs)
        vm.locationforecast.observeForever(locationObs)
        vm.sunrise.observeForever(sunriseObs)
        vm.sanntidsvarsel.observeForever(sannObs)
        vm.dagsvarsel.observeForever(dagsObs)
        vm.timesvarsel.observeForever(timesObs)
        vm.bakgrunn.observeForever(bakgrunnObs)

        shadowOf(getMainLooper()).idle()
    }

    @After
    fun tearDown() {
        vm.nowcast.removeObserver(nowcastObs)
        vm.locationforecast.removeObserver(locationObs)
        vm.sunrise.removeObserver(sunriseObs)
        vm.sanntidsvarsel.removeObserver(sannObs)
        vm.dagsvarsel.removeObserver(dagsObs)
        vm.timesvarsel.removeObserver(timesObs)
        vm.bakgrunn.removeObserver(bakgrunnObs)
    }

    @Test
    fun getNowcast() {
        assertNotNull(vm.nowcast.value)
        assertEquals(9.2, vm.nowcast.value!!.properties.timeseries[0].data.instant.details.air_temperature,0.0)
        assertEquals("lightrain", vm.nowcast.value!!.properties.timeseries[0].data.next_1_hours.summary.symbol_code)
    }

    @Test
    fun getLocationforecast() {
        assertNotNull(vm.locationforecast.value)
        assertEquals(17.1, vm.locationforecast.value!!.properties.timeseries[0].data.instant.details.air_temperature, 0.0)
        assertEquals("clearsky_day", vm.locationforecast.value!!.properties.timeseries[0].data.next_1_hours.summary.symbol_code)
    }

    @Test
    fun getSunrise() {
        assertNotNull(vm.sunrise.value)
        assertEquals("2021-05-04T04:07:26+01:00", vm.sunrise.value!!.location.time[0].sunrise.time)
        assertEquals("2021-05-04T20:22:13+01:00", vm.sunrise.value!!.location.time[0].sunset.time)
    }

    @Test
    fun getSanntidsvarsel() {
        assertNotNull(vm.sanntidsvarsel.value)
        assertEquals(9.2, vm.sanntidsvarsel.value!!.effTemp, 0.0)
        assertEquals(0.2, vm.sanntidsvarsel.value!!.nedbor, 0.0)
    }

    @Test
    fun getDagsvarsel() {
        assertNotNull(vm.dagsvarsel.value)
        assertEquals(11.1, vm.dagsvarsel.value!!.minTemp, 0.0)
        assertEquals(17.1, vm.dagsvarsel.value!!.maxTemp, 0.0)
        assertEquals(1.71, vm.dagsvarsel.value!!.precipitation, 0.0)
    }

    @Test
    fun getTimesvarsel() {
        assertNotNull(vm.timesvarsel.value)
        assertEquals("clearsky_day", vm.timesvarsel.value!!.weatherList[0].symbolCode)
        assertEquals(11.1, vm.timesvarsel.value!!.weatherList[0].temp, 0.0)
    }

    @Test
    fun getBakgrunn() {
        assertNotNull(vm.bakgrunn.value)
        assertEquals("lightrain_night_common", vm.bakgrunn.value)
    }

    @Test
    fun regnEffTemp() {

        assertEquals(4.0, vm.regnEffTemp(5.0, 1.5),0.25)
        assertEquals(-11.0, vm.regnEffTemp(-5.0, 4.5),0.25)
        assertEquals(-47.0, vm.regnEffTemp(-30.0, 10.5),0.25)
        assertEquals(6.0, vm.regnEffTemp(6.0, 1.5), 0.25)
        assertEquals(-5.0, vm.regnEffTemp(-5.0, 0.0), 0.25)
    }

    @Test
    fun degreesToCode() {
        assertEquals("E/SE", vm.dagsvarsel.value!!.windDir)
        assertEquals("W", vm.degreesToCode(270.0))
        assertEquals("S", vm.degreesToCode(180.0))
    }

    @Test
    fun getCurrentLocation() {
        assertEquals("Oslo", vm.getCurrentLocation())
    }

    @Test
    fun setCurrentLocation() {
        vm.setCurrentLocation(PlaceRoomEntity("Bergen", 0.0f, 0.0f))
        assertEquals("Bergen", vm.getCurrentLocation())
    }

    @Test
    fun setBackground() {
        val backgroundResource = vm.setBackground("clearsky_day")
        assertEquals("clearsky_night_common", backgroundResource)
    }
}