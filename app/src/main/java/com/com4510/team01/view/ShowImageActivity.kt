package com.com4510.team01.view
import android.app.Activity
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import com.com4510.team01.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.com4510.team01.model.data.database.ImageDataDao
import com.google.android.gms.maps.MapView
import kotlinx.coroutines.*

class ShowImageActivity : AppCompatActivity() {
    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    lateinit var daoObj: ImageDataDao



    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val position = result.data?.getIntExtra("position", -1)
            val id = result.data?.getIntExtra("id", -1)
            val del_flag = result.data?.getIntExtra("deletion_flag", -1)
            var intent = Intent().putExtra("position", position)
                .putExtra("id", id).putExtra("deletion_flag", del_flag)
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    this.setResult(result.resultCode, intent)
                    this.finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)
        val bundle: Bundle? = intent.extras
        var position = -1

        daoObj = (this@ShowImageActivity.application as ImageApplication)
            .databaseObj.imageDataDao()

        if (bundle != null) {
            // this is the image position in the itemList
            position = bundle.getInt("position")
            displayData(position)
        }
    }

    private fun displayData(position: Int){
        val imageView = findViewById<ImageView>(R.id.show_image)
        val titleToolbar = findViewById<Toolbar>(R.id.show_toolbar)
        val sensorsTextView = findViewById<TextView>(R.id.show_sensors)
        val mapView = findViewById<MapView>(R.id.show_map)
        val descriptionTextView = findViewById<TextView>(R.id.show_image_description)

        val buttonShowDescription = findViewById<Button>(R.id.show_description_button)
        val buttonShowMap = findViewById<Button>(R.id.show_map_button)
        val buttonShowSensors = findViewById<Button>(R.id.show_sensors_button)
        val viewsList = listOf(mapView, descriptionTextView, sensorsTextView)
        if (position != -1) {

            val imageData = MyAdapter.items[position]

            imageView.setImageBitmap(MyAdapter.items[position].thumbnail!!)
            titleToolbar.title = MyAdapter.items[position].imageTitle
            descriptionTextView.text = MyAdapter.items[position].imageDescription


            val fabEdit: FloatingActionButton = findViewById(R.id.fab_edit)
            fabEdit.setOnClickListener(View.OnClickListener {
                startForResult.launch(
                    Intent( this, EditActivity::class.java).apply {
                        putExtra("position", position)
                    }
                )
            })

            buttonShowDescription.setOnClickListener(View.OnClickListener {
                toggleDescription(descriptionTextView, viewsList)
            })
            buttonShowMap.setOnClickListener(View.OnClickListener {
                toggleDescription(mapView, viewsList)
            })
            buttonShowSensors.setOnClickListener(View.OnClickListener {
                toggleDescription(sensorsTextView, viewsList)
            })
        }
    }

    private fun toggleDescription(elem : View, viewsList: List<View>){
        for (view in viewsList){
            if (view != elem) {
                view.visibility = View.GONE
            }
        }
        if (elem.visibility == View.VISIBLE){
            elem.visibility = View.GONE
        }else{
            elem.visibility = View.VISIBLE
        }
    }

}
