package com.example.truckingappmanager.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.truckingappmanager.R;

public class DashboardFragment extends Fragment {

    private ImageView salaryImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Find the ImageView by id
        salaryImageView = rootView.findViewById(R.id.SalaryImageView);

        // Set click listener for the ImageView
        salaryImageView.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, new com.example.truckingappmanager.ui.salary.SalaryFragment());
            fragmentTransaction.addToBackStack(null); // Optional: Add transaction to back stack
            fragmentTransaction.commit();
        });
        return rootView;
    }
}