package uk.ac.shef.oak.com4510.view

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.*
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter

class ExistingTravelFragment : Fragment(), OnMapReadyCallback {
    private val args: ExistingTravelFragmentArgs by navArgs()
    private lateinit var viewModel: TravelViewModel
    private lateinit var mMap: GoogleMap
    private var tripID = -1
    private var entryID = -1
    private var mLine: Polyline? = null
    private var mLineLoc: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tripID = args.tripID
        entryID = args.entryID

        viewModel = ViewModelProvider(requireActivity())[TravelViewModel::class.java]

        return inflater.inflate(R.layout.fragment_existing_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.entriesOfTrip.observe(viewLifecycleOwner) { listOfEntryImagePair ->
            // listOfEntryImagePair is a list of Pairs of (EntryData,List<ImageData>). It contains each entry and it's associated list of images.
            // This is where perhaps, Dan, you could update the map on this fragment to display the image for each entry on the map
            Log.i("EntryCallback", listOfEntryImagePair.toString())
            try{


//                mLine!!.points.clear() // Reset the lines
//                mLineLoc!!.points.clear()

                // for each pair, add a marker...
                for (pair in listOfEntryImagePair) {
                    val entry = pair.first
                    val images = pair.second
                    val newPoint = LatLng(entry.lat, entry.lon)

                    val points = mLine!!.points
                    points.add(newPoint)
                    mLine!!.points = points

                    if (arrayOf(-1, 0, 1).contains(entry.id - entryID)) { // Highlight the selected location
                        Log.i("HighlightEntry", "Match found: $entry")
                        val highlightPoints = mLineLoc!!.points
                        highlightPoints.add(LatLng(entry.lat, entry.lon))
                        mLineLoc!!.points = highlightPoints

                        if (entry.id == entryID) {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(LatLng(
                                    entry.lat,
                                    entry.lon
                                ), 18f)
                            )
                        }
                    }
                    if (images.isNotEmpty()) {
                        Log.i("Images", images.toString())
                        Log.i("Bitmap", images.first().thumbnail.toString())
                        val bmp = ImagesAdapter.decodeSampledBitmapFromResource(images.first().imageUri, 150, 150)
                        val bmpDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
//                        val bmpDescriptor = BitmapDescriptorFactory.fromPath(images.first().imageUri)
                        mMap.addMarker(MarkerOptions()
                            .position(newPoint)
                            .icon(bmpDescriptor)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ExistingTravelFragment", "Could not write on map " + e.message)
            }

        }
        viewModel.updateEntriesOfTrip(tripID)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (mLine == null) mLine = mMap
            .addPolyline(PolylineOptions())
        if (mLineLoc == null) mLineLoc = mMap
            .addPolyline(PolylineOptions()
                .color(4282549748.toInt()) // Google blue
                .zIndex(1f)// Over the top of the entire trip
                .width(20f)
            )
    }
}