package uk.ac.shef.oak.com4510.view

import android.graphics.BitmapFactory
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
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

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
        val descriptionTextView = binding.showImageDescription
        val buttonShowMap = binding.showMapButton
        if (position != -1) {

            val imageData = ImagesAdapter.items[position]

            imageView.setImageBitmap(BitmapFactory.decodeFile(imageData.imageUri))
            titleToolbar.title = imageData.imageTitle
            descriptionTextView.text = imageData.imageDescription
            val entry_id = imageData.entry_id
            val entry = TravelViewModel.

            val fabEdit: FloatingActionButton = binding.fabEdit
            fabEdit.setOnClickListener(View.OnClickListener {
                val action = ShowImageFragmentDirections.actionShowImageFragmentToEditImageFragment(position)
                it.findNavController().navigate(action)
            })

            buttonShowMap.setOnClickListener(View.OnClickListener {
                // val action = ShowImageFragmentDirections.actionShowImageFragmentToEditImageFragment(position)
                // it.findNavController().navigate(action)
            })
        }
    }
}