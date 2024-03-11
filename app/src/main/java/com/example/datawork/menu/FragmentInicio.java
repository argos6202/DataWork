package com.example.datawork.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datawork.Frm_Registro;
import com.example.datawork.MainActivity;
import com.example.datawork.R;
public class FragmentInicio extends Fragment {
    private TextView textView;

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTextViewClickListener) {
            mListener = (OnTextViewClickListener) context;
            System.out.println("Si funciona el método nuevo");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTextViewClickListener");
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        TextView textView = root.findViewById(R.id.MnuPagosRealizados);
        /*View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        TextView pagosRealizados = root.findViewById(R.id.MnuPagosRealizados);
        pagosRealizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragmentAjustes();
                mListener.replaceFragment(fragment);
            }
        });
        return root;*/
        return root;
    }


}