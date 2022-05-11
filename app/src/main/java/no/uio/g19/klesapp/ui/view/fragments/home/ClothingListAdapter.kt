package no.uio.g19.klesapp.ui.view.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.data.model.wardrobe.Plagg

/**
 * This is a Recyclerview adapter which provides binding from
 * @param clothingList, a List<Plagg> to views on [TodayOutfitDetailFragment]
 * that are displayed within a recyclerview.
 * @property context provides the fragment's context.
 */
class ClothingListAdapter (private val clothingList: List<Plagg>) :
    RecyclerView.Adapter<ClothingListAdapter.ViewHolder>(){
    private lateinit var context: Context

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.clothing_name)

        //val infoTextView: TextView
        val iconView : ImageView = view.findViewById(R.id.clothing_icon)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.clothing_list_element, viewGroup, false)

        context = viewGroup.context
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = clothingList[position]

        //Setting clothing name to textview
        viewHolder.nameTextView.text = currentItem.navn

        //viewHolder.infoTextView.text = currentItem.info

        //Setting drawable resources to imageview
        val drawableIconName = currentItem.ikon
        val context = viewHolder.itemView.context
        val resourceId = context.resources.getIdentifier(drawableIconName, "drawable", context.packageName)

        viewHolder.iconView.setImageResource(resourceId)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = clothingList.size



}