package uk.ac.shef.oak.com4510.model.data.database;

import java.lang.System;

/**
 * Entity data class represents a single row in the database.
 */
@androidx.room.Entity(tableName = "image", indices = {@androidx.room.Index(value = {"id", "title"})})
@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\nJ\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003JK\u0010&\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010*\u001a\u00020\u0003H\u00d6\u0001J\t\u0010+\u001a\u00020\u0005H\u00d6\u0001R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR \u0010\u0007\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001e\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R \u0010\t\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0010\"\u0004\b\u0017\u0010\u0012R \u0010\u0018\u001a\u0004\u0018\u00010\u00198\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR \u0010\b\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0010\"\u0004\b\u001f\u0010\u0012\u00a8\u0006,"}, d2 = {"Luk/ac/shef/oak/com4510/model/data/database/ImageData;", "", "id", "", "imageUri", "", "imageTitle", "imageDescription", "thumbnailUri", "position", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getId", "()I", "setId", "(I)V", "getImageDescription", "()Ljava/lang/String;", "setImageDescription", "(Ljava/lang/String;)V", "getImageTitle", "setImageTitle", "getImageUri", "getPosition", "setPosition", "thumbnail", "Landroid/graphics/Bitmap;", "getThumbnail", "()Landroid/graphics/Bitmap;", "setThumbnail", "(Landroid/graphics/Bitmap;)V", "getThumbnailUri", "setThumbnailUri", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class ImageData {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "uri")
    private final java.lang.String imageUri = null;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "title")
    private java.lang.String imageTitle;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "description")
    private java.lang.String imageDescription;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "thumbnailUri")
    private java.lang.String thumbnailUri;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "position")
    private java.lang.String position;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Ignore()
    private android.graphics.Bitmap thumbnail;
    
    /**
     * Entity data class represents a single row in the database.
     */
    @org.jetbrains.annotations.NotNull()
    public final uk.ac.shef.oak.com4510.model.data.database.ImageData copy(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String imageUri, @org.jetbrains.annotations.NotNull()
    java.lang.String imageTitle, @org.jetbrains.annotations.Nullable()
    java.lang.String imageDescription, @org.jetbrains.annotations.Nullable()
    java.lang.String thumbnailUri, @org.jetbrains.annotations.Nullable()
    java.lang.String position) {
        return null;
    }
    
    /**
     * Entity data class represents a single row in the database.
     */
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    /**
     * Entity data class represents a single row in the database.
     */
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    /**
     * Entity data class represents a single row in the database.
     */
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public ImageData(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String imageUri, @org.jetbrains.annotations.NotNull()
    java.lang.String imageTitle, @org.jetbrains.annotations.Nullable()
    java.lang.String imageDescription, @org.jetbrains.annotations.Nullable()
    java.lang.String thumbnailUri, @org.jetbrains.annotations.Nullable()
    java.lang.String position) {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int getId() {
        return 0;
    }
    
    public final void setId(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getImageUri() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getImageTitle() {
        return null;
    }
    
    public final void setImageTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getImageDescription() {
        return null;
    }
    
    public final void setImageDescription(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getThumbnailUri() {
        return null;
    }
    
    public final void setThumbnailUri(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPosition() {
        return null;
    }
    
    public final void setPosition(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap getThumbnail() {
        return null;
    }
    
    public final void setThumbnail(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap p0) {
    }
}