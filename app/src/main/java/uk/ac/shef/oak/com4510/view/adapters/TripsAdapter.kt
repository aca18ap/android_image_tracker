package uk.ac.shef.oak.com4510.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.model.data.database.TripData
import uk.ac.shef.oak.com4510.view.adapters.ImagesAdapter.Companion.decodeSampledBitmapFromResource
import uk.ac.shef.oak.com4510.view.fragments.ViewPastTripsFragmentDirections

/**
 * Class TripsAdapter, used by the recyclerview responsible for showing
 * a list of all trips achieved so far.
 * @see ViewPastTripsFragment
 */
class TripsAdapter : RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private lateinit var context: Context

    /**
     * @constructor creating a list of items containing the trips data
     * @params items: a list of TripData files.
     * Each list entry can also have an ImageData file. This will correspond
     * to the first image of the trip, if the latter has any attached to it.
     *
     */
    constructor(items: List<TripData>) : super() {
        Companion.items = items as MutableList<Pair<TripData, ImageData?>>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflating layout
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.list_trip_item,
            parent, false
        )

        var holder: ViewHolder = ViewHolder(v)
        context = parent.context

        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        //Getting tripdata and the thumbnail image
        val (tripdata, first_image) = items[position]
        if (first_image != null){
            holder.progressBar.visibility = View.VISIBLE
            if (items[position].first.thumbnail == null) {
                items[position].let {
                    scope.launch {
                        //Decoding image to bitmap if available.
                        //By default, a missing image resource is used in the xml file, so we only need to
                        //Assign an image if present
                        if (first_image != null) {
                            val bitmap = decodeSampledBitmapFromResource(first_image.imageUri, 150, 150)
                            bitmap.let {
                                items[position].first.thumbnail = it
                                holder.thumbnail.setImageBitmap(tripdata.thumbnail)
                                holder.progressBar.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }else{
                holder.thumbnail.setImageBitmap(tripdata.thumbnail)
                holder.progressBar.visibility = View.INVISIBLE
            }
        }



        //Each item in the trips list has a title on top of the image that's already added.
        items[position].let {
            scope.launch {
                holder.title.text = tripdata.title
            }
        }


        //Each item has an onClickListener that will redirect it to the ViewTripDetailsFragment for that trip.
        // The position argument is passed to be processed by the fragment
        holder.itemView.setOnClickListener{view: View ->
            val action = ViewPastTripsFragmentDirections.actionViewPastTripsFragmentToViewTripDetailsFragment(
                items[position].second?.id ?: -1,items[position].first.id)
            view.findNavController().navigate(action)
        }
    }


    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var thumbnail: ImageView = itemView.findViewById<View>(R.id.trip_item_thumbnail) as ImageView
        var title: TextView = itemView.findViewById<View>(R.id.trip_item_title) as TextView
        var progressBar: ProgressBar = itemView.findViewById(R.id.progress_circle) as ProgressBar
    }


    companion object {
        lateinit var items: MutableList<Pair<TripData, ImageData?>>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    }

    override fun getItemCount(): Int {
        return items.size
    }


}