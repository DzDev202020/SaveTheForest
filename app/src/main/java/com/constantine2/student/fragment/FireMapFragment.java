package com.constantine2.student.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.constantine2.student.MapControl.CameraManager;
import com.constantine2.student.MapControl.LocationChangeListener;
import com.constantine2.student.MapControl.UserLocationManager;
import com.constantine2.student.R;
import com.constantine2.student.databinding.FragmentFireMapBinding;
import com.constantine2.student.dialog.CreateFireMarkerSelectDialog;
import com.constantine2.student.model.Fire;
import com.constantine2.student.viewModel.DashboardViewModel;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.AnnotationManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.viewannotation.ViewAnnotationManager;
import com.mapbox.search.MapboxSearchSdk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;

public class FireMapFragment extends Fragment {


    private final static int MY_REQUEST_CODE = 2021;

    DashboardViewModel viewModel;
    FragmentFireMapBinding bind;
    String[] locationAccessPermissionNeeded;

    public FireMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        bind.mapView.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        bind.mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        bind.mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.mapView.onDestroy();

        if (createFireMarkerSelectDialog != null)
            createFireMarkerSelectDialog.dismiss();
        if (locationChangeListener != null)
            userLocationManager.GetLocationEngine().removeLocationUpdates(locationChangeListener);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(requireContext(), getString(R.string.myMapboxAccessToken));
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(MapboxSearchSdk.)
//        MapboxSearchSdk.initialize(requireActivity().getApplication(), getString(R.string.mapbox_access_token));

