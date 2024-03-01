package com.example.datawork.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datawork.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEmpleados#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEmpleados extends Fragment {

    public FragmentEmpleados() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empleados, container, false);

    }
}