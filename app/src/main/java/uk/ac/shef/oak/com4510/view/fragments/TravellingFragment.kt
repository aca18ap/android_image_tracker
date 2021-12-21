package uk.ac.shef.oak.com4510.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import pl.aprilapps.easyphotopicker.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentTravellingBinding
import uk.ac.shef.oak.com4510.service.LocationService
import uk.ac.shef.oak.com4510.util.decodeSampledBitmapFromResource
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

/**
 * A fragment containing the current visit
 */
class TravellingFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private val args: TravellingFragmentArgs by navArgs()
    private lateinit var mMap: GoogleMap
    private lateinit var easyImage: EasyImage
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var ctx: Context
    private lateinit var intent: Intent
    private var mLine: Polyline? = null

    companion object {
        private lateinit var viewModel: TravelViewModel
        private lateinit var binding : FragmentTravellingBinding
        private var activity: FragmentActivity? = null
        private var tripID: Int = -1

        /**
         * Activity getter
         *
         * @return activity the parent activity
         */
        fun getActivity(): FragmentActivity? {
            return activity
        }

        /**
         * Activity setter
         *
         * @param newActivity an activity
         */
        fun setActivity(newActivity: FragmentActivity) {
            activity = newActivity
        }

        /**
         * ViewModel getter
         *
         * @return viewModel the associated TravelViewModel
         */
        fun getViewModel(): TravelViewModel {
            return viewModel
        }

        /**
         * Trip ID getter
         *
         * @return tripID the ID of the active trip
         */
        fun getTripId(): Int {
            return tripID
        }
    }

    /**
     * Called when the view is instantiated
     *
     * Sets up bindings and context
     *
     * @return binding.root
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tripID = args.tripID
        Log.i("Current Trip ID", "$tripID")

        setActivity(requireActivity())
        setContext(requireActivity())

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_travelling, container, false)
        viewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        startLocationUpdates()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // required by Android 6.0 +
        initEasyImage()

        binding.fabGallery.setOnClickListener{
            //Only open the gallery/camera if the trip has an entry associated with it
            if (viewModel.tripHasEntries(tripID))
                easyImage.openChooser(this)
            else
                Snackbar.make(binding.root, "Please wait a moment for us to find your location.", Snackbar.LENGTH_LONG).show()
        }

        binding.tripEndBtn.setOnClickListener{
            stopLocationUpdates()
            this.findNavController().popBackStack()
            Toast.makeText(activity, "Trip Over!", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    /**
     * Called when the map is ready
     *
     * Checks for location permissions
     * Without permissions, returns to the welcome page
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("TravellingFragment", "Created map")
        mMap = googleMap
        mLine = mMap.addPolyline(PolylineOptions())
        mMap.setOnMarkerClickListener(this)

        viewModel.entriesOfTrip.observe(viewLifecycleOwner) { listOfEntryImagePair ->
            // listOfEntryImagePair is a list of Pairs of (EntryData,List<ImageData>). It contains each entry and it's associated list of images.
            // This is where perhaps, Dan, you could update the map on this fragment to display the image for each entry on the map
            Log.i("EntryCallback", listOfEntryImagePair.toString())
            try {
                val points : MutableList<LatLng> = mutableListOf()
                // for each pair, add a marker...
                for (pair in listOfEntryImagePair) {
                    val entry = pair.first
                    val images = pair.second
                    val newPoint = LatLng(entry.lat, entry.lon)

                    points.add(newPoint)

                    if (images.isNotEmpty()) {
                        Log.i("Images", images.toString())
                        Log.i("Bitmap", images.first().thumbnail.toString())
                        val bmp = decodeSampledBitmapFromResource(images.first().imageUri, 120, 120)
                        val bmpDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(newPoint)
                                .icon(bmpDescriptor)
                                .snippet(images.first().id.toString())
                        )
                    }
                }
                mLine!!.points = points
            } catch (e: Exception) {
                Log.e("TravellingFragment", "Could not write on map " + e.message)
            }
            if (listOfEntryImagePair.isNotEmpty()) {
                val lastEntry = listOfEntryImagePair.last().first
                binding.latitudeText.text = "Latitude: ${lastEntry.lat}"
                binding.longitudeText.text = "Longitude: ${lastEntry.lon}"
                if (lastEntry.entry_pressure != null) binding.pressureText.text = "Pressure: ${lastEntry.entry_pressure} mbar"
                if (lastEntry.entry_temperature != null) binding.temperatureText.text =  "Temperature: ${lastEntry.entry_temperature} C"
                try {
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(LatLng(
                            lastEntry.lat,
                            lastEntry.lon
                        ), 15f)
                    )
                } catch (e: Exception) {
                    Log.e("TravellingFragment", "Could not move camera " + e.message)
                }
            }

        }

        // Update the entriesOfTrip observable to contain all entries of this trip
        viewModel.updateEntriesOfTrip(tripID)
    }

    override fun onMarkerClick(m: Marker?): Boolean {
        Log.i("MarkerClick", m.toString())
        if (m == null) return false
        val position = m.snippet.toInt()-1
        val action = TravellingFragmentDirections.actionTravellingFragmentToShowImageFragment(position)
        this.findNavController().navigate(action)
        return true
    }

    /**
     * Context setter
     * @param context the current context
     */
    private fun setContext(context: Context) {
        ctx = context
    }

    /**
     * Called on resume
     *
     * Creates a location request
     * Creates a location provider client
     * Starts location updates
     */
    override fun onResume() {
        Log.i("TravellingFragment", "onResume")
        super.onResume()
    }

    /**
     * Starts location updates
     *
     * Starts the location service
     * Requests location updates for the location service
     */
    private fun startLocationUpdates() {
        Log.e("Location update", "Starting...")
        intent = Intent(ctx, LocationService::class.java)
        ctx.startService(intent)
    }

    /**
     * Called on pause
     *
     * Stops location updates
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * Stops location updates
     *
     * Removes the callback from the location client
     */
    private fun stopLocationUpdates() {
        ctx.stopService(intent)
        viewModel.setOnGoingTrip(-1)
    }

    //Handle easyImage
    /**
     * Handler for image selector
     *
     * Adds the selected image to the database
     * Associates the image with the most recent location
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    Log.d("InsideDanFragment","TripID: $tripID")
                    viewModel.insertArrayMediaFilesWithLastEntryById(imageFiles)
                    viewModel.updateEntriesOfTrip(tripID)
                }
            })
    }

    /**
     * EasyImage builder
     *
     * Initialises an image selector
     */
    private fun initEasyImage() {
        easyImage = EasyImage.Builder(requireActivity())
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
            .build()
    }

}