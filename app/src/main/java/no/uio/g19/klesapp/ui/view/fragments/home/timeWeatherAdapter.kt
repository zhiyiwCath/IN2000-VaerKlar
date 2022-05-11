package no.uio.g19.klesapp.ui.view.fragments.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.viewmodel.HourForcastHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * This is a Recyclerview adapter which provides binding from
 * @param dataSet, a MutableList<HourForcastHolder> to views on [HomeFragment]
 * that are displayed within a recyclerview.
 */
class TimeWeatherAdapter(private val dataSet: MutableList<HourForcastHolder>
) :
        RecyclerView.Adapter<TimeWeatherAdapter.ViewHolder>() {

    //private val dataSet : MutableList<HourForcastHolder> = weatherHolder.weatherList

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hourText: TextView = view.findViewById(R.id.time_hour)
        val weatherSymbol: ImageView = view.findViewById(R.id.weather_symbol)
        val hourTemp: TextView = view.findViewById(R.id.hour_temp)
        val context: Context = hourTemp.context


    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.time_weather_element, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val weatherNext1Hrs = dataSet[position]
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val symbolFil: String =  weatherNext1Hrs.symbolCode
        val resId : Int =  viewHolder.context.resources.getIdentifier(symbolFil, "drawable", viewHolder.context.packageName)
        Log.v("Klesapp", "SymbolFil: $symbolFil ")

        val pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale("no","NO","nb_NO" ))
        val date : Date? = simpleDateFormat.parse(weatherNext1Hrs.time)
        val cal = Calendar.getInstance()
        cal.time = date
        val hours = cal.get(Calendar.HOUR_OF_DAY).toString()

        viewHolder.hourText.text = hours

        Glide.with(viewHolder.weatherSymbol.context).load(resId).placeholder(R.drawable.lightrain).override(100, 100).into(viewHolder.weatherSymbol)

        viewHolder.hourTemp.text = viewHolder.context.getString(R.string.viewHolder_hour_Temp_text, weatherNext1Hrs.temp)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
