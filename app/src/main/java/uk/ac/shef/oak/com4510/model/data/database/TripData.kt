package uk.ac.shef.oak.com4510.model.data.database


import android.graphics.Bitmap
import androidx.room.*

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "Trip", indices = [Index(value = ["id"])])
data class TripData(
    @PrimaryKey(autoGenerate = true)var id: Int = 0,
    @ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="country") var country: String,
    @ColumnInfo(name="timestamp") var trip_timestamp: Float)