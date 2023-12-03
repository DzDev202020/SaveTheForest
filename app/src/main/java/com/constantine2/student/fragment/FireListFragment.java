package com.constantine2.student.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.constantine2.student.CreatePostActivity;
import com.constantine2.student.R;
import com.constantine2.student.adapter.FireListAdapter;
import com.constantine2.student.adapter.PostListAdapter;
import com.constantine2.student.databinding.FragmentFireListBinding;
import com.constantine2.student.model.Client;
import com.constantine2.student.model.Fire;
import com.constantine2.student.model.Post;
import com.constantine2.student.viewModel.DashboardViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class FireListFragment extends Fragment {

    DashboardViewModel viewModel;
    FragmentFireListBinding bind;

//    ItemPostListHeaderBinding postListHeaderBinding;

    FireListAdapter fireListAdapter;
    PostListAdapter postListAdapter;


    AlertDialog focusFireInMapDialog;
    AlertDialog scaleAlertDialog;


    boolean canTapFire = true;


    public FireListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (focusFireInMapDialog != null)
            focusFireInMapDialog.dismiss();
        if (scaleAlertDialog != null)
            scaleAlertDialog.dismiss();
    }

    @Override

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentFireListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        viewModel.getFireListState().observe(getViewLifecycleOwner(), this::switchFireListState);

        viewModel.getPostListState().observe(getViewLifecycleOwner(), this::switchPostListState);

        bind.backButton.setOnClickListener(v -> {
            animateBackToFireList();
            viewModel.cancelPostRefresh();
        });

        return bind.getRoot();
    }

    private void switchFireListState(Integer integer) {
        switch (integer) {
            case DashboardViewModel.ON_READ_FIRES:
                setFireRecyclerViewLoad();
                break;
            case DashboardViewModel.FAIL_READ_FIRES:
                setFireRecyclerViewError();
                break;
            case DashboardViewModel.DONE_READ_FIRES:
                initFireRecyclerView();
                setFireRecyclerViewData();
                switchFireFocus();
                startSwitchScaleState();
                break;
        }
    }

    private void startSwitchScaleState() {
        viewModel.getScaleState().removeObservers(getViewLifecycleOwner());
        viewModel.getScaleState().observe(getViewLifecycleOwner(), integer -> {
            switch (integer) {
                case DashboardViewModel.IDEAL_SCALE:
                    break;
                case DashboardViewModel.FAIL_SCALE:
                    Toast.makeText(getContext(), R.string.fail_report_scale, Toast.LENGTH_SHORT).show();
                    break;
                case DashboardViewModel.DONE_SCALE:
                    Toast.makeText(getContext(), R.string.done_report_scale, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


    private void switchFireFocus() {
        Log.e("TAG", "switchFireFocus: ");
        viewModel.getFocusState().removeObservers(getViewLifecycleOwner());
        viewModel.getFocusState().observe(getViewLifecycleOwner(), integer -> {
            if (integer == DashboardViewModel.TAB_LIST_FOCUS) {
                viewModel.setFocusStateIdeal();
                focusFire(viewModel.getFocusFirePosition());
            }
        });
//        if (viewModel.getFocusState().getValue() != null && viewModel.getFocusState().getValue() == DashboardViewModel.TAB_LIST_FOCUS) {
//            viewModel.setFocusStateIdeal();
//            focusFire(viewModel.getFocusFirePosition());
//        }
    }

    private void focusFire(int position) {
        Log.e("TAG", "fragment: focusFire: " + position);
        bind.firesListRecyclerView.getRecyclerView().scrollToPosition(position);
        fireListAdapter.setSelectedFire(position);
    }


    public void initFireRecyclerView() {

        if (fireListAdapter != null)
            return;
        fireListAdapter = new FireListAdapter(getContext(), position -> {
            if (!canTapFire)
                return;
            viewModel.readPostsOfFire(position);
            fireListAdapter.setSelectedFire(position);
            animateForwardToFirePosts();
        }, position -> {
            if (!canTapFire)
                return;
            focusFireInMapDialog = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.fire_location)
                    .setMessage(R.string.fire_location_content)
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    })
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        viewModel.focusFireInMap(position);
                        fireListAdapter.setSelectedFire(position);
                    })
                    .create();
            focusFireInMapDialog.show();
        });


        bind.firesListRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
//        bind.firesListRecyclerView.getRecyclerView().setOnRefreshListener(() -> {
//            fireListAdapter.setSelectedFire(-1);
//            viewModel.refreshFires();
//
//        });

        bind.firesListRecyclerView.getRecyclerView().setAdapter(fireListAdapter);

    }

