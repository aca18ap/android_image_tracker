package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentEditImageBinding
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [EditImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditImageFragment : Fragment() {
    private val args: EditImageFragmentArgs by navArgs()
    lateinit var binding : FragmentEditImageBinding
    private val viewmodel: TravelViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentEditImageBinding>(inflater,
            R.layout.fragment_edit_image, container, false )

        //Assigning view texts
        if (args.imageID == -1){
            throw Exception("Somehow the EditImageFragment was navigated to it without being passed an imageID")
        }
        val imageToEdit = viewmodel.getImage(args.imageID)!!

        binding.editImage.setImageBitmap(imageToEdit.thumbnail)
        binding.editorToolbar.title = imageToEdit.imageTitle
        binding.editImageTitle.setText(imageToEdit.imageTitle)
        binding.editImageDescription.setText(imageToEdit.imageDescription)

        makeButtonListeners(imageToEdit)

        return binding.root
    }


    /**
     * Connects button listeners to the three buttons in the view
     */
    private fun makeButtonListeners(imageData: ImageData) {
        val cancelButton: Button = binding.cancelButton
        cancelButton.setOnClickListener {
            it.findNavController().popBackStack()
        }

        // Delete button listener
        val deleteButton: Button = binding.deleteButton
        deleteButton.setOnClickListener {
            viewmodel.deleteImageFromDatabase(imageData)
            // Pop back stack twice to get back to the gallery
            it.findNavController().popBackStack()
            it.findNavController().popBackStack()
        }

        // Save button listener
        val saveButton: Button = binding.saveButton
        saveButton.setOnClickListener {
            val descriptionTextInput = binding.editImageDescription
            val titleTextInput = binding.editImageTitle

            viewmodel.updateImageInDatabase(
                imageData,titleTextInput.text.toString(),
                descriptionTextInput.text.toString())
            it.findNavController().popBackStack()
        }
    }


}