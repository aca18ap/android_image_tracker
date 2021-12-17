package uk.ac.shef.oak.com4510.view

import android.os.Bundle
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
import uk.ac.shef.oak.com4510.databinding.FragmentGalleryBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

class TripImagesTabFragment : Fragment() {
    private lateinit var binding : FragmentGalleryBinding
    private var viewModel: TravelViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_trip_images_tab, container, false)

        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        val numberOfColumns = 4
        val mRecyclerView = binding.contentCamera.gridRecyclerView
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        val mAdapter = ImagesAdapter(ArrayList<ImageData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

        viewModel!!.searchResults.observe(this, Observer<List<ImageData>>{ images ->
            ImagesAdapter.items = images as MutableList<ImageData>
            mAdapter.notifyDataSetChanged()
        })
        viewModel!!.initImagesList() // Populate the imageList observable with all the images in the database


        return binding.root
    }

}