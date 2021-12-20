package uk.ac.shef.oak.com4510.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import uk.ac.shef.oak.com4510.model.data.Repository
import uk.ac.shef.oak.com4510.view.fragments.TravellingFragment
import java.text.DateFormat
import java.util.*

/**
 * A service to periodically gather location and sensor information, and pass it back to the current visit
 */
class LocationService : Service() {
    private lateinit var sensorManager: SensorManager
    private lateinit var locationCallback : LocationCallback
    private var mCurrentLocation: Location? = null
    private var mCurrentPressure: Float? = null
    private var mCurrentTemperature: Float? = null
    private var mLastUpdateTime: String? = null
    private var barometer: Sensor? = null
    private var thermometer: Sensor? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationClient: FusedLocationProviderClient

    private lateinit var mRepository: Repository

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
     * Called when the service is instantiated
     */
    override fun onCreate() {
        Log.i("LocationService", "onCreate")
        super.onCreate()
        createLocationRequest()
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        mRepository = Repository(application)
    }

    /**
     * Called to begin location tracking
     *
     * Sets up location and sensors
     * Passes data to the current visit every 20s
     */
    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        barometer = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        thermometer = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (barometer != null) sensorManager.registerListener(barometerEventListener, barometer, SensorManager.SENSOR_DELAY_NORMAL)
        if (thermometer != null) sensorManager.registerListener(thermometerEventListener, thermometer, SensorManager.SENSOR_DELAY_NORMAL)
        Log.i("LocationService", "onStartCommand")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.i("LocationCallback", location.toString())
                    mCurrentLocation = location
                    mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                    if (TravellingFragment.getActivity() != null) {
                        TravellingFragment.getActivity()?.runOnUiThread(Runnable {
                            mRepository.create_insert_entry_returnEntry(
                                TravellingFragment.getTripId(),
                                getLastTemperature(), // Nullable if phone has no ambient temperature sensor
                                getLastPressure(), // Nullable if phone has no barometer
                                getLastLocation()!!.latitude,
                                getLastLocation()!!.longitude,
                                System.currentTimeMillis()
                            )
                            Log.i("ServiceLocation", "Successfully added entry")
                            TravellingFragment.getViewModel().updateEntriesOfTrip(TravellingFragment.getTripId())
                        })
                    }
                }
            }
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        return START_REDELIVER_INTENT
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
     * Called on bind
     *
     * Given an intent, return the binder
     *
     * @param intent the intent
     * @return mBinder the LocalBinder object
     */
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * Called on destroy
     *
     * Does nothing
     */
    override fun onDestroy() {
        Log.e("Service", "Service ended")
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