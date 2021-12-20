package uk.ac.shef.oak.com4510.view.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentGalleryBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import pl.aprilapps.easyphotopicker.*
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.OrderBy


class GalleryFragment : Fragment() {
    private lateinit var easyImage: EasyImage
    private lateinit var binding : FragmentGalleryBinding
    private var orderSwitchToggleValue : Boolean = false
    private var viewModel: TravelViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_gallery, container, false)


        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]

        //Binding recycler view to adapter
        val mRecyclerView = binding.contentCamera.gridRecyclerView
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        val mAdapter = ImagesAdapter(ArrayList<ImageData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

        initEasyImage()

        binding.fabGallery.setOnClickListener{
            //If this device has a camera, allow the user to choose between the camera and gallery
            if (context?.packageManager!!.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                easyImage.openChooser(this)
            else // Otherwise simply open the gallery
                easyImage.openGallery(this)
        }

        //Search bar listeners
        binding.searchBarGallery.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(filter: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(filter: String?): Boolean {
                viewModel!!.search(filter,if (orderSwitchToggleValue) OrderBy.ASC else OrderBy.DESC)
                return false
            }
        })
        binding.OrderByTimeSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                viewModel!!.search(binding.searchBarGallery.query.toString(),if (isChecked) OrderBy.ASC else OrderBy.DESC)
            }
        })

        //Observing searchResults object in the view model
        viewModel!!.searchResults.observe(this, { images ->
            ImagesAdapter.items = images as MutableList<ImageData>
            mAdapter.notifyDataSetChanged()
        })

        viewModel!!.onGoingTrip.observe(this,{
            Log.d("GalleryFragment"," The ongoing trip $it")
            Snackbar.make(binding.root, "The ongoing trip is:$it", Snackbar.LENGTH_LONG).show()
        })

        viewModel!!.setOnGoingTrip(3)

        return binding.root
    }

    //Called when an image is picked
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                viewModel!!.debug_insertArrayMediaFiles(imageFiles)
            }
        })

    }

    private fun initEasyImage() {
        easyImage = EasyImage.Builder(requireActivity())
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
            .build()
    }

}