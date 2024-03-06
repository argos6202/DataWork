package com.example.datawork.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.datawork.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAjustes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAjustes extends Fragment {
    private Context ct;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);
        return view;
    }


    public void onTextViewClick() {
        // Código a ejecutar al hacer clic en el TextView
    }

    public void getToast(String men){
        Toast.makeText(ct,men,Toast.LENGTH_SHORT).show();
    }
}