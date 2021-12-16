package uk.ac.shef.oak.com4510.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentGalleryBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import pl.aprilapps.easyphotopicker.*
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter


class GalleryFragment : Fragment() {
    private val viewModel: TravelViewModel by activityViewModels()
    private lateinit var easyImage: EasyImage
    private lateinit var binding : FragmentGalleryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_gallery, container, false)

        val mRecyclerView = binding.contentCamera.gridRecyclerView
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        val mAdapter = ImagesAdapter(ArrayList<ImageData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

        // required by Android 6.0 +
        initEasyImage()

        binding.fabGallery.setOnClickListener{
            easyImage.openChooser(this)
        }

        binding.searchBarGallery.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(filter: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(filter: String?): Boolean {
                viewModel!!.search(filter)
                return false
            }
        })

        viewModel!!.searchResults.observe(this, Observer<MutableList<ImageData>>{ images ->
            ImagesAdapter.items = images as MutableList<ImageData>
            mAdapter.notifyDataSetChanged()
        })
        viewModel!!.initObservable() // Populate the imageList observable with all the images in the database

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                viewModel.insertArrayMediaFiles(imageFiles)
            }
        })
    }


    private fun initEasyImage() {
        easyImage = EasyImage.Builder(requireActivity())
//        .setChooserTitle("Pick media")
//        .setFolderName(GALLERY_DIR)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
//        .setCopyImagesToPublicGalleryFolder(true)
            .build()
    }

}