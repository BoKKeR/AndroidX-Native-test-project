package com.example.otto.ui.employees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.otto.R;

public class EmployeesFragment extends Fragment {

    private EmployeesViewModel employeesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        employeesViewModel =
                ViewModelProviders.of(this).get(EmployeesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_employees, container, false);
        final TextView textView = root.findViewById(R.id.text_employees);
        employeesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}