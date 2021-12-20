package uk.ac.shef.oak.com4510.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import pl.aprilapps.easyphotopicker.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentTravellingBinding
import uk.ac.shef.oak.com4510.service.LocationService
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

/**
 * A fragment containing the current visit
 */
class TravellingFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private val args: TravellingFragmentArgs by navArgs()
    private lateinit var easyImage: EasyImage
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var ctx: Context
    private var service : LocationService? = null
    private var mLocationPendingIntent: PendingIntent? = null

    private var locationCallback: LocationCallback = object : LocationCallback() {

    }

    private var mConnection: ServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            Log.i("ServiceConnection", "onServiceConnected")
            service = (binder as LocationService.LocalBinder).getService()
            Log.i("ServiceConnection", "Service: $service")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("ServiceConnection", "onServiceDisconnected")
            service = null
        }
    }
    companion object {
        private const val REQUEST_ACCESS_FINE_LOCATION = 1122 // Used in section 1.1.2 of brief
        private lateinit var mMap: GoogleMap
        private lateinit var viewModel: TravelViewModel
        private lateinit var binding : FragmentTravellingBinding
        private var activity: FragmentActivity? = null
        private var mCurrentLocation: Location? = null
        private var mCurrentPressure: Float? = null
        private var mCurrentTemperature: Float? = null
        private var mLastTimestamp: Long = 0
        private var tripID: Int = -1
        private var entryID: Int = -1

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
         * GoogleMap getter
         *
         * @return mMap the GoogleMap instance
         */
        fun getMap(): GoogleMap {
            return mMap
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
         * LocationService callback
         *
         * Sets location and sensor values
         * Updates text boxes
         *
         * @param location the location object containing latitude and longitude values
         * @param pressure the air pressure in millibars
         * @param temperature the ambient temperature in degrees C
         * @param time a timestamp in milliseconds
         * @return activity the parent activity
         */
        fun setData(location: Location?, pressure: Float?, temperature: Float?, time: Long) {
            mCurrentLocation = location
            binding.latitudeText.text = "Latitude: ${location!!.latitude}"
            binding.longitudeText.text = "Longitude: ${location!!.longitude}"
            mCurrentPressure = pressure
            if (pressure != null) binding.pressureText.text = "Pressure: $pressure mbar"
            mCurrentTemperature = temperature
            if (temperature != null) binding.temperatureText.text =  "Temperature: $temperature C"
            mLastTimestamp = time
        }

        /**
         * Trip ID getter
         *
         * @return tripID the ID of the active trip
         */
        fun getTripId(): Int {
            return tripID
        }

        /**
         * Entry ID getter
         *
         * @return entryID the ID of the most recent entry
         */
        fun setEntryID(id: Int) {
            entryID = id
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

        viewModel.entriesOfTrip.observe(viewLifecycleOwner) { listOfEntryImagePair ->
            // listOfEntryImagePair is a list of Pairs of (EntryData,List<ImageData>). It contains each entry and it's associated list of images.
            // This is where perhaps, Dan, you could update the map on this fragment to display the image for each entry on the map
            Log.i("EntryCallback", listOfEntryImagePair.toString())
            // for each pair, add a marker...
            for (pair in listOfEntryImagePair) {
                val entry = pair.first
                val images = pair.second
                val newPoint = LatLng(entry.lat, entry.lon)

                if (images.isNotEmpty()) {
                    Log.i("Images", images.toString())
                    Log.i("Bitmap", images.first().thumbnail.toString())
                    val bmp = ImagesAdapter.decodeSampledBitmapFromResource(images.first().imageUri, 120, 120)
                    val bmpDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
                    mMap?.addMarker(
                        MarkerOptions()
                        .position(newPoint)
                        .icon(bmpDescriptor)
                        .snippet(images.first().id.toString())
                    )
                }
            }
        }

        // Update the entriesOfTrip observable to contain all entries of this trip
        viewModel.updateEntriesOfTrip(tripID)

        binding.tripEndBtn.setOnClickListener{
            stopLocationUpdates()
            this.findNavController().popBackStack() // New Trip page
            this.findNavController().popBackStack() // Welcome page
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
        mMap.setOnMarkerClickListener(this)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("TravellingFragment", "Asking for location permissions...")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            Log.i("TravellingFragment", "Already have location permissions!")
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            this.findNavController().popBackStack()
            this.findNavController().popBackStack()
        }
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
     * Create a location request
     *
     * Generates a location request with an interval of 20s
     * Requests maximum accuracy
     */
    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 20000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
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
        createLocationRequest()
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        startLocationUpdates()
    }

    /**
     * Starts location updates
     *
     * Starts the location service
     * Requests location updates for the location service
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Log.e("Location update", "Starting...")
        val intent = Intent(ctx, LocationService::class.java)
        ctx.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        mLocationPendingIntent =
            PendingIntent.getService(ctx,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val locationTask = locationClient.requestLocationUpdates(
            locationRequest,
            mLocationPendingIntent!!
        )
        locationTask.addOnFailureListener { e ->
            if (e is ApiException) {
                e.message?.let { Log.w("TravellingFragment", it) }
            } else {
                Log.w("Travelling", e.message!!)
            }
        }
        locationTask.addOnCompleteListener {
            Log.d("Travelling", "Started gps")
        }
    }

    /**
     * Called on pause
     *
     * Stops location updates
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * Stops location updates
     *
     * Removes the callback from the location client
     */
    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
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
                    Log.d("InsideDanFragment","TripID: $tripID, EntryID: $entryID, Loc: $mCurrentLocation")
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