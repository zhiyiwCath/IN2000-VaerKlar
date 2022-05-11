package no.uio.g19.klesapp.ui.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.view.makeCurrentFragment
import no.uio.g19.klesapp.ui.viewmodel.SettingsViewModel

/**
 * This fragment shows a window with user settings.
 * @property settingsViewModel accesses necessary data.
 * It contains three buttons: omOss, bruksAnvisning, and delete_data.
 *
 */
class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    /**
     * This function contains the logic behind the three buttons.
     * Each button has a setOnClickListener, which leads to different fragments, or events.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)

        val context = activity as AppCompatActivity

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        val aboutUs = view?.findViewById<RelativeLayout>(R.id.omOss)
        val userManual = view?.findViewById<RelativeLayout>(R.id.bruksAnvisning)
        val delData = view?.findViewById<RelativeLayout>(R.id.delete_data)

        aboutUs?.setOnClickListener{
            context.makeCurrentFragment(OmOssFragment())
        }

        userManual?.setOnClickListener{
            context.makeCurrentFragment(BruksanvisningFragment())
        }

        delData?.setOnClickListener {
            activity?.let {
                Snackbar.make(
                    it.findViewById(R.id.settings_fragment),
                    "Slett alle lagrede steder?",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Ja") {
                        settingsViewModel.clearPLaceDatabase()

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
            settingsViewModel.clearPLaceDatabase()
        }


      return view

    }

}