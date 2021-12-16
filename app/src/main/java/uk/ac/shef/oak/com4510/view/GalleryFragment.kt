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



class GalleryFragment : Fragment() {
    private var viewModel: TravelViewModel? = null
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var easyImage: EasyImage

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
        val binding : FragmentGalleryBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_gallery, container, false)
        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]


        mRecyclerView = binding.contentCamera.gridRecyclerView
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(activity, numberOfColumns)
        mAdapter = ImagesAdapter(ArrayList<ImageData>()) as RecyclerView.Adapter<RecyclerView.ViewHolder>
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


        //SearchView functionalities




        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                viewModel?.insertArrayMediaFiles(imageFiles)
                // we tell the adapter that the data is changed
                //mRecyclerView.scrollToPosition(imageFiles.size - 1)
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                super.onImagePickerError(error, source)
            }

            override fun onCanceled(source: MediaSource) {
                super.onCanceled(source)
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