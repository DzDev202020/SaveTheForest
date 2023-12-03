package com.constantine2.student.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.constantine2.student.model.Client;
import com.constantine2.student.model.Fire;
import com.constantine2.student.model.Post;
import com.constantine2.student.repository.DashboardRepository;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.search.SearchEngine;


import java.util.List;

public class DashboardViewModel extends ViewModel {

    DashboardRepository repository;

    public static final int IDEAL_SCALE = 0;
    public static final int DONE_SCALE = 1;
    public static final int FAIL_SCALE = 2;


    public static final int IDEAL_FIRE = 0;
    public static final int ON_SAVE_FIRE = 1;
    public static final int DONE_SAVE_FIRE = 2;
    public static final int FAIL_SAVE_FIRE = 3;

    public static final int IDEAL_READ_FIRES = 0;
    public static final int ON_READ_FIRES = 1;
    public static final int FAIL_READ_FIRES = 2;
    public static final int DONE_READ_FIRES = 3;

    public static final int IDEAL_POSTS = 0;
    public static final int ON_READ_POSTS = 1;
    public static final int FAIL_READ_POST = 2;
    public static final int DONE_READ_POST = 3;


    public static final int TAB_IDEAL_FOCUS = 0;
    public static final int TAB_MAP_FOCUS = 1;
    public static final int TAB_LIST_FOCUS = 2;

    public DashboardViewModel() {
        repository = new DashboardRepository();
    }

    public LiveData<Integer> getFireListState() {
        return repository.getFireListState();
    }

    public LiveData<Integer> getScaleState() {
        return repository.getScaleState();
    }

    public LiveData<Integer> getPostListState() {
        return repository.getPostListState();
    }

    public LiveData<Integer> getFocusState() {
        return repository.getFocusState();
    }

    public void refreshFires() {
        repository.refreshFires();
    }

    public List<Fire> getFireList() {
        return repository.getFireList();
    }

    public void refreshPosts() {
        repository.refreshPosts();
    }

    public List<Post> getPostList() {
        return repository.getPostList();
    }

    public void cancelPostRefresh() {
        repository.cancelPostRefresh();
    }


    public void setClient(Client c) {
        repository.setClient(c);
    }

    public Client getClient() {
        return repository.getClient();
    }


    public void setFocusStateIdeal() {
        repository.setFocusStateIdeal();
    }

    public LatLng getFocusFireLocation() {
        return repository.getFocusLocation();
    }

    public int getFocusFirePosition() {
        return repository.getFocusFirePosition();
    }


    public void readPostsOfFire(int position) {
        repository.readPostsOfFire(position);
    }

    public LiveData<Integer> getFireState() {
        return repository.getFireState();
    }

    public Fire getLastFire() {
        return repository.getLastFire();
    }


    public void createFire(SearchEngine searchEngine, double latitude, double longitude) {
        repository.createFire(searchEngine, latitude, longitude);
    }

    public void createScale(int which) {
        repository.createScale(which);
    }

    public void focusFireInList(int symbol) {
        repository.focusFireInList(symbol);
    }

    public void focusFireInMap(int position) {
        repository.focusFireInMap(position);
    }


    public Fire getFireOnFocus() {
        return repository.getFireOnFocus();
    }


    public void tryFindAddreses(SearchEngine searchEngine){
        repository.tryFindAddreses(searchEngine);
    }
}
