package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentHomeBinding


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home, container, false)

        //Navigating to gallery
        binding.goToImagesButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_galleryFragment)
        }

        //Navigating to new trip
        binding.newTripButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_newTripFragment)
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