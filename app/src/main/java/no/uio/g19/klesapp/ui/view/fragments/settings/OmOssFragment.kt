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
 * This fragment shows a window with text describing the application's development.
 */
class OmOssFragment : Fragment() {

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.om_oss_fragment, container, false)
        val context = activity as AppCompatActivity
        val backbutton = view.findViewById<ImageButton>(R.id.home_button_OmOss)

        backbutton.setOnClickListener {
            context.makeCurrentFragment(SettingsFragment())
        }
        return view
    }




}