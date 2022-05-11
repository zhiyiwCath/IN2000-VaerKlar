package no.uio.g19.klesapp.ui.view.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.viewmodel.DailyWeatherHolder


/**
 * This is a Recyclerview adapter which provides binding from
 * @param days, an Array<DailyWeatherHolder> to views on [HomeFragment]
 * that are displayed within a recyclerview.
 */
class DailyWeatherAdapter (private val days: Array<DailyWeatherHolder>) :
        RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weekday: TextView = view.findViewById(R.id.daily_weather_day)
        val date: TextView = view.findViewById(R.id.daily_weather_date)
        val forecast: TextView = view.findViewById(R.id.daily_weather_forecast)
        val symbol: ImageView = view.findViewById(R.id.daily_weather_symbol)
        val rain: TextView = view.findViewById(R.id.daily_weather_rain)
        val context: Context = forecast.context!!

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.daily_weather_element, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (days[position]) {
            holder.weekday.text = day
            holder.date.text = holder.context.getString(R.string.daily_weather_adap_hlder_dte, date, (month + 1))
            if (precipitation > 0){
                holder.rain.text = holder.context.getString(R.string.daily_weather_adap_hlder_rain, precipitation)
            }
            holder.forecast.text = holder.context.getString(R.string.daily_weather_adap_hlder_forecast,tempMin, tempMax)
            val resId : Int =  holder.context.resources.getIdentifier(weatherSymbol, "drawable", holder.context.packageName)
            Glide.with(holder.context)
                    .load(resId)
                    .placeholder(R.drawable.lightrain)
                    .override(80, 80)
                    .into(holder.symbol)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = days.size

}
