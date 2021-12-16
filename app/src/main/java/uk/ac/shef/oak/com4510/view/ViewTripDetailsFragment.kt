package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentViewTripDetailsBinding
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

class ViewTripDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentViewTripDetailsBinding>(inflater,
            R.layout.fragment_view_trip_details, container, false)

        val viewModel = ViewModelProvider(this)[TravelViewModel::class.java]


        return binding.root

    }
}