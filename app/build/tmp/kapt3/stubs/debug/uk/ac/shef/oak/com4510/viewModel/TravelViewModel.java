package uk.ac.shef.oak.com4510.viewModel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\r\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\fJ\u0006\u0010\u0018\u001a\u00020\u0016J\u0006\u0010\u0019\u001a\u00020\u0016J\u0016\u0010\u001a\u001a\u00020\u00162\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\f0\u001cH\u0002J\u0019\u0010\u001d\u001a\u00020\u00162\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f\u00a2\u0006\u0002\u0010!J\u000e\u0010\"\u001a\u00020#2\u0006\u0010\u0017\u001a\u00020\fJ\u0010\u0010$\u001a\u00020\u00162\b\u0010%\u001a\u0004\u0018\u00010&J&\u0010\'\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\f2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010&2\n\b\u0002\u0010)\u001a\u0004\u0018\u00010&J\u0006\u0010*\u001a\u00020\u0016R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0011\u00a8\u0006+"}, d2 = {"Luk/ac/shef/oak/com4510/viewModel/TravelViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "repository", "Luk/ac/shef/oak/com4510/model/data/Repository;", "app", "Landroid/app/Application;", "(Luk/ac/shef/oak/com4510/model/data/Repository;Landroid/app/Application;)V", "application", "(Landroid/app/Application;)V", "_imageList", "Landroidx/lifecycle/MutableLiveData;", "", "Luk/ac/shef/oak/com4510/model/data/database/ImageData;", "_searchResults", "imageList", "Landroidx/lifecycle/LiveData;", "getImageList", "()Landroidx/lifecycle/LiveData;", "mRepository", "searchResults", "getSearchResults", "deleteImageInDatabase", "", "imageData", "initObservable", "initSearchResults", "insertAndUpdateImageDataList", "imageDataList", "", "insertArrayMediaFiles", "mediaFileArray", "", "Lpl/aprilapps/easyphotopicker/MediaFile;", "([Lpl/aprilapps/easyphotopicker/MediaFile;)V", "insertDataReturnId", "", "search", "query", "", "updateImageInDatabase", "title", "description", "updateImageList", "app_debug"})
public final class TravelViewModel extends androidx.lifecycle.AndroidViewModel {
    private uk.ac.shef.oak.com4510.model.data.Repository mRepository;
    
    /**
     * Observable list of images. Contains all images in the database
     */
    private final androidx.lifecycle.MutableLiveData<java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> _imageList = null;
    
    /**
     * Observable list of images to be used with searching. The search function given a string updates this livedata with
     * a list of all ImageData that contains one of the keywords of the string in either the Title or its Description
     */
    private final androidx.lifecycle.MutableLiveData<java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> _searchResults = null;
    
    public TravelViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    public TravelViewModel(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.Repository repository, @org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> getImageList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData>> getSearchResults() {
        return null;
    }
    
    /**
     * Inserts an imageData into the database and returns the id it was associated with. Warning: This does block the UI thread.
     * This does not update any liveData object
     */
    public final int insertDataReturnId(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData) {
        return 0;
    }
    
    /**
     * Handles the photos returned by EasyImage. Inserts an array of MediaFiles into the database and also changes the imageList livedata
     */
    public final void insertArrayMediaFiles(@org.jetbrains.annotations.NotNull()
    pl.aprilapps.easyphotopicker.MediaFile[] mediaFileArray) {
    }
    
    /**
     * Updates an imageData in the database. Does not update the observable LiveData TO ADD: Functionality for updating position
     */
    public final void updateImageInDatabase(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String description) {
    }
    
    /**
     * Given an imageData, it deletes it from the database and updates the observable liveadata
     */
    public final void deleteImageInDatabase(@org.jetbrains.annotations.NotNull()
    uk.ac.shef.oak.com4510.model.data.database.ImageData imageData) {
    }
    
    /**
     * Initialize all observable LiveData
     */
    public final void initObservable() {
    }
    
    /**
     * Updates the imageList LiveData to reflect what is in the database
     */
    public final void updateImageList() {
    }
    
    /**
     * Initializes the searchResults LiveData to hold every image from the database
     */
    public final void initSearchResults() {
    }
    
    /**
     * Given a query, it updates the _searchResults livedata
     */
    public final void search(@org.jetbrains.annotations.Nullable()
    java.lang.String query) {
    }
    
    /**
     * Internal function that inserts a list of ImageData objects into the database. Updates the list with their generated id's
     */
    private final void insertAndUpdateImageDataList(java.util.List<uk.ac.shef.oak.com4510.model.data.database.ImageData> imageDataList) {
    }
}