        bind = FragmentFireMapBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        initStartView();
        switchPermission();
        bind.confirmCreateFireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        viewModel.tryFindAddreses(MapboxSearchSdk.getReverseGeocodingSearchEngine());
        return bind.getRoot();
    }

    private void initStartView() {
        bind.postFire.setVisibility(View.GONE);

        bind.navigationInformation.setVisibility(View.GONE);

        bind.createFireButtons.setVisibility(View.GONE);
        bind.createFireMarker.setVisibility(View.GONE);

        bind.progressBar.setVisibility(View.GONE);
        bind.progressBarBackground.setVisibility(View.GONE);
    }

    private void switchPermission() {
        if (!checkLocationAccessPermission()) {
            setButtonToGetLocationAccessPermission();
        } else {
            hideTextButton();
            initMapView();
        }
    }


    private void showTextButton() {
        bind.stateText.setVisibility(View.VISIBLE);
        bind.action.setVisibility(View.VISIBLE);
    }

    private void hideTextButton() {
        bind.stateText.setVisibility(View.GONE);
        bind.action.setVisibility(View.GONE);
    }

    private void createToastNoPermissionGranted() {
        Toast.makeText(requireContext(), R.string.need_permission, Toast.LENGTH_LONG).show();
    }

    private void setButtonToGetLocationAccessPermission() {
        bind.stateText.setText(R.string.need_location_access_permission_message);
        bind.action.setOnClickListener(view -> {
            requireActivity().requestPermissions(locationAccessPermissionNeeded, MY_REQUEST_CODE);
            bind.stateText.setText(R.string.got_permission_check_it);
            setActionButtonGotPermission();
        });
    }

    private void setActionButtonGotPermission() {
        bind.action.setOnClickListener(view -> {
            switchPermission();
        });
    }


    private boolean checkLocationAccessPermission() {
        boolean hasFineLocationAccess = false;
        boolean hasCoarseLocationAccess = false;
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            hasFineLocationAccess = true;
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            hasCoarseLocationAccess = true;

        if (hasCoarseLocationAccess && hasFineLocationAccess)
            return true;
        else {
            int size = 0;
            if (!hasCoarseLocationAccess)
                size++;
            if (!hasFineLocationAccess)
                size++;
            locationAccessPermissionNeeded = new String[size];

            size = 0;
            if (!hasCoarseLocationAccess)
                locationAccessPermissionNeeded[size] = Manifest.permission.ACCESS_COARSE_LOCATION;
            size++;
            if (!hasFineLocationAccess)
                locationAccessPermissionNeeded[size] = Manifest.permission.ACCESS_FINE_LOCATION;
            return false;
        }
    }


    MapboxMap mapboxMap;
    boolean mapReady;
    boolean userLocationManagerActive;

    ViewAnnotationManager pointAnnotationManager;

    SymbolManager symbolManager;
    OnSymbolClickListener symbolClickListener;
    List<Symbol> symbolList;


    UserLocationManager userLocationManager;
    CameraManager cameraManager;


    @SuppressLint("UseCompatLoadingForDrawables")
    private void initMapView() {
        if (mapReady) {
            enableUserLocation();
            return;
        }
        bind.mapView.getMapboxMap().loadStyle(new Style.Builder().fromUri("mapbox://styles/ayoubkhal/ckcp5wl2m05ep1item1uclb3e"), new com.mapbox.maps.Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull com.mapbox.maps.Style style) {
                mapboxMap = bind.mapView.getMapboxMap();

                style.addImage("fire", BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.fire_marker));
                style.addImage("fireman", BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.fireman_marker));



                if (symbolManager == null) {

                     pointAnnotationManager = bind.mapView.getViewAnnotationManager();

//                    symbolManager = new SymbolManager(bind.mapView, mapboxMap, style);
//                    symbolManager = new SymbolManager(bind.mapView,mapboxMap,style);
//                    symbolManager.setIconAllowOverlap(false);
//                    symbolManager.setIconTranslate(new Float[]{-4f, 5f});
//                    if (symbolClickListener == null) {
//                        symbolClickListener = this::foundSymbol;
//                        symbolManager.addClickListener(symbolClickListener);
//                    }
                }

                userLocationManager = new UserLocationManager(requireContext(), mapboxMap.getLocationComponent());
                userLocationManager.ActivateLocationComponent(style);

                cameraManager = new CameraManager(mapboxMap);

                mapReady = true;

                enableUserLocation();
            }
        });


    }


    private void enableUserLocation() {
        Log.e("TAG", "enableUserLocation: ");
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            showTextButton();
            setButtonToGetLocationAccessPermission();
            createToastNoPermissionGranted();
            return;
        }
        userLocationManager.GetLocationEngine().getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {

                if (result.getLastLocation() == null) {
                    return;
                }
                cameraManager.EaseCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude()))
                        .tilt(0)
                        .zoom(16)
                        .build(), CameraManager.DEFAULT_DURATION);


                symbolList = new ArrayList<>();

                userLocationManagerActive = true;
                InitMapActionButtonsAfterPermissionAndLocation();

                startListenForFiresState();
                startListenForFireState();
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                showTextButton();
                setActionEnableUserLocation();
            }
        });

    }

    private void setActionEnableUserLocation() {
        bind.stateText.setText(R.string.enable_user_location);
        bind.action.setText(R.string.enable);
        bind.action.setOnClickListener(v -> {
            hideTextButton();
            enableUserLocation();
        });
    }

    private void InitMapActionButtonsAfterPermissionAndLocation() {
        bind.postFire.setVisibility(View.VISIBLE);
        setPostFireClickListener();

        bind.cancelCreateFireButton.setOnClickListener(v -> {
            bind.postFire.setVisibility(View.VISIBLE);

            bind.createFireMarker.setVisibility(View.GONE);
            bind.createFireButtons.setVisibility(View.GONE);
        });

        bind.confirmCreateFireButton.setOnClickListener(v -> {

            bind.createFireMarker.setVisibility(View.GONE);
            bind.createFireButtons.setVisibility(View.GONE);

            createFire();
        });
    }

    private void createFire() {
        double latitude = mapboxMap.getCameraPosition().target.getLatitude();
        double longitude = mapboxMap.getCameraPosition().target.getLongitude();
        viewModel.createFire(MapboxSearchSdk.getReverseGeocodingSearchEngine(), latitude, longitude);
    }

    private void startListenForFireState() {
        Log.e("TAG", "startListenForFireState: ");
        viewModel.getFireState().observe(getViewLifecycleOwner(), integer -> {
            Log.e("TAG", "startListenForFireState: " + integer);
            switch (integer) {
                case DashboardViewModel.IDEAL_FIRE:
                    break;
                case DashboardViewModel.ON_SAVE_FIRE:
                    showProgressBarOfCreationFire();
                    break;
                case DashboardViewModel.DONE_SAVE_FIRE:
                    hideProgressBarOfCreationFire();
                    addLastFire();
                    break;
                case DashboardViewModel.FAIL_SAVE_FIRE:
                    hideProgressBarOfCreationFire();
                    Toast.makeText(getContext(), R.string.fail_fire_creation, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void addLastFire() {
        Fire fire = viewModel.getLastFire();
        if (symbolList.get(symbolList.size() - 1).getLatLng().getLatitude() != fire.getLatitude() && symbolList.get(symbolList.size() - 1).getLatLng().getLongitude() != fire.getLongitude()) {
            SymbolOptions symbolOptions = new SymbolOptions()
                    .withIconImage("fire")
                    .withIconSize(ICON_SIZE)
                    .withLatLng(new LatLng(fire.getLatitude(), fire.getLongitude()));
            symbolList.add(symbolManager.create(symbolOptions));
        }
    }

    private void showProgressBarOfCreationFire() {
        bind.progressBar.setVisibility(View.VISIBLE);
        bind.progressBarBackground.setVisibility(View.VISIBLE);
        bind.postFire.setVisibility(View.GONE);
    }

    private void hideProgressBarOfCreationFire() {
        bind.progressBar.setVisibility(View.GONE);
        bind.progressBarBackground.setVisibility(View.GONE);
        bind.postFire.setVisibility(View.VISIBLE);
    }


    private void startListenForFiresState() {
        Log.e("TAG", "startListenForFiresState: ");
        viewModel.getFireListState().observe(getViewLifecycleOwner(), integer -> {
            Log.e("TAG", "startListenForFiresState: observe" + integer);
            if (integer == DashboardViewModel.DONE_READ_FIRES) {
                Log.e("TAG", "startListenForFiresState: DONE_READ_FIRES");
                setMapWithFiresLocations();
                switchFireFocus();

            }
        });

    }

    private void setMapWithFiresLocations() {
        symbolManager.deleteAll();
        symbolList.clear();
        List<Fire> fires = viewModel.getFireList();

        for (Fire fire : fires) {
            SymbolOptions symbolOptions = new SymbolOptions()
                    .withIconImage("fire")
                    .withIconSize(ICON_SIZE)
                    .withLatLng(new LatLng(fire.getLatitude(), fire.getLongitude()));
            symbolList.add(symbolManager.create(symbolOptions));
        }

    }


    private void switchFireFocus() {
        Log.e("TAG", "switchFireFocus: map");
        viewModel.getFocusState().observe(getViewLifecycleOwner(), integer -> {
            if (integer == DashboardViewModel.TAB_MAP_FOCUS) {
                viewModel.setFocusStateIdeal();
                focusFire(viewModel.getFocusFireLocation());
            }
        });
//        if (viewModel.getFocusState().getValue() != null && viewModel.getFocusState().getValue() == DashboardViewModel.TAB_MAP_FOCUS) {
//            viewModel.setFocusStateIdeal();
//            focusFire(viewModel.getFocusFireLocation());
//        }
    }

    private void focusFire(LatLng latLng) {
        Log.e("TAG", "focusFire: map");
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(latLng)
//                .bearing(mapboxMap.getCameraPosition().bearing)
                .zoom(15)
//                .tilt(mapboxMap.getCameraPosition().tilt)
                .build();
        cameraManager.AnimateCameraPosition(cameraPosition, 1500);

    }

    CreateFireMarkerSelectDialog createFireMarkerSelectDialog;

    private void foundSymbol(Symbol symbol) {
//        if (createFireMarkerSelectDialog == null)
        createFireMarkerSelectDialog = new CreateFireMarkerSelectDialog(requireContext(), new CreateFireMarkerSelectDialog.DirectionDialogCallback() {
            @Override
            public void createDirection() {
                createDirectionToFire(symbol);
            }

            @Override
            public void readPosts() {
                for (int i = 0; i < symbolList.size(); i++) {
                    Symbol symbol1 = symbolList.get(i);
                    if (symbol1.getId() == symbol.getId() &&
                            symbol1.getLatLng().getLatitude() == symbol.getLatLng().getLatitude() &&
                            symbol1.getLatLng().getLongitude() == symbol.getLatLng().getLongitude()) {
                        Log.e("TAG", "foundSymbol: " + i);
                        viewModel.focusFireInList(i);
                        return;
                    }

                }
                Toast.makeText(getContext(), R.string.cant_find_fire_in_list, Toast.LENGTH_SHORT).show();

            }
        });
        createFireMarkerSelectDialog.show();
    }


    private DirectionsRoute currentRoute;
    LineManager lineManager;

    double startDistance = 0;
    double startTime = 0;
    double diameterMulti = 0;

    Symbol destination;
    Location userLocation;

    private void hidePostFire() {
        bind.postFire.setVisibility(View.GONE);
    }

    private void showPostFire() {
        bind.postFire.setVisibility(View.VISIBLE);
    }

    private void createDirectionToFire(Symbol symbol) {
        destination = symbol;
        getUserLocation();
    }

    void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            createToastNoPermissionGranted();
            return;
        }

        hidePostFire();

        userLocationManager.GetLocationEngine().getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                userLocation = result.getLastLocation();
                cameraManager.FiTCameraWihEase(new LatLngBounds.Builder()
                                .include(new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude()))
                                .include(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                                .build()
                        , 0, 0, 100);

                userLocation = result.getLastLocation();
                RequestDirection();
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(requireContext(), R.string.cant_get_location, Toast.LENGTH_LONG).show();
                showPostFire();
            }
        });
    }

    public void RequestDirection() {

        MapboxDirections directions = MapboxDirections.builder()
                .origin(Point.fromLngLat(this.userLocation.getLongitude(), this.userLocation.getLatitude()))
                .destination(Point.fromLngLat(destination.getLatLng().getLongitude(), destination.getLatLng().getLatitude()))
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.myMapboxAccessToken))
                .build();

        directions.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(requireContext(), R.string.cant_find_direction, Toast.LENGTH_LONG).show();
                    showPostFire();
                    return;
                } else if (response.body().routes().size() < 1) {
                    Toast.makeText(requireContext(), R.string.cant_find_direction, Toast.LENGTH_LONG).show();
                    showPostFire();
                    return;
                }
                currentRoute = response.body().routes().get(0);
                afterGotRoutes();

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(requireContext(), R.string.cant_find_direction, Toast.LENGTH_LONG).show();
            }
        });

    }

    void afterGotRoutes() {

        bind.navigationInformation.setVisibility(View.VISIBLE);
        if (lineManager == null)
            lineManager = new LineManager(bind.mapView, mapboxMap, mapboxMap.getStyle(), symbolManager.getLayerId());


        lineManager.create(new LineOptions()
                .withLineColor(ColorUtils.colorToRgbaString(Color.CYAN))
                .withLineWidth(3.0f)

                .withGeometry(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6)));


        startTime = currentRoute.duration();
        startDistance = currentRoute.distance();
        diameterMulti = currentRoute.distance() / getDistanceFromLatLonInKm(destination.getLatLng().getLatitude(),
                destination.getLatLng().getLongitude(),
                userLocation.getLatitude(),
                userLocation.getLongitude());

        updateDistanceAndTime(startDistance, startTime);
        startFetchUiOnLocationChange();
        setFloatButtonToCancelDirection();
    }

    private void setPostFireClickListener() {
        bind.postFire.setOnClickListener(v -> {

            bind.postFire.setVisibility(View.GONE);

            bind.createFireMarker.setVisibility(View.VISIBLE);
            bind.createFireButtons.setVisibility(View.VISIBLE);

        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setFloatButtonToCancelDirection() {
        bind.postFire.setVisibility(View.VISIBLE);
        bind.postFire.setImageDrawable(getResources().getDrawable(R.drawable.icon_close, requireActivity().getTheme()));
        bind.postFire.setOnClickListener(v -> {
            setPostFireClickListener();
            bind.postFire.setImageDrawable(getResources().getDrawable(R.drawable.fire, requireActivity().getTheme()));

            lineManager.deleteAll();
            bind.navigationInformation.setVisibility(View.GONE);
        });
    }

    private final static long DEFAULT_INTERVAL_IN_MILLISECONDS = 30000L;
    private final static long MAX_WAIT_TIME_IN_MILLISECONDS = 60000L;

    LocationChangeListener locationChangeListener;

    private void startFetchUiOnLocationChange() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            createToastNoPermissionGranted();
            return;
        }
        userLocationManager.SetLocationEngine(LocationEngineProvider.getBestLocationEngine(requireContext()));
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setDisplacement(5)
                .setMaxWaitTime(MAX_WAIT_TIME_IN_MILLISECONDS)
                .build();
        if (locationChangeListener != null)
            userLocationManager.GetLocationEngine().removeLocationUpdates(locationChangeListener);
        locationChangeListener = new LocationChangeListener(requireActivity(), new LocationChangeListener.LocationChangeListenerCallBack() {
            @Override
            public void OnSuccessGetLocation(LocationEngineResult result) {
                Location newLocation = result.getLastLocation();
                double distance = diameterMulti * getDistanceFromLatLonInKm(destination.getLatLng().getLatitude(), destination.getLatLng().getLongitude()
                        , newLocation.getLatitude(), newLocation.getLongitude());
                updateDistanceAndTime(distance, startTime * distance / startDistance);
            }

            @Override
            public void OnFailureGetLocation(@NonNull Exception exception) {
                Log.e("Ayoub", "OnFailureGetLocation");
            }
        });

        userLocationManager.GetLocationEngine().requestLocationUpdates(request, locationChangeListener, requireActivity().getMainLooper());
    }

    public void updateDistanceAndTime(double distance, double time) {
        String text;
        if (distance < 1000)
            text = (int) distance + " M";
        else
            text = (int) (distance / 1000) + " KM";
        bind.distanceText.setText(text);
        if (time < 3600)
            text = (int) (time / 60) + " Min";
        else
            text = (int) (time / 3600) + " H " + (int) ((time % 3600) / 60) + " Min";
        bind.timeText.setText(text);
    }


    public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    public double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    private static final Float ICON_SIZE = 1.2f;


}