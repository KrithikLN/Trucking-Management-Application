package com.example.truckingappmanager.ui.salary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.truckingappmanager.R;

public class SalaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_salary, container, false);
        return rootView;
    }

    public void onBackPressed() {
        // Navigate back to DashboardFragment without adding SalaryFragment to the back stack
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.navigation_dashboard);
    }

}
