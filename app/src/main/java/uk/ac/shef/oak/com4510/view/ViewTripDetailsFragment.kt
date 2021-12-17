package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentViewTripDetailsBinding
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import uk.ac.shef.oak.com4510.viewModel.TripsAdapter
import uk.ac.shef.oak.com4510.viewModel.ViewPagerAdapter

private const val NUM_PAGES = 2

class ViewTripDetailsFragment : Fragment() {

    private lateinit var mPager: ViewPager
    private lateinit var binding: FragmentViewTripDetailsBinding
    private val args: ViewTripDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentViewTripDetailsBinding>(inflater,
            R.layout.fragment_view_trip_details, container, false)
        if (args.position != -1){
            displayData(args.position)
        }

        val tabLayout = binding.tablayout
        val viewpager = binding.viewpager
        val mAdapter = ViewPagerAdapter(childFragmentManager, lifecycle, position)

        viewpager.adapter = mAdapter
        TabLayoutMediator(tabLayout, viewpager){tab, position->
            when(position){
                0->{
                    tab.text=="Images"
                }
                1->{
                    tab.text="Entries"
                }
            }
        }.attach()

        val viewModel = ViewModelProvider(this)[TravelViewModel::class.java]

        return binding.root

    }


    private fun displayData(position: Int){
        val title = binding.tripTitle
        val location = binding.tripLocation
        val date = binding.tripDate
        val thumbnail = binding.tripThumbnail
        if (position != -1){
            val tripData = TripsAdapter.items[position].first
            val imageData = TripsAdapter.items[position].second
            title.text = tripData.title
            location.text = tripData.country
            date.text = tripData.trip_timestamp.toString()
            if (imageData != null){
                thumbnail.setImageBitmap((TripsAdapter.items[position].second?.thumbnail))
            }



        }
    }

}