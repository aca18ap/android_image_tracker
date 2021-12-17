package uk.ac.shef.oak.com4510.service

import android.app.IntentService
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.Context
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uk.ac.shef.oak.com4510.view.TravellingFragment
import java.text.DateFormat
import java.util.*

class LocationService : Service {
    private var mCurrentLocation: Location? = null
    private var mLastUpdateTime: String? = null

    private var startMode: Int = 0
    private var binder: IBinder? = null
    private var allowRebind: Boolean = false

    constructor(name: String?) : super() {}
    constructor() : super() {}

    override fun onCreate() {
        Log.i("LocationService", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("LocationService", "onStartCommand")
        if (LocationResult.hasResult(intent!!)) {
            Log.i("LocationResult", "Has result")
            val locResults = LocationResult.extractResult(intent)
            for (location in locResults.locations) {
                if (location == null) continue
                Log.i("In service New Location", "Current location: $location")
                mCurrentLocation = location
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                Log.i("This is in service, MAP", "New location " + mCurrentLocation.toString())
                if (TravellingFragment.getActivity() != null) {
                    TravellingFragment.getActivity()?.runOnUiThread(Runnable {
                        try {
                            TravellingFragment.getMap().addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        mCurrentLocation!!.latitude,
                                        mCurrentLocation!!.longitude
                                    )
                                ).title(mLastUpdateTime)
                            )
                            val zoom = CameraUpdateFactory.zoomTo(15f)
                            TravellingFragment.getMap().moveCamera(
                                CameraUpdateFactory.newLatLng(
                                    LatLng(
                                        mCurrentLocation!!.latitude,
                                        mCurrentLocation!!.longitude
                                    )
                                )
                            )
                            TravellingFragment.getMap().animateCamera(zoom)
                        } catch (e: Exception) {
                            Log.e("LocationService", "Error cannot write on map " + e.message)
                        }
                    })
                }
            }
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return allowRebind
    }

    override fun onRebind(intent: Intent) {

    }

    override fun onDestroy() {
        Log.e("Service", "end")
    }

    fun getLastLocation(): Location? {
        return mCurrentLocation
    }

}