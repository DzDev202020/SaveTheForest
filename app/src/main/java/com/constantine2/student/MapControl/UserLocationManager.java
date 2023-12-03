package com.constantine2.student.MapControl;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

public class UserLocationManager {
    private final static int MAX_ZOOM_SCALE = 1;
    private final static int MIN_ZOOM_SCALE = 1;
    private final static int TRAKING_ZOOM = 12;
    private final static int TRAKING_TILT = 30;


    Context mainContext;
    LocationComponent locationComponent;
    LocationComponentActivationOptions activationOptions;
    LocationComponentOptions locationComponentOptions;


    public UserLocationManager(@NonNull Context mainContext, @NonNull LocationComponent locationComponent) {
        this.mainContext = mainContext;
        this.locationComponent = locationComponent;
    }

    @SuppressWarnings({"MissingPermission"})
    public void ActivateLocationComponent(Style style) {
        if (!locationComponent.isLocationComponentActivated()) {
            // Display Option
            locationComponentOptions = LocationComponentOptions.builder(mainContext)
                    .layerBelow("LayerBelowLocationComponent")
                    //.layerBelow(null)
                    //.foregroundDrawable(R.drawable.cafe_hdpi)
                    // .bearingTintColor(Color.GREEN)
                    // .accuracyAlpha(1)
                    .pulseEnabled(false)
                    // .pulseColor(Color.GREEN)
                    //  .pulseAlpha(.4f)
                    // .pulseInterpolator(new BounceInterpolator())
                    .maxZoomIconScale(MAX_ZOOM_SCALE)
                    .minZoomIconScale(MIN_ZOOM_SCALE)
                    .build();
            // Activation Option
            activationOptions = LocationComponentActivationOptions
                    .builder(mainContext, style)
                    .locationComponentOptions(locationComponentOptions)
                    .useDefaultLocationEngine(true)
                    .build();
            locationComponent.activateLocationComponent(activationOptions);
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode : define TrackMod when UserLocation Change
            locationComponent.setCameraMode(CameraMode.NONE);
            // Set the component's render mode : define device location image
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            locationComponent.setLocationComponentEnabled(true);
        }
    }


    @SuppressWarnings({"MissingPermission"})
    public void ActivateLocationComponent(Style style, LocationComponentActivationOptions activationOptions, int cameraMode, int renderMode) {
        this.activationOptions = activationOptions;
        locationComponent.activateLocationComponent(activationOptions);
        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);
        // Set the component's camera mode
        locationComponent.setCameraMode(cameraMode);
        // Set the component's render mode
        locationComponent.setRenderMode(renderMode);
    }

    @SuppressWarnings({"MissingPermission"})
    public void EnableLocationComponent() {
        if (locationComponent.isLocationComponentActivated() && !locationComponent.isLocationComponentEnabled())
            locationComponent.setLocationComponentEnabled(true);
    }

    @SuppressWarnings({"MissingPermission"})
    public void DisableLocationCompenent() {
        if (locationComponent.isLocationComponentActivated() && locationComponent.isLocationComponentEnabled())
            locationComponent.setLocationComponentEnabled(false);
    }

    public void SetLocationEngine(LocationEngine locationEngine) {
        locationComponent.setLocationEngine(locationEngine);
    }

    public LocationEngine GetLocationEngine() {
        return locationComponent.getLocationEngine();
    }


    public void SetLocationComponentZoomTiltWhileTrack(double zoom, double tilt) {
        locationComponent.zoomWhileTracking(zoom);
        locationComponent.tiltWhileTracking(tilt);
    }

    public void SetLocationComponentZoomTiltWhileTrack(double zoom, long zoomAnimationDuration, MapboxMap.CancelableCallback callbackZoom,
                                                       double tilt, long tiltAnimationDuration, MapboxMap.CancelableCallback callbackTilt) {
        if (callbackZoom == null)
            locationComponent.zoomWhileTracking(zoom, zoomAnimationDuration);
        else
            locationComponent.zoomWhileTracking(zoom, zoomAnimationDuration, callbackZoom);
        if (callbackTilt == null)
            locationComponent.tiltWhileTracking(tilt, tiltAnimationDuration);
        else
            locationComponent.tiltWhileTracking(tilt, zoomAnimationDuration, callbackZoom);

    }

    public void SetOnCameraModChangeListener(OnCameraTrackingChangedListener listener) {
        locationComponent.addOnCameraTrackingChangedListener(listener);
    }

    public Location GetLastGoodPosition() {
        return locationComponent.getLastKnownLocation();
    }


    public void FocusUserLocation() {
        locationComponent.setCameraMode(CameraMode.TRACKING);
    }

    public void SetCameraMode(int cameraMod) {
        locationComponent.setCameraMode(cameraMod);
    }

    public void SetRenderMode(int cameraMod) {
        locationComponent.setRenderMode(cameraMod);
    }
}
