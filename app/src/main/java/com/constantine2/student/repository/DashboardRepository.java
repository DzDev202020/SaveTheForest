package com.constantine2.student.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.constantine2.student.model.Client;
import com.constantine2.student.model.Fire;
import com.constantine2.student.model.FireList;
import com.constantine2.student.model.Post;
import com.constantine2.student.model.Scale;
import com.constantine2.student.retrofit.FireApi;
import com.constantine2.student.retrofit.RetrofitApi;
import com.constantine2.student.retrofit.ScaleApi;
import com.constantine2.student.viewModel.DashboardViewModel;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.ReverseGeoOptions;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.result.SearchResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    private static final String TAG = "TAG";
    FireApi fireApi;
    ScaleApi scaleApi;


    MutableLiveData<Integer> fireState;
    MutableLiveData<Integer> scaleState;
    MutableLiveData<Integer> fireListState;
    MutableLiveData<Integer> postListState;
    MutableLiveData<Integer> tabFocusState;

    List<Fire> fireList;
    List<Post> postList;

    int fireOnFocus = -1;

    Call<FireList> firesCall;
    Call<Fire> fireCall;
    Call<Fire> fireCreateCall;

    Client client;

    public DashboardRepository() {
        fireApi = RetrofitApi.getFireApi();
        scaleApi = RetrofitApi.getScaleApi();

        fireState = new MutableLiveData<>(DashboardViewModel.IDEAL_FIRE);
        scaleState = new MutableLiveData<>(DashboardViewModel.IDEAL_SCALE);
        fireListState = new MutableLiveData<>(DashboardViewModel.ON_READ_FIRES);
        postListState = new MutableLiveData<>(DashboardViewModel.IDEAL_POSTS);
        tabFocusState = new MutableLiveData<>(DashboardViewModel.TAB_IDEAL_FOCUS);

        fireList = new ArrayList<>();
        postList = new ArrayList<>();

        refreshFires();

    }


    public void tryFindAddreses(SearchEngine searchEngine) {

//        Double[] lats = new Double[]{36.29499, 36.2201, 36.15103, 36.14025, 36.21388, 36.40325, 36.4016, 36.34094, 36.34094, 36.28204};
//        Double[] logs = new Double[]{6.54033, 6.47852, 6.57592, 6.74108, 6.66205, 6.55485, 6.68074, 6.70579, 6.80761};
        Double[] lats = new Double[]{36.29899};
        Double[] logs = new Double[]{6.54533};
        for (int i = 0; i < 1; i++) {
            double latitude = lats[i];
            double longitude = logs[i];
            searchEngine.search(new ReverseGeoOptions.Builder(Point.fromLngLat(longitude, latitude)).limit(1).build(), new SearchCallback() {
                @Override
                public void onResults(@NotNull List<SearchResult> list, @NotNull ResponseInfo responseInfo) {
                    Log.e("TAG", "onResults: ");
                    if (list.isEmpty()) {
                        Log.e("TAG", "isEmpty");
                    } else {
                        Log.e("TAG", "! isEmpty");
                        SearchResult searchResult = list.get(0);
                        String s = "getCountry : " + searchResult.getAddress().getCountry() + "\\n" +
                                "getStreet : " + searchResult.getAddress().getStreet() + "\\n" +
                                "getPostcode : " + searchResult.getAddress().getPostcode() + "\\n" +
                                "getRegion : " + searchResult.getAddress().getRegion() + "\\n" +
                                "getPlace : " + searchResult.getAddress().getPlace() + "\\n" +
                                "getPlace : " + searchResult.getAddress().getPlace() + "\\n";
                        Log.e("TAG", s);
                    }
                }

                @Override
                public void onError(@NotNull Exception e) {
                    Log.e("TAG", "onError: e : " + e.getMessage());
                }
            });
        }

    }

    public MutableLiveData<Integer> getFireListState() {
        return fireListState;
    }

    public MutableLiveData<Integer> getScaleState() {
        return scaleState;
    }

    public MutableLiveData<Integer> getPostListState() {
        return postListState;
    }

    public MutableLiveData<Integer> getFocusState() {
        return tabFocusState;
    }

    public LiveData<Integer> getFireState() {
        return fireState;
    }


    public void refreshFires() {
        Log.e(TAG, "refreshFires: ");
        fireListState.setValue(DashboardViewModel.ON_READ_FIRES);
        fireOnFocus = -1;
        if (fireCall != null) {
            fireCall.cancel();
            fireCall = null;
        }

        firesCall = fireApi.getFiresList();
        firesCall.enqueue(new Callback<FireList>() {
            @Override
            public void onResponse(@NotNull Call<FireList> call, @NotNull Response<FireList> response) {
                Log.e(TAG, "refreshFires: onResponse: ");
                firesCall = null;
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: isSuccessful");
                    fireList.clear();
                    assert response.body() != null;
                    fireList.addAll(response.body().getFireList());
                    Log.e(TAG, "refreshFires: isSuccessful: fireList : " + fireList.size());
                    fireListState.setValue(DashboardViewModel.DONE_READ_FIRES);
                } else {
                    Log.e(TAG, "onResponse: not Successful");
                    fireListState.setValue(DashboardViewModel.FAIL_READ_FIRES);
                    fireListState.setValue(DashboardViewModel.IDEAL_READ_FIRES);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FireList> call, @NotNull Throwable t) {
                Log.e(TAG, "refreshFires: onFailure: " + t.getMessage());
                firesCall = null;
                fireListState.setValue(DashboardViewModel.FAIL_READ_FIRES);
            }
        });

    }

    public List<Fire> getFireList() {
        return fireList;
    }

    public void refreshPosts() {
        Log.e(TAG, "refreshPosts: ");
        postListState.setValue(DashboardViewModel.ON_READ_POSTS);

        fireCall = fireApi.getFireById(fireList.get(fireOnFocus).getId());
        Log.e(TAG, "refreshPosts: before: onfocus" + fireList.get(fireOnFocus).getrPosts().size());

        fireCall.enqueue(new Callback<Fire>() {
            @Override
            public void onResponse(@NotNull Call<Fire> call, @NotNull Response<Fire> response) {
                Log.e(TAG, "refreshPosts: onResponse");
                fireCall = null;
                if (response.isSuccessful()) {

                    Fire fire = response.body();
                    fireList.remove(fireOnFocus);
                    fireList.add(fireOnFocus, fire);

                    if (fire != null && fire.getrPosts() != null)
                        Log.e(TAG, "refreshPosts: onResponse: " + fire.getrPosts().size());
                    else
                        Log.e(TAG, "refreshPosts: onResponse: null ");
                    postListState.setValue(DashboardViewModel.DONE_READ_POST);
                } else {
                    postListState.setValue(DashboardViewModel.FAIL_READ_POST);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Fire> call, @NotNull Throwable t) {
                Log.e(TAG, "refreshPosts: onFailure");
                postListState.setValue(DashboardViewModel.FAIL_READ_POST);

            }
        });
    }

    public void cancelPostRefresh() {
        if (fireCall != null) {
            fireCall.cancel();
            fireCall = null;
        }
    }

    public List<Post> getPostList() {
        postList.clear();
        postList.addAll(fireList.get(fireOnFocus).getrPosts());
        return postList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client c) {
        if (client == null)
            client = c;
    }

    public Fire getLastFire() {
        return fireList.get(fireList.size() - 1);
    }

    public void createFire(SearchEngine searchEngine, double latitude, double longitude) {
        Log.e(TAG, "createFire: ");
        fireState.setValue(DashboardViewModel.ON_SAVE_FIRE);
        searchEngine.search(new ReverseGeoOptions.Builder(Point.fromLngLat(longitude, latitude)).limit(1).build(), new SearchCallback() {
            @Override
            public void onResults(@NotNull List<SearchResult> list, @NotNull ResponseInfo responseInfo) {
                Log.e(TAG, "onResults: ");
                if (list.isEmpty()) {
                    Log.e(TAG, "isEmpty: ");
                    fireState.setValue(DashboardViewModel.FAIL_SAVE_FIRE);
                    fireState.setValue(DashboardViewModel.IDEAL_FIRE);
                } else {
                    Log.e(TAG, "!isEmpty: ");
                    SearchResult searchResult = list.get(0);

                    Fire fire = new Fire();
                    fire.setrClient(client);

                    fire.setId(0);
                    fire.setLatitude(latitude);
                    fire.setLongitude(longitude);
                    fire.setCountry(searchResult.getAddress().getCountry());
                    fire.setCity(searchResult.getAddress().getRegion());
                    fire.setStreet(searchResult.getAddress().getPlace());
                    fire.setZipcode(searchResult.getAddress().getPostcode());

                    finalCreateFire(fire);
                }
            }

            @Override
            public void onError(@NotNull Exception e) {
                fireState.setValue(DashboardViewModel.FAIL_SAVE_FIRE);
                fireState.setValue(DashboardViewModel.IDEAL_FIRE);
            }
        });
    }

    private void finalCreateFire(Fire newFire) {
        fireCreateCall = fireApi.createFire(newFire);
        fireCreateCall.enqueue(new Callback<Fire>() {
            @Override
            public void onResponse(@NotNull Call<Fire> call, @NotNull Response<Fire> response) {
                Log.e(TAG, "onResponse: ");
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: isSuccessful");
                    fireList.add(response.body());
                    fireState.setValue(DashboardViewModel.DONE_SAVE_FIRE);
                } else {
                    Log.e(TAG, "onResponse:not isSuccessful ");
                    fireState.setValue(DashboardViewModel.FAIL_SAVE_FIRE);
                }

                fireState.setValue(DashboardViewModel.IDEAL_FIRE);
            }

            @Override
            public void onFailure(@NotNull Call<Fire> call, @NotNull Throwable t) {
                Log.e(TAG, "finalCreateFire:onFailure ");
                fireState.setValue(DashboardViewModel.FAIL_SAVE_FIRE);
                fireState.setValue(DashboardViewModel.IDEAL_FIRE);
            }
        });
    }

    public void readPostsOfFire(int position) {
        fireOnFocus = position;
        postListState.setValue(DashboardViewModel.DONE_READ_POST);
    }

    public void focusFireInList(int symbol) {
        Log.e(TAG, "focusFireInList: before: " + fireOnFocus);
//        double lat = symbol.getLatLng().getLatitude();
//        double lon = symbol.getLatLng().getLongitude();
//        Log.e(TAG, "focusFireInList: " + lon);
//        for (int i = 0; i < fireList.size(); i++) {
//            Fire fire = fireList.get(i);
//            if (fire.getLatitude() == lat && fire.getLongitude() == lon)
//
//        }
        fireOnFocus = symbol;
        Log.e(TAG, "focusFireInList: after: " + fireOnFocus);
        tabFocusState.setValue(DashboardViewModel.TAB_LIST_FOCUS);
    }

    public void focusFireInMap(int position) {
        fireOnFocus = position;
        tabFocusState.setValue(DashboardViewModel.TAB_MAP_FOCUS);
    }

    public void setFocusStateIdeal() {
        tabFocusState.setValue(DashboardViewModel.TAB_IDEAL_FOCUS);
    }

    public LatLng getFocusLocation() {
        return new LatLng(fireList.get(fireOnFocus).getLatitude(), fireList.get(fireOnFocus).getLongitude());
    }

    public int getFocusFirePosition() {
        return fireOnFocus;
    }


    public void createScale(int which) {
        Scale.FireScale fireScale;

        switch (which) {
            default:
                fireScale = Scale.FireScale.LOW;
                break;
            case 1:
                fireScale = Scale.FireScale.MEDIUM;
                break;

            case 2:
                fireScale = Scale.FireScale.HIGH;
                break;

        }
        Scale scale = new Scale(0, client.getId(), fireList.get(fireOnFocus).getId(), fireScale, null);
        scale.setrClient(client);
        scale.setrFire(fireList.get(fireOnFocus));
        scale.setId_c(client.getId());
        scale.setId_f(fireList.get(fireOnFocus).getId());

        scaleApi.createScale(scale)
                .enqueue(new Callback<Scale>() {
                    @Override
                    public void onResponse(@NotNull Call<Scale> call, @NotNull Response<Scale> response) {
                        Log.e(TAG, "createScale: onResponse: ");
                        if (response.isSuccessful()) {
                            Log.e(TAG, "createScale: onResponse: isSuccessful");
                            scaleState.setValue(DashboardViewModel.DONE_SCALE);
                        } else {
                            Log.e(TAG, "createScale: onResponse: not Successful");
                            scaleState.setValue(DashboardViewModel.FAIL_SCALE);
                        }
                        scaleState.setValue(DashboardViewModel.IDEAL_SCALE);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Scale> call, @NotNull Throwable t) {
                        Log.e(TAG, "createScale: onFailure: " + t.getMessage());
                        scaleState.setValue(DashboardViewModel.FAIL_SCALE);
                        scaleState.setValue(DashboardViewModel.IDEAL_SCALE);
                    }
                });
    }

    public Fire getFireOnFocus() {
        return fireList.get(fireOnFocus);
    }


    //

}
