package uk.ac.shef.oak.com4510.view;

import java.lang.System;

@kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00162\u00020\u00012\u00020\u0002:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u000bH\u0016R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0017"}, d2 = {"Luk/ac/shef/oak/com4510/view/MapActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/google/android/gms/maps/OnMapReadyCallback;", "()V", "locationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "getLocationClient", "()Lcom/google/android/gms/location/FusedLocationProviderClient;", "setLocationClient", "(Lcom/google/android/gms/location/FusedLocationProviderClient;)V", "mMap", "Lcom/google/android/gms/maps/GoogleMap;", "getMMap", "()Lcom/google/android/gms/maps/GoogleMap;", "setMMap", "(Lcom/google/android/gms/maps/GoogleMap;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onMapReady", "googleMap", "Companion", "app_debug"})
public final class MapActivity extends androidx.appcompat.app.AppCompatActivity implements com.google.android.gms.maps.OnMapReadyCallback {
    public com.google.android.gms.location.FusedLocationProviderClient locationClient;
    public com.google.android.gms.maps.GoogleMap mMap;
    @org.jetbrains.annotations.NotNull()
    public static final uk.ac.shef.oak.com4510.view.MapActivity.Companion Companion = null;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1121;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1122;
    private static final int REQUEST_CLOSE_MAP = 1129;
    
    public MapActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.gms.location.FusedLocationProviderClient getLocationClient() {
        return null;
    }
    
    public final void setLocationClient(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.location.FusedLocationProviderClient p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.gms.maps.GoogleMap getMMap() {
        return null;
    }
    
    public final void setMMap(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    @kotlin.Metadata(mv = {1, 5, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Luk/ac/shef/oak/com4510/view/MapActivity$Companion;", "", "()V", "REQUEST_ACCESS_COARSE_LOCATION", "", "REQUEST_ACCESS_FINE_LOCATION", "REQUEST_CLOSE_MAP", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}