package uk.ac.shef.oak.com4510.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import uk.ac.shef.oak.com4510.view.TravellingFragment
import java.text.DateFormat
import java.util.*

/**
 * A service to periodically gather location and sensor information, and pass it back to the current visit
 */
class LocationService : Service() {
    private lateinit var sensorManager: SensorManager

    private var mCurrentLocation: Location? = null
    private var mCurrentPressure: Float? = null
    private var mCurrentTemperature: Float? = null
    private var mLastUpdateTime: String? = null
    private var mLine: Polyline? = null
    private var currentEntryID: Int = -1
    private var barometer: Sensor? = null
    private var thermometer: Sensor? = null
    private var doneFirstReading: Boolean = false // First reading often has inaccurate location
    private val mBinder: IBinder = LocalBinder()

    private var barometerEventListener  = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            mCurrentPressure = event.values[0]
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private var thermometerEventListener  = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            mCurrentTemperature = event.values[0]
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    /**
     * A binder class to expose this service to TravellingFragment
     */
    class LocalBinder : Binder() {
        /**
         * Service getter
         *
         * @return LocationService an instance of the service
         */
        fun getService() : LocationService {
            return LocationService()
        }
    }

    /**
     * Called when the service is instantiated
     */
    override fun onCreate() {
        Log.i("LocationService", "onCreate")
        super.onCreate()
    }

    /**
     * Called to begin location tracking
     *
     * Sets up location and sensors
     * Passes data to the current visit every 20s
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        barometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        thermometer = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (barometer != null) sensorManager.registerListener(barometerEventListener, barometer, SensorManager.SENSOR_DELAY_NORMAL)
        if (thermometer != null) sensorManager.registerListener(thermometerEventListener, thermometer, SensorManager.SENSOR_DELAY_NORMAL)
        Log.i("LocationService", "onStartCommand")
        if (LocationResult.hasResult(intent!!)) {
            Log.i("LocationResult", "Has result")
            val locResults = LocationResult.extractResult(intent)
            for (location in locResults.locations) {
                if (location == null) continue
                Log.i("In service New Location", "Current location: $location")
                Log.i("Sensors", "Pressure: $mCurrentPressure, Temperature: $mCurrentTemperature")
                mCurrentLocation = location
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                val newPoint = LatLng(
                    mCurrentLocation!!.latitude,
                    mCurrentLocation!!.longitude
                )
                Log.i("This is in service, MAP", "New location " + mCurrentLocation.toString())
                if (TravellingFragment.getActivity() != null) {
                    TravellingFragment.getActivity()?.runOnUiThread(Runnable {
                        try {
                            val zoom = CameraUpdateFactory.zoomTo(15f)
                            TravellingFragment.getMap().moveCamera(
                                CameraUpdateFactory.newLatLng(newPoint)
                            )
                            TravellingFragment.getMap().animateCamera(zoom)
                            TravellingFragment.setData(getLastLocation()!!, getLastPressure(), getLastTemperature(), System.currentTimeMillis())
                            if (doneFirstReading) {
                                currentEntryID = TravellingFragment.getViewModel().create_insert_entry_returnEntry(
                                    TravellingFragment.getTripId(),
                                    getLastTemperature(), // Nullable if phone has no ambient temperature sensor
                                    getLastPressure(), // Nullable if phone has no barometer
                                    getLastLocation()!!.latitude,
                                    getLastLocation()!!.longitude,
                                    System.currentTimeMillis()
                                ).id
                                Log.i("ServiceEntryID", "ID: $currentEntryID")
                                TravellingFragment.setEntryID(currentEntryID)
//                                TravellingFragment.getMap().addMarker(
//                                    MarkerOptions().position(newPoint)
//                                        .title("$mLastUpdateTime")
//                                        .snippet(
//                                        "Pressure: $mCurrentPressure mbar, Temperature: $mCurrentTemperature C"
//                                        )
//                                )
                                if (mLine == null) mLine = TravellingFragment.getMap()
                                    .addPolyline(PolylineOptions())
                                val points = mLine!!.points
                                points.add(newPoint)
                                mLine!!.points = points
                            }
                        } catch (e: Exception) {
                            Log.e("LocationService", "Could not write on map " + e.message)
                        }
                    })
                }
            }
            if (!doneFirstReading) doneFirstReading = true
        }
        return Service.START_REDELIVER_INTENT
    }

    /**
     * Called on bind
     *
     * Given an intent, return the binder
     *
     * @param intent the intent
     * @return mBinder the LocalBinder object
     */
    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    /**
     * Called on unbind
     *
     * Given an intent, return whether it is allowed to rebind
     *
     * @param intent the intent
     * @return false whether it is allowed to rebind
     */
    override fun onUnbind(intent: Intent): Boolean {
        return false
    }

    /**
     * Called on rebind
     *
     * Does nothing
     */
    override fun onRebind(intent: Intent) {

    }

    /**
     * Called on destroy
     *
     * Does nothing
     */
    override fun onDestroy() {
        Log.e("Service", "end")
    }

    /**
     * Location getter
     *
     * Returns the most recent location
     *
     * @return mCurrentLocation the most recent location
     */
    fun getLastLocation(): Location? {
        return mCurrentLocation
    }

    /**
     * Pressure getter
     *
     * Returns the most recent pressure
     *
     * @return mCurrentPressure the most recent pressure
     */
    fun getLastPressure(): Float? {
        return mCurrentPressure
    }

    /**
     * Temperature getter
     *
     * Returns the most recent Temperature
     *
     * @return mCurrentTemperature the most recent temperature
     */
    fun getLastTemperature(): Float? {
        return mCurrentTemperature
    }

}