package com.com4510.team01.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.com4510.team01.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var locationClient: FusedLocationProviderClient
    lateinit var mMap: GoogleMap

    companion object {
        private const val REQUEST_ACCESS_COARSE_LOCATION = 1121 // Used in section 1.1.2 of brief
        private const val REQUEST_ACCESS_FINE_LOCATION = 1122 // Used in section 1.1.2 of brief
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("Created Activity")
        super.onCreate(savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_map)
        println("Set content view")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        println("Set map fragment")
        mapFragment.getMapAsync(this)
        println("Set async")

    }

    override fun onMapReady(googleMap: GoogleMap) {
        println("Created Map")
        mMap = googleMap;
        var position = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Asking for location permissions...")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )
            arrayOf(0 as Double, 0 as Double)
        } else {
            print("Getting location...")
            var locationCts = CancellationTokenSource()
            var location = locationClient.getCurrentLocation(100, locationCts.token) // High accuracy
            arrayOf(location.result.latitude, location.result.longitude)
        }
        println("Got location")
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(position[0], position[1]), 15F))
        println("Moved camera")

    }
}