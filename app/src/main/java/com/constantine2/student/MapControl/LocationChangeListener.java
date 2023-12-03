package com.constantine2.student.MapControl;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;

import java.lang.ref.WeakReference;

public class LocationChangeListener implements LocationEngineCallback<LocationEngineResult> {
    private final WeakReference<Activity> activityWeakReference;
    private LocationChangeListenerCallBack callBack;
    public LocationChangeListener(Activity activityWeakReference, LocationChangeListenerCallBack callBack) {
        this.activityWeakReference = new WeakReference<Activity>(activityWeakReference);
        this.callBack=callBack;
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        if (activityWeakReference.get()!=null && callBack != null)
            callBack.OnSuccessGetLocation(result);
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        if (activityWeakReference.get()!=null && callBack != null)
            callBack.OnFailureGetLocation(exception);
    }



    public interface  LocationChangeListenerCallBack
    {
        public void OnSuccessGetLocation(LocationEngineResult result);
        public void OnFailureGetLocation(@NonNull Exception exception);

    }
}
