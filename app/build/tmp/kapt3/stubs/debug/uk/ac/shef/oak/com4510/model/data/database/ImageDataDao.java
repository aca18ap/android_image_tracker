package uk.ac.shef.oak.com4510.model.data.database;

import java.lang.System;

/**
 * Database access object to access the Inventory database
 */
@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\'J\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0019\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\u0006\u0010\u0011\u001a\u00020\u0012H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0015"}, d2 = {"Luk/ac/shef/oak/com4510/model/data/database/ImageDataDao;", "", "delete", "", "imageData", "Luk/ac/shef/oak/com4510/model/data/database/ImageData;", "(Luk/ac/shef/oak/com4510/model/data/database/ImageData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getItem", "id", "", "getItems", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "", "singleImageData", "search", "query", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
public abstract interface ImageDataDao {
    
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "SELECT * from image ORDER by id ASC")
    public abstract java.lang.Object getItems(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> continuation);
    
    /**
     * Returns a list of ImageData whose title OR description contain a keyword in a given query
     */
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Query(value = "\n    SELECT *\n    FROM image\n    JOIN image_fts ON image.title = image_fts.title\n    WHERE image_fts MATCH :query\n  ")
    public abstract java.lang.Object search(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> continuation);
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from image WHERE id = :id")
    public abstract uk.ac.shef.oak.com4510.model.data.database.ImageData getItem(int id);
    
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData singleImageData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Update()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable()
    @androidx.room.Delete()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
}