package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentNewTripBinding
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel


class NewTripFragment : Fragment() {

    private val viewModel: TravelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNewTripBinding>(inflater,
            R.layout.fragment_new_trip, container, false)


        binding.startTripButton.setOnClickListener{view : View ->
            val tripID = viewModel.create_insert_return_tripID(binding.titleInput.text.toString(),"placeholderCountry", System.currentTimeMillis().toFloat())
            val action : NavDirections = NewTripFragmentDirections.actionNewTripFragmentToTravellingFragment(tripID)
            view.findNavController().navigate(action)
        }



        return binding.root

    }


}