package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentViewTripDetailsBinding
import uk.ac.shef.oak.com4510.view.adapters.ViewPagerAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

private const val NUM_PAGES = 2

class ViewTripDetailsFragment : Fragment() {

    private lateinit var mPager: ViewPager
    private lateinit var binding: FragmentViewTripDetailsBinding
    private val args: ViewTripDetailsFragmentArgs by navArgs()
    private val viewModel: TravelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentViewTripDetailsBinding>(inflater,
            R.layout.fragment_view_trip_details, container, false)
        if (args.tripID != -1){
            displayData(args.imageID,args.tripID)
        }


        /**
         * Tabs binding
         */
        val tabLayout = binding.tablayout
        val viewpager = binding.viewpager
        val pagerAdapter = ViewPagerAdapter(activity!!)

        val viewModel = ViewModelProvider(this)[TravelViewModel::class.java]

        pagerAdapter.addFragment(TripImagesTabFragment(args.tripID))
        pagerAdapter.addFragment(TripEntriesTabFragment(args.tripID))
        viewpager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewpager) { tab, pos ->
            when(pos){
                0->{
                    tab.text = "Images"
                }
                1->{
                    tab.text = "Entries"
                }
            }
        }.attach()


        //TripsAdapter.items[position].first.id

        viewModel.updateEntriesOfTrip(args.tripID)
        viewModel.updateImagesOfTrip(args.tripID)

        /*val ft : FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(R.id.map_container, ExistingTravelFragment())
        ft.commit()*/


        return binding.root

    }


    private fun displayData(imageID: Int,tripID : Int){
        val title = binding.tripTitle
        val location = binding.tripLocation
        val date = binding.tripDate
        //val thumbnail = binding.tripThumbnail

        if (imageID != -1){
            val tripData = viewModel.getTrip(tripID) //TripsAdapter.items[position].first
            //val imageData = viewModel.getImage(imageID)// TripsAdapter.items[position].second

            title.text = tripData?.title
            location.text = tripData?.country
            date.text = tripData?.trip_timestamp.toString()
            //if (imageData != null){
               // thumbnail.setImageBitmap((TripsAdapter.items[position].second?.thumbnail))
            //}



        }
    }

}