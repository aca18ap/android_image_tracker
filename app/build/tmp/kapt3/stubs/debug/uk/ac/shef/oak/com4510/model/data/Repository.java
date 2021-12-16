package uk.ac.shef.oak.com4510.model.data;

import java.lang.System;

@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\r\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bJ\u001b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ!\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u001b\u0010\u0018\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eR\u0010\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Luk/ac/shef/oak/com4510/model/data/Repository;", "", "dataSource", "Luk/ac/shef/oak/com4510/model/data/database/ImageDataDao;", "app", "Landroid/app/Application;", "(Luk/ac/shef/oak/com4510/model/data/database/ImageDataDao;Landroid/app/Application;)V", "application", "(Landroid/app/Application;)V", "imageDataDao", "delete", "", "imageData", "Luk/ac/shef/oak/com4510/model/data/database/ImageData;", "(Luk/ac/shef/oak/com4510/model/data/database/ImageData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllImages", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertDataReturnId", "", "search", "query", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateImage", "app_debug"})
public final class Repository {
    private uk.ac.shef.oak.com4510.model.data.database.ImageDataDao imageDataDao;
    
    public Repository(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super();
    }
    
    public Repository(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageDataDao dataSource, @org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super();
    }
    
    /**
     * Returns every image in the databases
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllImages(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object search(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    /**
     * Inserts an imageData object into the database. Returns the id it was assigned to
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertDataReturnId(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> continuation) {
        return null;
    }
    
    /**
     * Updates the database given an imageData object
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateImage(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}