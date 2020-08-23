package com.example.pavelplakhotny_c196;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pavelplakhotny_c196.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BottomNavigationView navView = activityMainBinding.navView;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_terms, R.id.navigation_courses, R.id.navigation_assessment)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_app_logo);
        getWindow().setStatusBarColor(Color.BLACK);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name_course = "CourseNotificationChannel";
            String description_course = "Channel for setting course start and end date alerts";
            CharSequence name_assessment = "AssessmentNotificationChannel";
            String description_assessment = "Channel for setting assessment start and end date alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel1 = new NotificationChannel("courseAlert", name_course, importance);
            channel1.setDescription(description_course);
            NotificationChannel channel2 = new NotificationChannel("assessmentAlert", name_assessment, importance);
            channel2.setDescription(description_assessment);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}