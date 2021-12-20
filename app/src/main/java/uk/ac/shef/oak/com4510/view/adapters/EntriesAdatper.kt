package uk.ac.shef.oak.com4510.view.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.model.data.database.EntryData
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

/**
 * Class EntriesAdapter, used by the recyclerview responsible for showing each entry
 * in a trip.
 * @see TripEntriesTabFragment
 */
class EntriesAdapter : RecyclerView.Adapter<EntriesAdapter.ViewHolder> {
    private lateinit var context: Context

    /**
     * @constructor, a list of EntryData is passed to the constructor,
     * which can contain an optional list of ImageData related to that Entry.
     */
    constructor(items: List<EntryData>) : super() {
        Companion.items = items as MutableList<Pair<EntryData,List<ImageData>>>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.list_entry_item,
            parent, false
        )

        var holder: ViewHolder = ViewHolder(v)
        context = parent.context
        return holder
    }



    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var entryID: TextView = itemView.findViewById<TextView>(R.id.entry_id)
        var entryTime: TextView = itemView.findViewById(R.id.entry_time)
        var entryCoordinates: TextView = itemView.findViewById(R.id.entry_coordinates)
    }

    companion object{
        lateinit var items: MutableList<Pair<EntryData, List<ImageData>>>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (entryData, imageList : List<uk.ac.shef.oak.com4510.model.data.database.ImageData>) =
            items[position]

        items[position].let{
            scope.launch{
                holder.entryID.text = "Entry ID: " + entryData.id.toString()
                holder.entryCoordinates.text = "Coordinates: " + convertCoordinates(entryData.lon, entryData.lat)
                holder.entryTime.text = "Date and Time: " + getDateTime(entryData.entry_timestamp)
            }
        }
    }

    fun convertCoordinates(lon: Double, lat: Double): String{
        
        val lonDirection = if (lon >= 0 ) "E" else "W"
        val latDirection = if (lat >= 0 ) "N" else "S"
        val lonRounded = "%.4f".format(lon)
        val latRounded = "%.4f".format(lat)

        return "$latRounded$latDirection, $lonRounded$lonDirection"
    }

    /**
     * function getDateTime
     * @param timestamp the timestamp corresponding to seconds since the linux Epoch (01/01/1970)
     * Currently only available for API26+, needs more compatible alternative
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime(timestamp: Long): String{
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(timestamp)
            sdf.format(netDate).toString()
        }catch (e: Exception){
            e.toString()
        }
    }

}