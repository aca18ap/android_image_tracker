package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentTripImagesTabBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

class TripImagesTabFragment(tripID: Int) : Fragment() {
    private var viewModel: TravelViewModel? = null
    private val tripID = tripID
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentTripImagesTabBinding>(inflater,
            R.layout.fragment_trip_images_tab, container, false)
        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        val numberOfColumns = 4
        val mRecyclerView = binding.contentCamera.gridRecyclerView
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        val mAdapter = ImagesAdapter(ArrayList<ImageData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

        viewModel!!.imagesOfTrip.observe(this, Observer<MutableList<ImageData>>{ images ->
            ImagesAdapter.items = images as MutableList<ImageData>
            mAdapter.notifyDataSetChanged()
        })

        Log.d("TripID:", tripID.toString())
        viewModel!!.updateImagesOfTrip(tripID)

        return binding.root
    }


}