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
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    private lateinit var easyImage: EasyImage
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var ctx: Context
    private lateinit var binding : FragmentTravellingBinding
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

    private var mCurrentLocation: Location? = null
    private var mLastUpdateTime: String? = null
    private var mLocationPendingIntent: PendingIntent? = null

    companion object {
        private const val REQUEST_ACCESS_COARSE_LOCATION = 1121 // Used in section 1.1.2 of brief
        private const val REQUEST_ACCESS_FINE_LOCATION = 1122 // Used in section 1.1.2 of brief
        private const val REQUEST_CLOSE_MAP = 1129

        private var activity: FragmentActivity? = null
        private lateinit var mMap: GoogleMap
        //private const val ACCESS_FINE_LOCATION = 123


        fun getActivity(): FragmentActivity? {
            return activity
        }

        fun setActivity(newActivity: FragmentActivity) {
            activity = newActivity
        }

        fun getMap(): GoogleMap {
            return mMap
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

        setActivity(requireActivity())
        setContext(requireActivity())

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_travelling, container, false)
        var viewModel = ViewModelProvider(this)[TravelViewModel::class.java]
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // required by Android 6.0 +
        initEasyImage()

        //Doesn't exist yet
        binding.fabGallery.setOnClickListener{
            //easyImage.openChooser(this)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(),
            object : DefaultCallback(){
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    viewModel!!.debug_insertArrayMediaFiles(imageFiles)
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