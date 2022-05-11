package no.uio.g19.klesapp.ui.view.fragments.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import no.uio.g19.klesapp.BuildConfig.MAPS_API_KEY
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity
import no.uio.g19.klesapp.ui.view.makeCurrentFragment
import no.uio.g19.klesapp.ui.viewmodel.HomeViewModel

/*

Copyright 2021 Michael P. Kirya-Atabala

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


Source of code: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
Source of copyright: https://www.apache.org/licenses/LICENSE-2.0

The following section contains a modification of google developers source material
 */
/**
 * This fragment shows the window where users can search for new locations.
 * @property homeViewModel accesses necessary data.
 * @property locationPermissionGranted defines whether the user
 * has granted permission to share their location.
 * @property map is an entry point for GoogleMaps API.
 * @property lastKnownLocation provides information of the user's last known location.
 * @property fusedLocationProviderClient an intelligent location API.
 * @property placesClient is an entry point for Places API.
 * @property defaultLocation is the default location, set as Oslo's coordinates
 * fusedLocationProvider.
 * @property  defaultLocation the default location Oslo
 */
class SearchNewLoctionFragment : Fragment(), OnMapReadyCallback {
    private lateinit var homeViewModel : HomeViewModel

    private var locationPermissionGranted = false
    private var map: GoogleMap? = null
    private var lastKnownLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private val defaultLocation = LatLng(59.9138688, 10.7522454)

    private var container: ViewGroup? = null

    /**
     * This function inflates layout, sets button and initializes maps
     * @return view
     */
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val context = activity as AppCompatActivity
        val view = inflater.inflate(R.layout.searchnewloction_fragment, container, false)
        this.container = container

        val backButton = view.findViewById<TextView>(R.id.cancel_locsearch)


        backButton.setOnClickListener {
            context.makeCurrentFragment(ChooseLocationFragment())
        }


