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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentEditImageBinding
import uk.ac.shef.oak.com4510.model.data.database.ImageDataDao
import uk.ac.shef.oak.com4510.viewModel.ImageApplication
import uk.ac.shef.oak.com4510.viewModel.MyAdapter
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 * Use the [EditImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditImageFragment : Fragment() {
    private val args: EditImageFragmentArgs by navArgs()
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    lateinit var daoObj: ImageDataDao



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= DataBindingUtil.inflate<FragmentEditImageBinding>(inflater,
            R.layout.fragment_edit_image, container, false )
        if (args.position != -1){
            MyAdapter.items[args.position].let{
                binding.editImage.setImageBitmap(it.thumbnail)
                binding.editorToolbar.title = it.imageTitle
                binding.editImageTitle.setText(it.imageTitle)
                binding.editImageDescription.setText(it.imageDescription)
            }
        }
        daoObj = (activity?.application as ImageApplication)
            .databaseObj.imageDataDao()


        return binding.root
    }

    private fun makeButtonListeners(position: Int, binding: FragmentEditImageBinding) {
        var id = MyAdapter.items[position].id
        val cancelButton: Button = binding.cancelButton
        cancelButton.setOnClickListener {
            it.findNavController().popBackStack()
        }

        // Delete button listener
        val deleteButton: Button = binding.deleteButton
        deleteButton.setOnClickListener {
            scope.launch(Dispatchers.IO) {
                async { daoObj.delete(MyAdapter.items[position]) }
                    .invokeOnCompletion {
                        MyAdapter.items.removeAt(position)
                        val intent = Intent()
                            .putExtra("position", position)
                            .putExtra("id", id)
                            .putExtra("deletion_flag", 1)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    }
            }
        }

        // Save button listener
        val saveButton: Button = binding.saveButton
        saveButton.setOnClickListener {
            val descriptionTextInput =
                binding.editImageDescription
            MyAdapter.items[position].imageDescription = descriptionTextInput.text.toString()
            val titleTextInput = binding.editImageTitle
            MyAdapter.items[position].imageTitle = titleTextInput.text.toString()

            scope.launch(Dispatchers.IO) {
                async { daoObj.update(MyAdapter.items[position]) }
                    .invokeOnCompletion {
                        val intent = Intent()
                            .putExtra("position", position)
                            .putExtra("id", id)
                            .putExtra("deletion_flag", 0)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    }
            }
        }
    }


}