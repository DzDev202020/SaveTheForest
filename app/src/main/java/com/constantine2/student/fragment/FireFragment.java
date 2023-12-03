package com.constantine2.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.constantine2.student.adapter.FireTabLayoutAdapter;
import com.constantine2.student.databinding.FragmentFireBinding;
import com.constantine2.student.viewModel.DashboardViewModel;

public class FireFragment extends Fragment {

    FragmentFireBinding bind;
    FireTabLayoutAdapter tabLayoutAdapter;

    DashboardViewModel viewModel;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        bind = FragmentFireBinding.inflate(inflater, container, false);

        tabLayoutAdapter = new FireTabLayoutAdapter(requireActivity().getSupportFragmentManager(), requireContext());
        bind.viewPager.setAdapter(tabLayoutAdapter);
        bind.tabLayout.setupWithViewPager(bind.viewPager);

        viewModel.getFocusState().observe(getViewLifecycleOwner(), integer -> {
            switch (integer) {
                case DashboardViewModel.TAB_IDEAL_FOCUS:
                    break;
                case DashboardViewModel.TAB_MAP_FOCUS:
                    focusMap();
                    break;
                case DashboardViewModel.TAB_LIST_FOCUS:
                    ListMap();
                    break;
            }
        });


        return bind.getRoot();
    }

    private void ListMap() {
        bind.tabLayout.selectTab(bind.tabLayout.getTabAt(1),true);
    }

    private void focusMap() {
        bind.tabLayout.selectTab(bind.tabLayout.getTabAt(0),true);
    }


}
