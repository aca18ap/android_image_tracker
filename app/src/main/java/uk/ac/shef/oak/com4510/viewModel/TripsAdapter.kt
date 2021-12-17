package uk.ac.shef.oak.com4510.viewModel

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import uk.ac.shef.oak.com4510.view.ViewPastTripsFragmentDirections
import uk.ac.shef.oak.com4510.viewModel.ImagesAdapter.Companion.decodeSampledBitmapFromResource


class TripsAdapter : RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private lateinit var context: Context

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
        holder.itemView.setOnClickListener(View.OnClickListener {
            it.findNavController().navigate(R.id.action_viewPastTripsFragment_to_viewTripDetailsFragment)
        })

        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (tripdata, first_image) = items[position]
        if (items[position].first.thumbnail == null) {
            items[position].let {
                scope.launch {
                    if (first_image != null) {
                        val bitmap = decodeSampledBitmapFromResource(first_image.imageUri, 150, 150)
                        bitmap.let {
                            items[position].first.thumbnail = it
                            holder.thumbnail.setImageBitmap(tripdata.thumbnail)
                        }
                    }
                }
            }
        }else { holder.thumbnail.setImageBitmap(items[position].first.thumbnail) }



        items[position].let {
            scope.launch {
                holder.title.text = tripdata.title
            }
        }


        holder.itemView.setOnClickListener{view: View ->
            val action = ViewPastTripsFragmentDirections.actionViewPastTripsFragmentToViewTripDetailsFragment(position)
            view.findNavController().navigate(action)
        }
    }


    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var thumbnail: ImageView = itemView.findViewById<View>(R.id.trip_item_thumbnail) as ImageView
        var title: TextView = itemView.findViewById<View>(R.id.trip_item_title) as TextView
    }


    companion object {
        lateinit var items: MutableList<Pair<TripData, ImageData?>>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    }

    override fun getItemCount(): Int {
        return items.size
    }


}