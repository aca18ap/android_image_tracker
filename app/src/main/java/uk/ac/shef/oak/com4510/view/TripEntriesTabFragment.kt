package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentTripEntriesTabBinding
import uk.ac.shef.oak.com4510.model.data.database.EntryData
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.viewModel.EntriesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel


class TripEntriesTabFragment(tripID: Int) : Fragment() {

    private var viewModel: TravelViewModel? = null
    private val tripID: Int = tripID
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentTripEntriesTabBinding>(inflater,
        R.layout.fragment_trip_entries_tab, container, false)

        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        val mRecyclerView = binding.contentEntries.entryGridRecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        val mAdapter = EntriesAdapter(ArrayList<EntryData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

        viewModel!!.entriesOfTrip.observe(this, Observer<MutableList<Pair<EntryData,List<ImageData>>>>{ pair ->
            EntriesAdapter.items = pair
            mAdapter.notifyDataSetChanged()
        })
        viewModel!!.updateEntriesOfTrip(tripID)

        return binding.root

    }

}