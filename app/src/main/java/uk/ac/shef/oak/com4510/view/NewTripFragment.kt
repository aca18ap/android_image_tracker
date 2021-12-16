package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentNewTripBinding


class NewTripFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNewTripBinding>(inflater,
            R.layout.fragment_new_trip, container, false)

        binding.startTripButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_newTripFragment_to_travellingFragment)
        }

        return binding.root

    }


}