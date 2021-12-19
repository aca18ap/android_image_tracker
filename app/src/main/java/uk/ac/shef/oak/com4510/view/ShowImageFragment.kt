package uk.ac.shef.oak.com4510.view

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentShowImageBinding
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ShowImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowImageFragment : Fragment() {
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var viewModel : TravelViewModel
    private val args: ShowImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentShowImageBinding>(inflater,
            R.layout.fragment_show_image, container, false)
        viewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]
        if (args.position != -1){
            displayData(args.position, binding)
        }

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun displayData(position: Int, binding: FragmentShowImageBinding){
        val imageView = binding.showImage
        val titleToolbar = binding.showToolbar
        val timeTextView = binding.showImageTime
        val descriptionTextView = binding.showImageDescription
        val sensorsTextView = binding.showSensors
        val buttonShowMap = binding.showMapButton
        var entryID = -1
        var tripID = -1
        if (position != -1) {

            val imageData = ImagesAdapter.items[position]

            var sensorText = ""

            imageView.setImageBitmap(BitmapFactory.decodeFile(imageData.imageUri))
            titleToolbar.title = imageData.imageTitle
            descriptionTextView.text = imageData.imageDescription

            if (imageData.entry_id != null) {
                entryID = imageData.entry_id!!
                val entry = viewModel.getEntry(entryID)
                val timestamp = entry!!.entry_timestamp
                val pressure = entry!!.entry_pressure
                val temperature = entry!!.entry_temperature
                tripID = entry.trip_id

                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val dateTime = formatter.format(Date(timestamp))
                timeTextView.text = "Taken on: $dateTime"

                if (pressure != null) sensorText += "\nPressure: $pressure mbar"
                if (temperature != null) sensorText += "\nTemperature: $temperature C"
                if (sensorText == "") sensorText = "No sensor data found"
                buttonShowMap.visibility = View.VISIBLE
            }

            if (sensorText == "") sensorText = "This image is not associated with a trip."

            sensorsTextView.text = sensorText

            val fabEdit: FloatingActionButton = binding.fabEdit
            fabEdit.setOnClickListener(View.OnClickListener {
                val action = ShowImageFragmentDirections.actionShowImageFragmentToEditImageFragment(position)
                it.findNavController().navigate(action)
            })

            buttonShowMap.setOnClickListener(View.OnClickListener {
                val action = ShowImageFragmentDirections.actionShowImageFragmentToExistingTravelFragment(tripID, entryID)
                it.findNavController().navigate(action)
            })
        }
    }
}