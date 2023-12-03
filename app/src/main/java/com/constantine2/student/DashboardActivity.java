package com.constantine2.student;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.constantine2.student.databinding.ActivityDashboardBinding;
import com.constantine2.student.databinding.DashboardAppBarBinding;
import com.constantine2.student.databinding.DashboardMainContentBinding;
import com.constantine2.student.databinding.NavHeaderMainBinding;
import com.constantine2.student.model.Client;
import com.constantine2.student.viewModel.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity {


    ActivityDashboardBinding drawerBind;
    DashboardAppBarBinding appBarBind;
    DashboardMainContentBinding contentBind;
    NavHeaderMainBinding navHeaderMainBinding;


    ActionBarDrawerToggle actionBarDrawerToggle;
    AppBarConfiguration mAppBarConfiguration;

    DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        Bundle b = getIntent().getExtras();
//        Client c = (Client) savedInstanceState.getSerializable("client");
        Client c = (Client) b.getSerializable("client");
        if (c != null)
            viewModel.setClient(c);
        else
            Log.e("TAG", "onCreate: Dashboard: client==null" );


        drawerBind = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(drawerBind.getRoot());

        appBarBind = DashboardAppBarBinding.bind(findViewById(R.id.app_bar_container));
        contentBind = DashboardMainContentBinding.bind(findViewById(R.id.dashboard_main_content));

        setSupportActionBar(appBarBind.toolbar);


//        appBarBind.toolbar.setNavigationOnClickListener(v -> drawerBind.drawerLayout.open());


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerBind.drawerLayout, appBarBind.toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerBind.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.fire, R.id.friends, R.id.settings, R.id.disconnect)
                .setOpenableLayout(drawerBind.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(drawerBind.navView, navController);


        navHeaderMainBinding = NavHeaderMainBinding.inflate(getLayoutInflater(), drawerBind.navView, false);

        if (c != null) {
            String s = c.getFirstName() + " " + c.getLastName();
            navHeaderMainBinding.userName.setText(s);
            navHeaderMainBinding.userEmail.setText(c.getEmail());
        }

        drawerBind.navView.addHeaderView(navHeaderMainBinding.getRoot());


        //TODO disconnect here
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}