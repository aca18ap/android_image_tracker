package com.com4510.team01.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.com4510.team01.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.com4510.team01.model.data.database.ImageData
import com.com4510.team01.viewModel.TravelViewModel
import kotlinx.coroutines.*
import pl.aprilapps.easyphotopicker.*

class MainActivity : AppCompatActivity() {

    private var viewModel: TravelViewModel? = null
    private lateinit var mAdapter: Adapter<RecyclerView.ViewHolder>
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        mRecyclerView = findViewById(R.id.grid_recycler_view)
        // set up the RecyclerView
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(this, numberOfColumns)

        //Current issue with the adapter being initialized empty since (my current guess), the coroutine didn't have time to update the viewModel
        mAdapter = MyAdapter(ArrayList<ImageData>()) as Adapter<RecyclerView.ViewHolder>

        mRecyclerView.adapter = mAdapter

        // required by Android 6.0 +
        checkPermissions(applicationContext)
        initEasyImage()

        // the floating button that will allow us to get the images from the Gallery
        val fabGallery: FloatingActionButton = findViewById(R.id.fab_gallery)
        fabGallery.setOnClickListener(View.OnClickListener {
            easyImage.openChooser(this@MainActivity)
        })
        //Binds the adapter to the viewModel. To refactor: Place this code in the xml.
        viewModel!!.getImageList().observe(this, Observer<MutableList<ImageData>>{ images ->
            MyAdapter.items = images
        })
    }

    /**
     * it initialises EasyImage
     */
    private fun initEasyImage() {
        easyImage = EasyImage.Builder(this)
//        .setChooserTitle("Pick media")
//        .setFolderName(GALLERY_DIR)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
//        .setCopyImagesToPublicGalleryFolder(true)
            .build()
    }


    /**
     * insert a ImageData into the database
     * Called for each image the user adds by clicking the fab button
     * Then retrieves the same image so we can have the automatically assigned id field
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    viewModel?.insertArrayMediaFiles(imageFiles)
                    // we tell the adapter that the data is changed
                    mAdapter.notifyDataSetChanged()
                    mRecyclerView.scrollToPosition(imageFiles.size - 1)
                }

                override fun onImagePickerError(error: Throwable, source: MediaSource) {
                    super.onImagePickerError(error, source)
                }

                override fun onCanceled(source: MediaSource) {
                    super.onCanceled(source)
                }
            })
    }

    /**
     * check permissions are necessary starting from Android 6
     * if you do not set the permissions, the activity will simply not work and you will be probably baffled for some hours
     * until you find a note on StackOverflow
     * @param context the calling context
     */
    private fun checkPermissions(context: Context) {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    val alertBuilder: AlertDialog.Builder =
                        AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(android.R.string.ok,
                        DialogInterface.OnClickListener { _, _ ->
                            ActivityCompat.requestPermissions(
                                context as Activity, arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ), REQUEST_READ_EXTERNAL_STORAGE
                            )
                        })
                    val alert: AlertDialog = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
            }
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("Writing external storage permission is necessary")
                    alertBuilder.setPositiveButton(android.R.string.ok,
                        DialogInterface.OnClickListener { _, _ ->
                            ActivityCompat.requestPermissions(
                                context as Activity, arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ), REQUEST_WRITE_EXTERNAL_STORAGE
                            )
                        })
                    val alert: AlertDialog = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_WRITE_EXTERNAL_STORAGE
                    )
                }
            }
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_CODE
                );
            }
        }
    }
}