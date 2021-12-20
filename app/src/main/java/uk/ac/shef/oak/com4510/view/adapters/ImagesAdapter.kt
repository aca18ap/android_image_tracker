package uk.ac.shef.oak.com4510.view.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.*
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.util.decodeSampledBitmapFromResource
import uk.ac.shef.oak.com4510.view.fragments.GalleryFragmentDirections
import uk.ac.shef.oak.com4510.view.fragments.ViewTripDetailsFragmentDirections
import java.lang.IllegalArgumentException

/**
 * Adapter for the recycler view responsible for showing images in gallery and
 * within trip details.
 * @see GalleryFragment, TripImagesTabFragment
 */
class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    //Activity context
    private lateinit var context: Context


    /**
     * @constructor: the companion object is assigned a list of ImageData
     */
    constructor(items: List<ImageData>) : super() {
        Companion.items = items as MutableList<ImageData>
    }


    /**
     * Called on creation of the recycler view. Each list item is inflated uwing list_item_image.xml.
     * On creation, each image is attached to an onclicklistener that redirects
     * to the ShowImageFragment for that specific image.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_image,
            parent, false
        )

        val holder: ViewHolder = ViewHolder(v)

        holder.progressBar.visibility = View.VISIBLE

        return holder
    }


    /**
     * The actual content of each list item is bound in this function. The thumbnail of
     * each image is calculated using the function decodeSampledBitmapFromResource.
     * Each item is given onClickListener too, which will redirect to the showImageFragment.
     * This can be accessed from either the TripImageTabFragment or the GalleryFragment.
     *
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (items[position].thumbnail == null) {
            items[position].let {
                scope.launch {
                    val bitmap =
                        decodeSampledBitmapFromResource(it.imageUri, 150, 150)
                    bitmap.let {
                        items[position].thumbnail = it
                        holder.imageView.setImageBitmap(items[position].thumbnail)
                        holder.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
        else {
            holder.imageView.setImageBitmap(items[position].thumbnail)
            holder.progressBar.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener{view: View ->
            //This will throw an exception if the action is done from the wrong fragment
            try{
                val action = GalleryFragmentDirections.actionGalleryFragmentToShowImageFragment(items[position].id)
                view.findNavController().navigate(action)

            //As the action can only be called from two fragments, the app will check first if the action
            // is called from the GalleryFragment, if unsuccessful the call must have been dine from the ViewTripDetailsFragment.
            }catch(e: IllegalArgumentException){
                val action = ViewTripDetailsFragmentDirections.actionViewTripDetailsFragmentToShowImageFragment(items[position].id)
                view.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageView: ImageView = itemView.findViewById<View>(R.id.image_item) as ImageView
        var progressBar: ProgressBar = itemView.findViewById(R.id.progress_circle) as ProgressBar

    }

    companion object {
        lateinit var items: MutableList<ImageData>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }


}
