package uk.ac.shef.oak.com4510.viewModel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.model.data.database.EntryData
import uk.ac.shef.oak.com4510.model.data.database.ImageData

class EntriesAdapter : RecyclerView.Adapter<EntriesAdapter.ViewHolder> {
    private lateinit var context: Context

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
    }

    companion object{
        lateinit var items: MutableList<Pair<EntryData, List<ImageData>>>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (entryData, imageList : List<uk.ac.shef.oak.com4510.model.data.database.ImageData>) =
            items[position]

        items[position].let{
            scope.launch{
                holder.entryID.text = entryData.trip_id.toString()
            }
        }
    }

}