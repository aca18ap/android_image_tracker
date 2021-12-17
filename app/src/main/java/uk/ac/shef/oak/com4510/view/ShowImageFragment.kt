package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentShowImageBinding
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter
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
    private val args: ShowImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentShowImageBinding>(inflater,
            R.layout.fragment_show_image, container, false)
        if (args.position != -1){
            displayData(args.position, binding)
        }

        binding.lifecycleOwner = this

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

            val imageData = ImagesAdapter.items[position]

            imageView.setImageBitmap(imageData.thumbnail!!)
            titleToolbar.title = imageData.imageTitle
            descriptionTextView.text = imageData.imageDescription



            val fabEdit: FloatingActionButton = binding.fabEdit
            fabEdit.setOnClickListener(View.OnClickListener {
                val action = ShowImageFragmentDirections.actionShowImageFragmentToEditImageFragment(position)
                it.findNavController().navigate(action)
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