package no.uio.g19.klesapp.ui.view.fragments.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.uio.g19.klesapp.R
import no.uio.g19.klesapp.ui.view.fragments.home.HomeFragment
import no.uio.g19.klesapp.ui.view.makeCurrentFragment
import no.uio.g19.klesapp.ui.viewmodel.HomeViewModel

/**
 * This fragment shows the list of added locations by the user.
 * A user can choose a location that they want to view the weather forecast of.
 * They can also add or remove locations from the list.
 */
class ChooseLocationFragment : Fragment() {
    private lateinit var homeViewModel : HomeViewModel

    /**
     * This function contains the logic behind two buttons.
     * Each button has a setOnClickListener, which leads to different events.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = activity as AppCompatActivity
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.choose_location_fragment, container, false)


        
        val addButton = view.findViewById<ImageButton>(R.id.button_add_loc)



        addButton.setOnClickListener {
            context.makeCurrentFragment(SearchNewLoctionFragment())
        }

        return view

    }

    /**
     * This function sets the recyclerview: locations for this fragment.
     * Recyclerview creates error "E/RecyclerView: No adapter attached; skipping layout", we
     * believe this is because the LiveData that is observed uses some time to emit its value
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = activity as AppCompatActivity

        val recyclerView = view.findViewById<RecyclerView>(R.id.locations)
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(context )


            homeViewModel.allPlaces.observe(viewLifecycleOwner, { places->
                recyclerView.layoutManager = LinearLayoutManager(context )
                recyclerView.adapter = LocationListAdapter(places,
                    object : LocationListAdapterListener {
                        override fun homeButtonOnClick(view: View, position : Int) {
                            homeViewModel.setCurrentLocation(places[position])
                            context.makeCurrentFragment(HomeFragment())
                        }
                        override fun locationElementOnClick(view: View, position : Int) {
                            homeViewModel.setCurrentLocation(places[position])
                            context.makeCurrentFragment(HomeFragment())
                        }
                    })
            })

        }
    }

}