package no.uio.g19.klesapp.ui.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.view.makeCurrentFragment

/**
 * This fragment shows a window with text describing how the application can be used,
 * also known as a user guide.
 */
class BruksanvisningFragment : Fragment() {

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.bruksanvisning_fragment, container, false)
        val context = activity as AppCompatActivity
        val backButton = view.findViewById<ImageButton>(R.id.home_button_userManual)

        backButton.setOnClickListener {
            context.makeCurrentFragment(SettingsFragment())
        }

        return view
    }


}