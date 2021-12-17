package uk.ac.shef.oak.com4510.model.data.database


import android.graphics.Bitmap
import androidx.room.*

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "Entry",
    indices = [Index(value = ["id"])],
    foreignKeys = arrayOf(ForeignKey(entity = TripData::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("trip_id"),
    onDelete = ForeignKey.CASCADE)))
data class EntryData(
    @PrimaryKey(autoGenerate = true)var id: Int = 0,
    @ColumnInfo(name="lat") var lat: Double,
    @ColumnInfo(name="lon") var lon: Double,
    @ColumnInfo(name="timestamp") var entry_timestamp: Long,
    @ColumnInfo(name="temperature") var entry_temperature: Float?,
    @ColumnInfo(name="pressure") var entry_pressure: Float?,
    @ColumnInfo(name="trip_id") var trip_id: Int)// TO DO: Figure out foreign keys
