package no.uio.g19.klesapp.ui.view.fragments.location

import android.view.View

/**
 * Interface for LocationListAdapterListener
 */
interface LocationListAdapterListener {
    /**
     * These two functions both takes the user back to HomeFragment with chosen location,
     * depending on what the user has clicked on. The user may choose to click on the
     * whole location-element, or the arrow beside it.
     * @param view is the current view
     * @param position is the chosen position
     */
    fun homeButtonOnClick(view: View, position : Int)
    fun locationElementOnClick(view: View, position : Int)
}