//    private void unLockFireList() {
//        canTapFire = true;
//        bind.firesListRecyclerView.getRecyclerView().setSwipeEnable(true);
//
//    }
//
//    private void LockFireList() {
//        canTapFire = false;
//        bind.firesListRecyclerView.getRecyclerView().setSwipeEnable(false);
//    }

    public void setFireRecyclerViewData() {
        Log.e("TAG", "setFireRecyclerViewData: ");
//        bind.firesListRecyclerView.getRecyclerView().setRefreshing(false);
        List<Fire> list = viewModel.getFireList();
        Log.e("TAG", "setFireRecyclerViewData: " + list.size());
        if (list != null && list.size() != 0) {

            bind.firesListRecyclerView.hideAllViews();
            fireListAdapter.setFireList(list);
        } else {
            bind.firesListRecyclerView.showEmptyView();
        }
    }

    private void setFireRecyclerViewLoad() {
        bind.firesListRecyclerView.showLoadingView();
    }

    private void setFireRecyclerViewError() {
        bind.firesListRecyclerView.showErrorView(null);
        bind.firesListRecyclerView.setOnRetryClickListener(v -> viewModel.refreshFires());
    }


    private void switchPostListState(Integer integer) {

        switch (integer) {
            case DashboardViewModel.IDEAL_POSTS:
                break;
            case DashboardViewModel.ON_READ_POSTS:
                setPostRecyclerViewLoad();
                lockPostList();
                break;
            case DashboardViewModel.FAIL_READ_POST:
                setPostRecyclerViewError();
                unlockPostList();
                break;
            case DashboardViewModel.DONE_READ_POST:
                initPostRecyclerView();
                setPostRecyclerViewData();
                unlockPostList();
                break;
        }

    }


    private void lockPostList() {

        bind.backButton.setEnabled(false);
        bind.createPost.setEnabled(false);
        bind.createScale.setEnabled(false);

//        bind.postsListRecyclerView.getRecyclerView().setSwipeEnable(false);
    }

    private void unlockPostList() {

        bind.backButton.setEnabled(true);
        bind.createPost.setEnabled(true);
        bind.createScale.setEnabled(true);

//        bind.postsListRecyclerView.getRecyclerView().setSwipeEnable(true);
    }

    private void setPostRecyclerViewLoad() {
        bind.postsListRecyclerView.showLoadingView();
    }

    private void setPostRecyclerViewError() {
        bind.postsListRecyclerView.showErrorView(null);
        bind.postsListRecyclerView.setOnRetryClickListener(v -> viewModel.refreshPosts());
    }


    public void initPostRecyclerView() {
        if (postListAdapter != null)
            return;

        postListAdapter = new PostListAdapter(getContext(), position -> {
            // single click
        }, position -> {
            // long click
        });


        bind.postsListRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));


//        bind.postsListRecyclerView.getRecyclerView().setOnRefreshListener(() -> viewModel.refreshPosts());

        initPostsHeader();

        bind.postsListRecyclerView.getRecyclerView().setAdapter(postListAdapter);

    }


    public void initPostsHeader() {
//        postListHeaderBinding = ItemPostListHeaderBinding.inflate(getLayoutInflater(), bind.postsListRecyclerView.getRecyclerView(), false);

        bind.createScale.setOnClickListener(v -> {
            scaleAlertDialog = new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.select_scale)
                    .setItems(new String[]{
                                    getString(R.string.low),
                                    getString(R.string.medium),
                                    getString(R.string.high)},
                            (dialog, which) -> viewModel.createScale(which)).create();

            scaleAlertDialog.show();
        });
        bind.createPost.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), CreatePostActivity.class);
            Client client = viewModel.getClient();
            Fire fire = viewModel.getFireOnFocus();
            Fire fire1= new Fire();

            fire1.setId(fire.getId());
            fire1.setStreet(fire.getStreet());
            fire1.setCity(fire.getCity());
            fire1.setCountry(fire.getCountry());


            Bundle bundle = new Bundle();
            bundle.putSerializable("client", client);
            bundle.putSerializable("fire", fire1);
            i.putExtras(bundle);
            requireActivity().startActivity(i);
        });

    }


    private void setPostRecyclerViewData() {
        Fire fire = viewModel.getFireOnFocus();
        String s = "";
        if (fire.getStreet() != null)
            s += fire.getStreet() + ", ";
        if (fire.getCity() != null)
            s += fire.getCity() + ", ";
        if (fire.getCountry() != null)
            s += fire.getCountry() + ", ";
        if (fire.getZipcode() != null)
            s += fire.getZipcode();
        if (s.equals(""))
            bind.fireAddress.setText(R.string.unknow_address);
        else
            bind.fireAddress.setText(s);

//        bind.postsListRecyclerView.getRecyclerView().setRefreshing(false);
        List<Post> list = viewModel.getPostList();
        if (list != null && list.size() != 0) {

            bind.postsListRecyclerView.hideAllViews();
            postListAdapter.setPostList(list);
        } else {
            postListAdapter.setPostList(list);
            bind.postsListRecyclerView.showEmptyView();
        }


    }

    private void animateBackToFireList() {
        lockPostList();
//        unLockFireList();
        bind.motionLayout.transitionToStart();
    }


    private void animateForwardToFirePosts() {
        unlockPostList();
//        LockFireList();
        bind.motionLayout.transitionToEnd();
    }


}