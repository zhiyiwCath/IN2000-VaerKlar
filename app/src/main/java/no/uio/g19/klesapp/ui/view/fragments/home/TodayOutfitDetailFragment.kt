package no.uio.g19.klesapp.ui.view.fragments.home

import android.os.Bundle
import android.util.Log
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
import no.uio.g19.klesapp.data.model.wardrobe.Plagg
import no.uio.g19.klesapp.ui.view.makeCurrentFragment
import no.uio.g19.klesapp.ui.viewmodel.HomeViewModel
/**
 * This fragment shows a window with a list of clothing items from the recommended outfit.
 * @property viewModel accesses necessary data.
 * @property clothingAdapter is the recyclerview adapter that binds data and views.
 * @property clothingList is a list of the clothing objects according to the recommended outfit.
 * It contains a recyclerview: today_outfit_detail_list.
 */
class TodayOutfitDetailFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var clothingAdapter: ClothingListAdapter
    private lateinit var clothingList: List<Plagg>

    /**
     * This function sets an onClickListener on button_to_to_home.
     * This function sets the recyclerview: today_outfit_detail_list for this fragment,
     * based on data retrieved from the Nowcast API through sanntidsvarsel in viewModel.
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todayoutfitdetail_fragment, container, false)

        val context = activity as AppCompatActivity

        val backbutton = view.findViewById<ImageButton>(R.id.button_to_to_home)

        backbutton.setOnClickListener {
            context.makeCurrentFragment(HomeFragment())
        }

        //get list of clothing from outfit from viewmodel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //Outfit chosen
        viewModel.sanntidsvarsel.observe(viewLifecycleOwner, {
            val outfit = viewModel.velgAntrekk()
            if (outfit != null) {
                Log.v("Outfit", outfit.id)

                //Getting clothing items from chosen outfit
                clothingList = outfit.let { viewModel.getClothingFromOutfit(it) }
                Log.v("Plaggliste", clothingList.toString())

                /* Populate recycleview with clothing items */
                val clothingRecycleView = view.findViewById<RecyclerView>(R.id.today_outfit_detail_list)
                clothingRecycleView.layoutManager = LinearLayoutManager(context)

                clothingAdapter = ClothingListAdapter(clothingList)
                clothingRecycleView.adapter = clothingAdapter
                clothingAdapter.notifyDataSetChanged()
            } else Log.d("OutfitDetailFragment: ", "Ingen outfit funnet")
        })



        return view

    }

}