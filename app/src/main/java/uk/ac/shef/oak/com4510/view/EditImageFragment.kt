package uk.ac.shef.oak.com4510.view

import android.app.Activity
import android.content.Intent
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
import com.google.android.material.textfield.TextInputEditText
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentEditImageBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageDataDao
import uk.ac.shef.oak.com4510.viewModel.ImageApplication
import uk.ac.shef.oak.com4510.viewModel.MyAdapter
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [EditImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditImageFragment : Fragment() {
    private val args: EditImageFragmentArgs by navArgs()
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    lateinit var binding : FragmentEditImageBinding
    private val model: TravelViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentEditImageBinding>(inflater,
            R.layout.fragment_edit_image, container, false )
        if (args.position != -1){
            MyAdapter.items[args.position].let{
                binding.editImage.setImageBitmap(it.thumbnail)
                binding.editorToolbar.title = it.imageTitle
                binding.editImageTitle.setText(it.imageTitle)
                binding.editImageDescription.setText(it.imageDescription)
            }
        }
        makeButtonListeners(args.position)

        return binding.root
    }

    private fun makeButtonListeners(position: Int) {
        val cancelButton: Button = binding.cancelButton
        cancelButton.setOnClickListener {
            it.findNavController().popBackStack()
        }

        // Delete button listener
        val deleteButton: Button = binding.deleteButton
        deleteButton.setOnClickListener {
            model.deleteImageInDatabase(MyAdapter.items[position])
            it.findNavController().popBackStack()
        }

        // Save button listener
        val saveButton: Button = binding.saveButton
        saveButton.setOnClickListener {
            val descriptionTextInput = binding.editImageDescription
            val titleTextInput = binding.editImageTitle

            model.updateImageInDatabase(
                MyAdapter.items[position],titleTextInput.text.toString(),
                descriptionTextInput.text.toString())
            it.findNavController().popBackStack()
        }
    }


}