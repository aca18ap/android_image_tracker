package com.com4510.team01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.com4510.team01.databinding.FragmentShowImageBinding
import com.com4510.team01.model.data.database.ImageDataDao
import com.com4510.team01.view.EditActivity
import com.com4510.team01.view.ImageApplication
import com.com4510.team01.view.MyAdapter
import com.google.android.gms.maps.MapView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * A simple [Fragment] subclass.
 * Use the [ShowImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowImageFragment : Fragment() {
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    lateinit var daoObj: ImageDataDao
    val args: ShowImageFragmentArgs by navArgs()
    private var position: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentShowImageBinding>(inflater, R.layout.fragment_show_image, container, false)
        if (args.pos != null){
            displayData(args.pos, binding)
        }

        return binding.root
    }

    private fun displayData(position: Int, binding: FragmentShowImageBinding){
        val imageView = binding.showImage
        val titleToolbar = binding.showToolbar
        val sensorsTextView = binding.showSensors
        val mapView = binding.showMap
        val descriptionTextView = binding.showImageDescription

        val buttonShowDescription = binding.showDescriptionButton
        val buttonShowMap = binding.showMapButton
        val buttonShowSensors = binding.showSensorsButton
        val viewsList = listOf(mapView, descriptionTextView, sensorsTextView)
        if (position != -1) {

            val imageData = MyAdapter.items[position]

            imageView.setImageBitmap(MyAdapter.items[position].thumbnail!!)
            titleToolbar.title = MyAdapter.items[position].imageTitle
            descriptionTextView.text = MyAdapter.items[position].imageDescription


            val fabEdit: FloatingActionButton = binding.fabEdit
            fabEdit.setOnClickListener(View.OnClickListener {
                //TODO link to navigation from showimage to editimage
            })

            buttonShowDescription.setOnClickListener(View.OnClickListener {
                toggleDescription(descriptionTextView, viewsList)
            })
            buttonShowMap.setOnClickListener(View.OnClickListener {
                toggleDescription(mapView, viewsList)
            })
            buttonShowSensors.setOnClickListener(View.OnClickListener {
                toggleDescription(sensorsTextView, viewsList)
            })
        }
    }

    private fun toggleDescription(elem : View, viewsList: List<View>){
        for (view in viewsList){
            if (view != elem) {
                view.visibility = View.GONE
            }
        }
        if (elem.visibility == View.VISIBLE){
            elem.visibility = View.GONE
        }else{
            elem.visibility = View.VISIBLE
        }
    }
}