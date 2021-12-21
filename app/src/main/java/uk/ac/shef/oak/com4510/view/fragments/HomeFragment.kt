package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentHomeBinding
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: TravelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home, container, false)

        viewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]

        //Navigating to gallery
        binding.goToImagesButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_galleryFragment)
        }

        //Navigating to new trip
        binding.newTripButton.setOnClickListener{view : View ->
            val tripID = viewModel.getOnGoingTrip()
            Log.d("HomeFragment", "onGoingTrip: $tripID")
            if (tripID == -1) { // No ongoing trip
                view.findNavController().navigate(R.id.action_homeFragment_to_newTripFragment)
            } else { // Ongoing trip
                val action : NavDirections = HomeFragmentDirections.actionHomeFragmentToTravellingFragment(tripID)
                view.findNavController().navigate(action)
            }

        }
        //Navigating to pastTrips
        binding.viewTripsButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_viewPastTripsFragment)
        }

        /*
        binding.debugButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_debugFragment)
        }
        */
        return binding.root
    }

}