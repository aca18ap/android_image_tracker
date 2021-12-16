package uk.ac.shef.oak.com4510.viewModel

import android.app.Application
import uk.ac.shef.oak.com4510.model.data.database.ImageRoomDatabase

class ImageApplication: Application() {
    val databaseObj: ImageRoomDatabase by lazy { ImageRoomDatabase.getDatabase(this) }
}