        //Maps
        //Loads the map view via a callback object
        //A fragmentmanager is used instead of a supportFragmentManager
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view

    }


    /**
     * This method is unmodified from Android Developers Guide, see sources above
     * See explanation below
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        val context = activity as AppCompatActivity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(context , arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    /**
     * This method is unmodified from Android Developers Guide, see sources above
     * Sets locationPermissionGranted and calls updateLocationUI
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }

        updateLocationUI()
    }


    /**
     * This method is unmodified from Android Developers Guide, see sources above
     * Updates googlemaps fragment UI based on whether or not
     * location permission is granted
     * If not it will as for a permission
     */
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * This method is modified from Android Developers Guide, see sources above
     * Fetches device location if locationPermissionGranted
     * Moves map camera to this location, and observes addressOnDeviceLocation and updates this
     * value to be the device location.
     *
     * The user gets a snackbar asking if this location should be added to database.
     * If device location is not added map is moved to default location.
     *
     */
    private fun getDeviceLocation() {

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        val context = activity as AppCompatActivity
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {

                            val lat: Double = lastKnownLocation!!.latitude
                            val lng: Double = lastKnownLocation!!.longitude
                            val placeLatLng = LatLng(lat, lng)

                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lat,
                                    lng), DEFAULT_ZOOM
                            ))

                            homeViewModel.addressOnDeviceLocation.observe(viewLifecycleOwner, { placeString ->
                                if (placeString.isNullOrEmpty()) {
                                    activity?.let {
                                        Snackbar.make(
                                            it.findViewById(R.id.searchNewLocContent),
                                            "Sted utenfor rekkevidde",
                                            Snackbar.LENGTH_LONG
                                        )
                                            .setBackgroundTint(ContextCompat.getColor(context, R.color.grey))
                                            .setActionTextColor(ContextCompat.getColor(context, R.color.white))
                                            .show()
                                    }
                                } else {
                                        activity?.let {
                                            Snackbar.make(
                                                it.findViewById(R.id.searchNewLocContent),
                                                "Legg til nåværende sted; $placeString ?",
                                                Snackbar.LENGTH_LONG
                                            )
                                                .setAction("Ja") {
                                                    map?.addMarker(
                                                        MarkerOptions()
                                                            .position(placeLatLng)
                                                            .title(placeString)
                                                    )
                                                    val newPlaceMaps =
                                                        PlaceRoomEntity(
                                                            placeString,
                                                            lat.toFloat(),
                                                            lng.toFloat()
                                                        )
                                                            homeViewModel.insertPlaceRoomEntity(newPlaceMaps)
                                                            Log.i("SearchNewLoctionFrag", "newPlaceMaps: $newPlaceMaps")
                                                            homeViewModel.setCurrentLocation(newPlaceMaps)
                                                            context.makeCurrentFragment(ChooseLocationFragment())

                                                }
                                                .setBackgroundTint(
                                                    ContextCompat.getColor(
                                                        context,
                                                        R.color.grey
                                                    )
                                                )
                                                .setActionTextColor(
                                                    ContextCompat.getColor(
                                                        context,
                                                        R.color.white
                                                    )
                                                )
                                                .show()
                                        }
                                    }
                            })

                            homeViewModel.getGeocoderAddressess(lat, lng, true)

                        }
                    } else {
                        Log.d("SrchNwLocFrag", "Current location is null. Using defaults.")
                        Log.e("SrchNwLocFrag", "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    /**
     * This method is modified from Android Developers Guide, see sources above
     * Observes allplaces in homeviewmodel and sets markers on map
     *
     * Uses Autocomplete to find a place and adds to database if user wishes so
     *
     * On a map click is where the addressonmapclick is observed and then updated according
     * to the location. This location is added tot he database if the user wishes so
     * @googleMap this fragments map
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val context = activity as AppCompatActivity
        this.map = googleMap

        homeViewModel.allPlaces.observe(viewLifecycleOwner, {
                for(place in it){
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.lat.toDouble(), place.lon.toDouble()))
                            .title(place.name)
                    )
                }
        })

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        //Initializing places API
        Places.initialize(context, MAPS_API_KEY)
        placesClient = Places.createClient(context)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as? AutocompleteSupportFragment

        //Sets place fields to be retrieved and ensures that they are in Norway
        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
            ?.setCountries("NO")

        //When a place is selected the camera moves to the place and user is asked if place is to be
        //added to database
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val placeCordinate: LatLng? = place.latLng
                if (placeCordinate != null ){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeCordinate, DEFAULT_ZOOM))

                    val lat : Float = placeCordinate.latitude.toFloat()
                    val lng : Float = placeCordinate.longitude.toFloat()
                    val name : String = place.name.toString()

                    activity?.let {
                        Snackbar.make(
                            it.findViewById(R.id.searchNewLocContent),
                            "Legg til $name i listen?",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Ja") {
                                map?.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(placeCordinate.latitude, placeCordinate.longitude))
                                        .title(name)
                                )
                                val newPlaceAuto = PlaceRoomEntity(name, lat, lng)
                                homeViewModel.insertPlaceRoomEntity(newPlaceAuto)
                                Log.i("SearchNewLoctionFrag", "newPlaceAuto: $newPlaceAuto")
                                homeViewModel.setCurrentLocation(newPlaceAuto)
                                context.makeCurrentFragment(ChooseLocationFragment())

                            }
                            .setBackgroundTint(
                                ContextCompat.getColor(
                                    context,
                                    R.color.grey
                                )
                            )
                            .setActionTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                            .show()
                    }
                }
                Log.i("SrchNwLocFrag", "Place: ${place.name},${place.latLng}, ${place.id} ")
            }

            override fun onError(status: Status) {
                Log.e("SrchNwLocFrag", "Autocomplete error occurred: $status")
            }
        })


        googleMap.setOnMapClickListener { p0 ->

            val lat: Double = p0.latitude
            val lng: Double = p0.longitude


            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(lat,
                    lng), DEFAULT_ZOOM
            ))

            homeViewModel.addressOnMapClick.observe(viewLifecycleOwner, { placeString ->
                if (placeString.isNullOrEmpty()) {
                    activity?.let {
                        Snackbar.make(
                            it.findViewById(R.id.searchNewLocContent),
                            "Sted utenfor rekkevidde",
                            Snackbar.LENGTH_LONG
                        )
                            .setBackgroundTint(ContextCompat.getColor(context, R.color.grey))
                            .setActionTextColor(ContextCompat.getColor(context, R.color.white))
                            .show()
                    }
                } else {
                    activity?.let {
                        Snackbar.make(
                            it.findViewById(R.id.searchNewLocContent),
                            "Legg til $placeString i listen?",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Ja") {
                                map?.addMarker(
                                    MarkerOptions()
                                        .position(p0)
                                        .title(placeString)
                                )
                                val newPlaceMaps =
                                    PlaceRoomEntity(
                                        placeString,
                                        lat.toFloat(),
                                        lng.toFloat()
                                    )

                                homeViewModel.insertPlaceRoomEntity(newPlaceMaps)
                                Log.i("SearchNewLoctionFrag", "newPlaceMaps: $newPlaceMaps")
                                homeViewModel.setCurrentLocation(newPlaceMaps)
                                context.makeCurrentFragment(ChooseLocationFragment())


                            }
                            .setBackgroundTint(
                                ContextCompat.getColor(
                                    context,
                                    R.color.grey
                                )
                            )
                            .setActionTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                            .show()
                    }

                }
            })

            homeViewModel.getGeocoderAddressess(lat, lng, false)
            Log.d("Map_Tag", "CLICK")
        }
        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }


    /**
     * This companion object contains values that are used as constants
     * @property DEFAULT_ZOOM used as default zoom for map camera
     * @property PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION used to check if permission is true
     *
     */
    companion object {
        private const val DEFAULT_ZOOM = 15f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

}