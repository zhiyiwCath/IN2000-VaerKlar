package no.uio.g19.klesapp.ui.view.fragments.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.data.model.placesDB.entities.PlaceRoomEntity
/**
 * This is a Recyclerview adapter which provides binding from
 * @param dataSet, a List<PlaceRoomEntity> and
 * @param clickListener, a LocationListAdapterListener
 * to views on [ChooseLocationFragment] that are displayed within a recyclerview.
 */
class LocationListAdapter(private val dataSet: List<PlaceRoomEntity>,
                          private val clickListener : LocationListAdapterListener
) :
        RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, clickListener: LocationListAdapterListener) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.loc_name)
        private val homeButton: ImageButton = view.findViewById(R.id.button_loc_to_home)
        private val locationElementButton = view.findViewById<LinearLayout>(R.id.location_list_item)

        init {
            homeButton.setOnClickListener {
                clickListener.homeButtonOnClick(it, adapterPosition)
            }

            locationElementButton.setOnClickListener {
                clickListener.locationElementOnClick(it, adapterPosition)
            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.location_list_element, viewGroup, false)

        return ViewHolder(view, clickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.placeName.text = dataSet[position].name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
