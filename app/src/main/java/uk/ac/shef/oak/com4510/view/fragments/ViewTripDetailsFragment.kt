package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentViewTripDetailsBinding
import uk.ac.shef.oak.com4510.util.decodeSampledBitmapFromResource
import uk.ac.shef.oak.com4510.view.adapters.ViewPagerAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import java.text.SimpleDateFormat
import java.util.*

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

        val viewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]

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


        viewModel.updateEntriesOfTrip(args.tripID)
        viewModel.updateImagesOfTrip(args.tripID)




        return binding.root

    }


    private fun displayData(imageID: Int,tripID : Int){
        val title = binding.tripTitle
        //val location = binding.tripLocation
        val date = binding.tripDate
        val thumbnail = binding.tripThumbnail
        val tripData = viewModel.getTrip(tripID) //TripsAdapter.items[position].first

        if (imageID != -1){
            thumbnail.setImageBitmap(decodeSampledBitmapFromResource(viewModel.getImage(imageID)!!.imageUri, 150,150))
        }
        title.text = tripData?.title
        //location.text = tripData?.country
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateTime = formatter.format(Date(tripData?.trip_timestamp!!.toLong()))
        date.text = dateTime

    }

}