package com.com4510.team01.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.com4510.team01.R
import com.google.android.material.textfield.TextInputEditText
import com.com4510.team01.model.data.database.ImageDataDao
import com.com4510.team01.viewModel.TravelViewModel
import kotlinx.coroutines.*

class EditActivity : AppCompatActivity() {
    private var viewModel: TravelViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        viewModel = ViewModelProvider(this)[TravelViewModel::class.java]


        val bundle: Bundle? = intent.extras
        var position = -1

        if (bundle != null) {
            // this is the image position in the itemList
            position = bundle.getInt("position")
            if (position != -1) {
                val imageView = findViewById<ImageView>(R.id.edit_image)
                val titleEditToolbar = findViewById<Toolbar>(R.id.editor_toolbar)
                val titleTextInput = findViewById<TextInputEditText>(R.id.edit_image_title)
                val descriptionTextInput =
                    findViewById<TextInputEditText>(R.id.edit_image_description)

                makeButtonListeners(position)

                MyAdapter.items[position].let {
                    imageView.setImageBitmap(it.thumbnail)
                    titleEditToolbar.title = it.imageTitle
                    titleTextInput.setText(it.imageTitle)
                    descriptionTextInput.setText(it.imageDescription ?: "N/A")
                }
            }
        }
    }

    private fun makeButtonListeners(position: Int) {
        var id = MyAdapter.items[position].id
        val cancelButton: Button = findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            this@EditActivity.finish()
        }
        // Delete button listener
        val deleteButton: Button = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            viewModel?.deleteImageInDatabase(MyAdapter.items[position])
            val intent = Intent()
                .putExtra("position", position)
                .putExtra("id", id)
                .putExtra("deletion_flag", 1)
            this@EditActivity.setResult(Activity.RESULT_OK, intent)
            this@EditActivity.finish()
        }

        // Save button listener
        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            val descriptionTextInput = findViewById<TextInputEditText>(R.id.edit_image_description)
            val titleTextInput = findViewById<TextInputEditText>(R.id.edit_image_title)
            //Note that this closes the activity before the update is finished. This is a single, fast update so that should not be an issue.
            //If necessary, make the updateImage function a suspend function and deal with it here to close the activity after the update is finished
            viewModel?.updateImageInDatabase(MyAdapter.items[position],titleTextInput.text.toString(),
                descriptionTextInput.text.toString())

            val intent = Intent()
                .putExtra("position", position)
                .putExtra("id", id)
                .putExtra("deletion_flag", 0)
            this@EditActivity.setResult(Activity.RESULT_OK, intent)
            this@EditActivity.finish()
        }
    }
}