package uk.ac.shef.oak.com4510.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
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


class LocationService : Service {
    private var mCurrentLocation: Location? = null
    private var mCurrentPressure: Float? = null
    private var mCurrentTemperature: Float? = null
    private var mLastUpdateTime: String? = null
    private var mLine: Polyline? = null

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

    private var startMode: Int = 0
    private var binder: IBinder? = null
    private var allowRebind: Boolean = false

    private lateinit var sensorManager: SensorManager
    private lateinit var barometer: Sensor
    private lateinit var thermometer: Sensor

    private var doneFirstReading: Boolean = false // First reading often has inaccurate location

    constructor(name: String?) : super() {}
    constructor() : super() {}

    class LocalBinder : Binder() {
        fun getService() : LocationService {
            return LocationService()
        }
//        val service: LocationService
//            get() = LocationService()
    }

    override fun onCreate() {
        Log.i("LocationService", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        barometer = sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)!!
        thermometer = sensorManager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!!
        sensorManager.registerListener(barometerEventListener, barometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(thermometerEventListener, thermometer, SensorManager.SENSOR_DELAY_NORMAL)
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
                                /*TravellingFragment.getViewModel().create_insert_entry(
                                    null, // how to get?
                                    getLastTemperature(), // Nullable if phone has no ambient temperature sensor
                                    getLastPressure(), // Nullable if phone has no barometer
                                    getLastLocation()!!.latitude,
                                    getLastLocation()!!.longitude,
                                    System.currentTimeMillis()
                                )*/
                                TravellingFragment.getMap().addMarker(
                                    MarkerOptions().position(newPoint)
                                        .title("$mLastUpdateTime")
                                        .snippet(
                                        "Pressure: $mCurrentPressure mbar, Temperature: $mCurrentTemperature C"
                                        )
                                )
                                if (mLine == null) mLine = TravellingFragment.getMap()
                                    .addPolyline(PolylineOptions())
                                val points = mLine!!.points
                                points.add(newPoint)
                                mLine!!.points = points
                            }
                        } catch (e: Exception) {
                            Log.e("LocationService", "Error cannot write on map " + e.message)
                        }
                    })
                }
            }
            if (!doneFirstReading) doneFirstReading = true
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    private val mBinder: IBinder = LocalBinder()

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

    fun getLastPressure(): Float? {
        return mCurrentPressure
    }

    fun getLastTemperature(): Float? {
        return mCurrentTemperature
    }

}