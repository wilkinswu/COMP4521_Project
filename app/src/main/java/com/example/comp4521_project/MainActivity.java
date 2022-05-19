package com.example.comp4521_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.comp4521_project.ui.dashboard.DashboardFragment;
import com.example.comp4521_project.ui.home.HomeFragment;
import com.example.comp4521_project.ui.personal.PersonalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.comp4521_project.databinding.ActivityMainBinding;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    final DashboardFragment fragment2 = new DashboardFragment();
    final PersonalFragment fragment3 = new PersonalFragment();
    final HomeFragment fragment1 = new HomeFragment(this, fragment2);
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ViewPager viewPager = findViewById(R.id.container);
//        viewPager.setOffscreenPageLimit(2);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container,fragment1, "1").commit();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home,
//                R.id.navigation_dashboard,
//                R.id.navigation_personal)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    clickHome();
                    return true;
                case R.id.navigation_dashboard:
                    clickDashboard();
                    return true;
                case R.id.navigation_personal:
                    clickPersonal();
                    return true;
            }
            return false;
        }
    };

    public void clickHome() {
        fm.beginTransaction().hide(active).show(fragment1).commit();
        active = fragment1;
    }

    public void clickDashboard() {
        fm.beginTransaction().hide(active).show(fragment2).commit();
        active = fragment2;
    }

    public void clickPersonal() {
        fm.beginTransaction().hide(active).show(fragment3).commit();
        active = fragment3;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }


}