package uk.ac.shef.oak.com4510.view

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
import pl.aprilapps.easyphotopicker.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentTravellingBinding
import uk.ac.shef.oak.com4510.service.LocationService
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

class TravellingFragment : Fragment(), OnMapReadyCallback {
    private val args: TravellingFragmentArgs by navArgs()
    private lateinit var easyImage: EasyImage
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var ctx: Context
    private var service : LocationService? = null
    private var mLastUpdateTime: String? = null
    private var mLocationPendingIntent: PendingIntent? = null

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
        }
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
        private const val REQUEST_ACCESS_COARSE_LOCATION = 1121 // Used in section 1.1.2 of brief
        private const val REQUEST_ACCESS_FINE_LOCATION = 1122 // Used in section 1.1.2 of brief
        private const val REQUEST_CLOSE_MAP = 1129

        private var activity: FragmentActivity? = null
        private lateinit var mMap: GoogleMap
        private lateinit var viewModel: TravelViewModel
        //private const val ACCESS_FINE_LOCATION = 123
        private var mCurrentLocation: Location? = null
        private var mCurrentPressure: Float? = null
        private var mCurrentTemperature: Float? = null
        private var mLastTimestamp: Long = 0
        private var tripID: Int = -1
        private var entryID: Int = -1
        private lateinit var binding : FragmentTravellingBinding

        fun getActivity(): FragmentActivity? {
            return activity
        }

        fun setActivity(newActivity: FragmentActivity) {
            activity = newActivity
        }

        fun getMap(): GoogleMap {
            return mMap
        }

        fun getViewModel(): TravelViewModel {
            return viewModel
        }

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

        fun getTripId(): Int {
            return tripID
        }

        fun setEntryID(id: Int) {
            entryID = id
        }
    }

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
            easyImage.openChooser(this)
        }

        binding.tripEndBtn.setOnClickListener{
            stopLocationUpdates()
            this.findNavController().popBackStack() // New Trip page
            this.findNavController().popBackStack() // Welcome page
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("TravellingFragment", "Created map")
        mMap = googleMap

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
        }
    }

    private fun setContext(context: Context) {
        ctx = context
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 20000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    override fun onResume() {
        Log.i("TravellingFragment", "onResume")
        super.onResume()
        createLocationRequest()
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        startLocationUpdates()
    }

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
        Log.d("StartLocationUpdates", "Svc?: $mLocationPendingIntent.")
        Log.d("StartLocationUpdates", "Loc?: ${service?.getLastLocation()}")
        val locationTask = locationClient.requestLocationUpdates(
            locationRequest,
            mLocationPendingIntent!!
        )
        locationTask.addOnFailureListener { e ->
            if (e is ApiException) {
                e.message?.let { Log.w("NewTripFragment", it) }
            } else {
                Log.w("NewTripFragment", e.message!!)
            }
        }
        locationTask.addOnCompleteListener {
            Log.d("NewTripFragment", "Starting gps successfully!")
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    //Handle easyImage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    //This is where you get control after choosing a bunch of images
                    Log.d("InsideDanFragment","TripID: $tripID, EntryID: $entryID, Loc: $mCurrentLocation")
                    //Get hold of an entry
                    viewModel.insertArrayMediaFilesWithLastEntryById(imageFiles)
                }
            })
    }

    private fun initEasyImage() {
        easyImage = EasyImage.Builder(requireActivity())
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
            .build()
    }

}