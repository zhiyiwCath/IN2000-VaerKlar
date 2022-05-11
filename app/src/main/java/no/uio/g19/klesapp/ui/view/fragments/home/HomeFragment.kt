package no.uio.g19.klesapp.ui.view.fragments.home

import android.content.res.Resources
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.view.makeCurrentFragment
import no.uio.g19.klesapp.ui.viewmodel.HomeViewModel

/**
 * This fragment shows the main window of the application,
 * and is also the first window shown, when the application is started.
 * @property homeViewModel accesses necessary data.
 * It contains weather information for a specified location,
 * and an view of an outfit recommended by the application based on the weather.
 * It contains two Recyclerviews: time_weather and daily_weather.
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    /**
     * This function sets an onClickListener on outfit_image.
     * It also adjusts the height and padding of rom (Relativelayout in home_fragment.xml)
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)

        val context = activity as AppCompatActivity
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)




        val outfit = view.findViewById<ImageView>(R.id.outfit_image)

        outfit.setOnClickListener{
            context.makeCurrentFragment(TodayOutfitDetailFragment())
        }

        //get height and width of the current screen
        val height = Resources.getSystem().displayMetrics.heightPixels
        val width = Resources.getSystem().displayMetrics.widthPixels

        Log.v("SIZE: Screen h is:", height.toString())
        Log.v("SIZE: Screen w is:", width.toString())

        //get height of navigation menu
        val menuHeight = getMenuHeight(context)
        Log.v("SIZE: Menu h is", menuHeight.toString())

        //Change height of relativelayout (rom) to match all screensizes
        val romRelative= view.findViewById<RelativeLayout>(R.id.rom)
        romRelative.layoutParams.height = height - menuHeight

        //Change height and width of outfit_image based on sizechange of screen/rom relativelayout
        outfit.layoutParams.height = (height * 0.52).toInt()
        outfit.layoutParams.width = (width * 0.63).toInt()

        Log.v("SIZE: Outfits h is ", outfit.layoutParams.height.toString())
        Log.v("SIZE: Outfits w is ", outfit.layoutParams.width.toString())

        romRelative.setPadding(0, 0, 0, menuHeight)

        return view

    }

    /**
     * This function updates the values of different elements in home_fragment, based on data
     * retrieved from nowcast, locationforecast, and sunrise API.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        this.context?.let { homeViewModel.initializeFileDatabase(it, "antrekk.JSON") }
        this.context?.let { homeViewModel.initializePlacesDatabase(it) }

        if (view != null) {

            val currentLocation = requireView().findViewById<TextView>(R.id.now_location)

            currentLocation.text = homeViewModel.getCurrentLocation()
            val weatherTempTextView = requireView().findViewById<TextView>(R.id.home_tempText)
            val effTempTextView = requireView().findViewById<TextView>(R.id.feel_temp)
            val outfitView = requireView().findViewById<ImageView>(R.id.outfit_image)
            val topWeatherTextView = requireView().findViewById<TextView>(R.id.home_weatherText)
            val backgroudpic = requireView().findViewById<ImageView>(R.id.weather_background)

            homeViewModel.sanntidsvarsel.observe(viewLifecycleOwner, {
                weatherTempTextView.text = getString(R.string.weather_Temp_TextView, it.temp)
                effTempTextView.text = getString(R.string.eff_Temp_TextView, it.effTemp)
                val outfit = homeViewModel.velgAntrekk()
                topWeatherTextView.text = symbolToText(it.symbol_code)
                Log.v("Now weather is", it.symbol_code)
                if (outfit != null) {
                    Log.d("Sanntidsvarsel outfit: ", outfit.ikon)
                    val drawableIconName = outfit.ikon
                    val resourceId = context?.resources?.getIdentifier(
                        drawableIconName,
                        "drawable",
                        context?.packageName
                    )
                    if (resourceId != null) {
                        Log.d("Sanntidsvarsel id: ", resourceId.toString())
                        outfitView.setImageResource(resourceId)
                    } else {
                        Toast.makeText(this.context, "Error: Outfit icon not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this.context, "Error: Outfit not found", Toast.LENGTH_SHORT).show()
                }
            })



            val minMaxTempTextView = requireView().findViewById<TextView>(R.id.maxmin_temp)
            val precipitationTextView = requireView().findViewById<TextView>(R.id.precipitation_amount)
            val humidityTextView = requireView().findViewById<TextView>(R.id.relative_humidity)
            val windDirectionSpeedTextView = requireView().findViewById<TextView>(R.id.wind_direction_speed)

            homeViewModel.dagsvarsel.observe(viewLifecycleOwner, {
                minMaxTempTextView.text = getString(R.string.min_Max_Temp_TextView, it.maxTemp , it.minTemp)
                precipitationTextView.text = getString(R.string.precipitation_TextView, it.precipitation)
                humidityTextView.text = getString(R.string.humidity_TextView, it.humidity)
                windDirectionSpeedTextView.text = getString(R.string.wind_Direction_Speed_TextView, it.windDir, it.wind)

            })


            val timeWeatherRecyclerView = requireView().findViewById<RecyclerView>(R.id.time_weather)
            timeWeatherRecyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            homeViewModel.timesvarsel.observe(viewLifecycleOwner, {
                val timeWeatherAdapter = TimeWeatherAdapter(it.weatherList)
                timeWeatherRecyclerView.adapter = timeWeatherAdapter
            })


            val dailyWeatherRecyclerView = requireView().findViewById<RecyclerView>(R.id.daily_weather)
            dailyWeatherRecyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )

            homeViewModel.langtidsvarsel.observe(viewLifecycleOwner, {
                dailyWeatherRecyclerView.adapter = DailyWeatherAdapter(it)
            })

            homeViewModel.bakgrunn.observe(viewLifecycleOwner, {
                try {
                    Log.v("KLESAPP", "Bakgrunn er $it")
                    val id = resources.getIdentifier(it, "drawable", activity?.packageName)
                    val drawable = context?.let { it1 -> ContextCompat.getDrawable(it1, id) }
                    backgroudpic.background = drawable
                } catch (expt: NullPointerException) {
                    Log.e("KLESAPP", "Bakgrunnsbilde finnes ikke.")

                }
            })

            // TODO: Use the ViewModel
        }

    }

    /*private fun loadFragment(fragment: Fragment){
        getChildFragmentManager().beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }*/

    /**
     * This function converts a symbol code to a text describing the weather.
     * @param symbol is a symbol code waiting to be converted.
     * @return weather text converted from symbol code.
     */
    private fun symbolToText(symbol: String):String{
        return with(symbol){
            when{
                contains("clearsky_") -> "Klarvær"
                equals("cloudy") -> "Skyet"
                contains("fair_") -> "Lettskyet"
                equals("fog") -> "Tåke"
                equals("heavyrain") -> "Kraftig regn"
                equals("heavyrainandthunder") -> "Kraftig regn og torden"
                contains("heavyrainshowers_") -> "Kraftige regnbyger"
                contains("heavyrainshowersandthunder_") -> "Kraftige regnbyger og torden"
                equals("heavysleet") -> "Kraftig sludd"
                equals("heavysleetandthunder") -> "Kraftig sludd og torden"
                contains("heavysleetshowers_") -> "Kraftige sluddbyger"
                contains("heavysleetshowersandthunder_") -> "Kraftige sluddbyger og torden"
                equals("heavysnow") -> "Kraftig snø"
                equals("heavysnowandthunder") -> "Kraftig snø og torden"
                contains("heavysnowshowers_") -> "Kraftige snøbyger"
                contains("heavysnowshowersandthunder_") -> "Kraftige snøbyger og torden"
                equals("lightrain") -> "Lett regn"
                equals("lightrainandthunder") -> "Lett regn og torden"
                contains("lightrainshowers_") -> "Lette regnbyger"
                contains("lightrainshowersandthunder_") -> "Lette regnbyger og torden"
                equals("lightsleet") -> "Lett sludd"
                equals("lightsleetandthunder") -> "Lett sludd og torden"
                contains("lightsleetshowers_") -> "Lette sluddbyger"
                equals("lightsnow") -> "Lett snø"
                equals("lightsnowandthunder") -> "Lett snø og torden"
                contains("lightsnowshowers_") -> "Lette snøbyger"
                contains("lightssleetshowersandthunder_") -> "Lette sluddbyger og torden"
                contains("lightssnowshowersandthunder_") -> "Lette snøbyger og torden"
                contains("partlycloudy_") -> "Delvis skyet"
                equals("rain") -> "Regn"
                equals("rainandthunder") -> "Regn og torden"
                contains("rainshowers_") -> "Regnbyger"
                contains("rainshowersandthunder_") -> "Regnbyger og torden"
                equals("sleet") -> "Sludd"
                equals("sleetandthunder") -> "Sludd og torden"
                contains("sleetshowers_") -> "Sluddbyger"
                contains("sleetshowersandthunder_") -> "Sluddbyger og torden"
                equals("snow") -> "Snø"
                equals("snowandthunder") -> "Snø og torden"
                contains("snowshowers_") -> "Snøbyger"
                contains("snowshowersandthunder_") -> "Snøbyger og torden"

                else -> "unknown"
            }

        }
    }

    /**
     * This function gets the height of the navigation menu displayed in the application.
     * @param a [AppCompatActivity] of the application.
     * @return the height of navigation menu.
     */
    private fun getMenuHeight(a: AppCompatActivity):Int{
        val resources: Resources = a.resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

}
