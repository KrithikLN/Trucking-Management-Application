package com.example.truckingappmanager;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.truckingappmanager.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String[] name = {"Krithik", "Naveen", "Rohith", "Selvakumar"};
    private ArrayAdapter<String> arrayAdapter;

    private Handler handler = new Handler();
    private LocationApiService locationApiService;

    private Runnable locationUpdateTask = new Runnable() {
        @Override
        public void run() {
            // Call LocationApiService to upload location data
            locationApiService.uploadLocationData();

            // Schedule the task to run again after 5 seconds
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.truckingappmanager.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name);
        listView.setAdapter(arrayAdapter);

        // Initialize LocationApiService with the context
        locationApiService = new LocationApiService(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the location update task when the activity is resumed
        handler.postDelayed(locationUpdateTask, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the location update task when the activity is paused
        handler.removeCallbacks(locationUpdateTask);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        int currentDestinationId = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
                .getCurrentDestination().getId();

        if (currentDestinationId == R.id.navigation_home) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.isEmpty()) {
                        listView.setVisibility(View.VISIBLE);
                        findViewById(R.id.nav_host_fragment_activity_main).setVisibility(View.INVISIBLE);
                    } else {
                        listView.setVisibility(View.INVISIBLE);
                        findViewById(R.id.nav_host_fragment_activity_main).setVisibility(View.VISIBLE);
                    }
                    arrayAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        } else {
            menuItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }
}