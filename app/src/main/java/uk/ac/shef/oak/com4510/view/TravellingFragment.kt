package uk.ac.shef.oak.com4510.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import uk.ac.shef.oak.com4510.R

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import pl.aprilapps.easyphotopicker.*
import uk.ac.shef.oak.com4510.databinding.FragmentTravellingBinding
import uk.ac.shef.oak.com4510.service.LocationService
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import java.text.DateFormat
import java.util.*


class TravellingFragment : Fragment(), OnMapReadyCallback {
    private val args: TravellingFragmentArgs by navArgs()
    private lateinit var easyImage: EasyImage
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var ctx: Context
    private lateinit var binding : FragmentTravellingBinding
    private var service : LocationService? = null
    private var viewModel: TravelViewModel? = null
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
//            mCurrentLocation = locationResult.lastLocation
//            mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
//            Log.i("MAP", "new location " + mCurrentLocation.toString())
//            mMap.addMarker(
//                MarkerOptions().position(
//                    LatLng(
//                        mCurrentLocation!!.latitude,
//                        mCurrentLocation!!.longitude
//                    )
//                )
//                    .title(mLastUpdateTime)
//            )
//            mMap.moveCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    LatLng(
//                        mCurrentLocation!!.latitude,
//                        mCurrentLocation!!.longitude
//                    ), 15f
//                )
//            )
        }
    }

//    private ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            // This is called when the connection with the service has been
//            // established, giving us the service object we can use to
//            // interact with the service.  Because we have bound to a explicit
//            // service that we know is running in our own process, we can
//            // cast its IBinder to a concrete class and directly access it.
//            mBoundService = ((LocalService.LocalBinder)service).getService();
//
//            // Tell the user about this for our demo.
//            Toast.makeText(Binding.this, R.string.local_service_connected,
//                Toast.LENGTH_SHORT).show();
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            // This is called when the connection with the service has been
//            // unexpectedly disconnected -- that is, its process crashed.
//            // Because it is running in our same process, we should never
//            // see this happen.
//            mBoundService = null;
//            Toast.makeText(Binding.this, R.string.local_service_disconnected,
//                Toast.LENGTH_SHORT).show();
//        }
//    }

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


    private var mLastUpdateTime: String? = null
    private var mLocationPendingIntent: PendingIntent? = null

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
            mCurrentPressure = pressure
            mCurrentTemperature = temperature
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
        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // required by Android 6.0 +
        initEasyImage()

        //Doesn't exist yet
        binding.fabGallery.setOnClickListener{
            easyImage.openChooser(this)
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        println("Created Map in fragment")
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Asking for location permissions...")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            println("Already have permissions!")
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

    private fun setContext(context: Context) {
        ctx = context
    }

    //Handle easyImage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    //This is where you get control after choosing a bunch of images
                    Log.d("InsideDanFragment","TripID: $tripID, EntryID: $entryID, Loc: $mCurrentLocation")
                    //Get hold of an entry

                    //val entryData = viewModel.create_insert_entry_returnEntry(TripData, temperature:Float?, pressure:Float?, lat:Double, lon:Double, timestamp:Long)

                    //viewModel!!.insertArrayMediaFilesWithEntry(imageFiles,entryData)
                }
            })
    }

    private fun initEasyImage() {
        easyImage = EasyImage.Builder(requireActivity())
//        .setChooserTitle("Pick media")
//        .setFolderName(GALLERY_DIR)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
//        .setCopyImagesToPublicGalleryFolder(true)
            .build()
    }

}