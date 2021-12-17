package uk.ac.shef.oak.com4510.model.data.database

import android.graphics.Bitmap
import androidx.room.*

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "image", indices = [Index(value = ["id","title"]),Index(value = ["uri"], unique = true)],
    foreignKeys = arrayOf(ForeignKey(entity = EntryData::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("entry_id"))))
data class ImageData(
    @PrimaryKey(autoGenerate = true)var id: Int = 0,
    @ColumnInfo(name="uri") val imageUri: String,
    @ColumnInfo(name="title") var imageTitle: String,
    @ColumnInfo(name="description") var imageDescription: String? = null,
    @ColumnInfo(name="thumbnailUri") var thumbnailUri: String? = null,
    @ColumnInfo(name="entry_id") var entry_id: Int? = null,
    )
{
    @Ignore
    var thumbnail: Bitmap? = null
}


@Entity(tableName = "image_fts")
@Fts4(contentEntity = ImageData::class)
data class ImageDataFTS(
    @ColumnInfo(name="title") var imageTitle: String,
    @ColumnInfo(name="description") var imageDescription: String? = null,
)
