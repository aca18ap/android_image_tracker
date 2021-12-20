package uk.ac.shef.oak.com4510.view.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentFullScreenImageBinding
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FullScreenImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FullScreenImageFragment : Fragment() {
    val args: FullScreenImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentFullScreenImageBinding>(inflater,
            R.layout.fragment_full_screen_image, container, false)

        val viewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]
        if (args.imageID != -1){
            val img = viewModel.getImage(args.imageID)
            binding.fullScreenImage.setImageBitmap(BitmapFactory.decodeFile(img!!.imageUri))

        }


        return binding.root
    }

}