package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentViewPastTripsBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPastTripsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewPastTripsFragment : Fragment() {
    private var viewModel: TravelViewModel? = null
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentViewPastTripsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_view_past_trips, container, false)
        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]


        mRecyclerView = binding.contentTrips.tripGridRecyclerView
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        mAdapter = ImagesAdapter(ArrayList<ImageData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter



        return binding.root
    }


}