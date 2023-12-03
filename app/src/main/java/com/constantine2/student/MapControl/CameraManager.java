package com.constantine2.student.MapControl;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class CameraManager {

    // tilt default is 0 degrees, and the maximum tilt is 60
    public static int DEFAULT_DURATION=2000;
    public static int DEFAULT_FOCUS_DURATION=1000;
    public static int DEFAULT_ZOOM=15;
    public static int DEFAULT_TILT=0;
    public static int DEFAULT_BEARING=0;
    public static int DEFAULT_CAMERA_PADDING=50;

    MapboxMap mapboxMap;

    public CameraManager(MapboxMap mapboxMap)
    {
        this.mapboxMap=mapboxMap;
    }
    public void MoveCameraPosition(CameraPosition cameraPosition)
    {
       mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void AnimateCameraPosition(CameraPosition cameraPosition, int duration)
    {
       mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),duration);
    }
    public void AnimateCameraPosition(CameraPosition cameraPosition, int duration, MapboxMap.CancelableCallback cancelableCallback)
    {
       mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),duration,cancelableCallback);
    }

    public void EaseCameraPosition(CameraPosition cameraPosition, int duration)
    {
        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration);
    }
        public void EaseCameraPosition(CameraPosition cameraPosition, int duration, MapboxMap.CancelableCallback cancelableCallback)
    {
        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, cancelableCallback);
    }

    public void SetRestrictMapPanning(LatLngBounds restrictBoundsArea )
    {
        mapboxMap.setLatLngBoundsForCameraTarget(restrictBoundsArea);
    }

    public void FiTCameraWihEase(LatLngBounds latLngBounds, double tilt, double bearing, int padding )
    {
        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,bearing,tilt,padding));
    }
    public void FiTCameraWihAnimation(LatLngBounds latLngBounds, double tilt, double bearing, int padding )
    {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,bearing,tilt,padding));
    }

    public void   SetCameraIdealListener(MapboxMap.OnCameraIdleListener listener){
        mapboxMap.addOnCameraIdleListener(listener);
    }
    public void  SetOnCameraMoveListener(MapboxMap.OnCameraMoveListener listener){
        mapboxMap.addOnCameraMoveListener(listener);
    }

    /* mapboxMap.setCameraPosition(new CameraPosition.Builder()
        .target(new LatLng(60.169091, 24.939876))
        .zoom(12)
        .tilt(20)
        .bearing(90)
        .build()
      );
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
        .include(locationOne) // Northeast
        .include(locationTwo) // Southwest
        .build();
        */
}




