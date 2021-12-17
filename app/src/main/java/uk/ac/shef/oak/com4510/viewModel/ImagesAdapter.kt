package uk.ac.shef.oak.com4510.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.*
import uk.ac.shef.oak.com4510.model.data.database.ImageData
import uk.ac.shef.oak.com4510.view.GalleryFragmentDirections
import kotlinx.coroutines.*
import uk.ac.shef.oak.com4510.view.TripImagesTabFragment
import uk.ac.shef.oak.com4510.view.ViewTripDetailsFragment
import uk.ac.shef.oak.com4510.view.ViewTripDetailsFragmentDirections
import java.lang.IllegalArgumentException

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    private lateinit var context: Context



    constructor(items: List<ImageData>) : super() {
        Companion.items = items as MutableList<ImageData>
    }

    constructor(cont: Context, items: List<ImageData>) : super() {
        Companion.items = items as MutableList<ImageData>
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_image,
            parent, false
        )

        val holder: ViewHolder = ViewHolder(v)
        context = parent.context
        holder.itemView.setOnClickListener(View.OnClickListener {

            it.findNavController().navigate(R.id.action_galleryFragment_to_showImageFragment)

        })
        return holder
    }

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
                    }
                }
            }
        }
        else {holder.imageView.setImageBitmap(items[position].thumbnail)}
        holder.itemView.setOnClickListener{view: View ->
            try{
                val action = GalleryFragmentDirections.actionGalleryFragmentToShowImageFragment(position)
                view.findNavController().navigate(action)

            }catch(e: IllegalArgumentException){
                val action = ViewTripDetailsFragmentDirections.actionViewTripDetailsFragmentToShowImageFragment(position)
                view.findNavController().navigate(action)

            }
        }

    }

    /*
    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(filter: CharSequence?): FilterResults {
                val charString = filter?.toString() ?: ""
                if (charString.isEmpty()) itemsFiltered = items as ArrayList<ImageData> else {
                    items.filter {
                        (it.imageTitle.contains(filter!!)) or
                                (it.imageDescription!!.contains(filter))
                    }.forEach {itemsFiltered.add(it)}
                }
                return FilterResults().apply { values = itemsFiltered }
            }

            override fun publishResults(filter: CharSequence?, results: FilterResults?) {
               itemsFiltered = if (results?.values == null)
                   ArrayList()
                else
                    results.values as ArrayList<ImageData>
                notifyDataSetChanged()
            }
        }
    }
    */

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageView: ImageView = itemView.findViewById<View>(R.id.image_item) as ImageView



    }

    companion object {
        lateinit var items: MutableList<ImageData>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        /**
         * helper function to generate a bitmap object of a given size from an image's file path.
         */
        fun decodeSampledBitmapFromResource(
            filePath: String,
            reqWidth: Int,
            reqHeight: Int
        ): Bitmap {
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()

            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options);
        }

        /**
         * helper function to calculate an inSampleSize for resampling to achieve the right picture
         * density for a smaller size file.
         * See inSampleFile: https://developer.android.com/reference/android/graphics/BitmapFactory.Options#inSampleSize
         */
        private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            // Raw height and width of image
            val height = options.outHeight;
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = (height / 2).toInt()
                val halfWidth = (width / 2).toInt()

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth
                ) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize.toInt();
        }
    }


}
