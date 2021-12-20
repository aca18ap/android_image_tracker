package uk.ac.shef.oak.com4510.view.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentViewPastTripsBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.model.data.database.TripData
import uk.ac.shef.oak.com4510.view.adapters.TripsAdapter
import uk.ac.shef.oak.com4510.viewModel.OrderBy
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel


class ViewPastTripsFragment : Fragment() {
    private var viewModel: TravelViewModel? = null
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mRecyclerView: RecyclerView

    companion object {
        val ADAPTER_ITEM_DELETED = 100
        private const val REQUEST_READ_EXTERNAL_STORAGE = 2987
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 7829
        private const val REQUEST_CAMERA_CODE = 100

    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pos = result.data?.getIntExtra("position", -1)!!
                val id = result.data?.getIntExtra("id", -1)!!
                val del_flag = result.data?.getIntExtra("deletion_flag", -1)!!
                if (pos != -1 && id != -1) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        when(del_flag){
                            -1, 0 -> mAdapter.notifyDataSetChanged()
                            else -> mAdapter.notifyItemRemoved(pos)
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentViewPastTripsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_view_past_trips, container, false)

        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]

        mRecyclerView = binding.contentTrips.tripGridRecyclerView
        val numberOfColumns = 2
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        mAdapter = TripsAdapter(ArrayList<TripData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter


        binding.searchBarTrips.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel!!.search(p0,OrderBy.NOPARTICULARORDER)
                return false
            }
        })




        viewModel!!.allTripsObservable.observe(this, Observer<List<Pair<TripData,ImageData?>>>{ trips ->
            Log.d("ViewTripsFragment",trips.toString())
            TripsAdapter.items = trips as MutableList<Pair<TripData,ImageData?>>
            mAdapter.notifyDataSetChanged()
        })


        return binding.root
    }


}