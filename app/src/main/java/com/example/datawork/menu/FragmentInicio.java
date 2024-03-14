package com.example.datawork.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datawork.Frm_Registro;
import com.example.datawork.MainActivity;
import com.example.datawork.R;
import com.example.datawork.menuInicio.BlankFragment;
import com.example.datawork.menuInicio.pagosRealizados;
import com.example.datawork.util.Mensaje;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentInicio extends Fragment {
    private TextView textView;
    private Mensaje ms;
    pagosRealizados pagos = new pagosRealizados();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        ms = new Mensaje(root.getContext());
        TextView pagosRealizados = root.findViewById(R.id.MnuPagosRealizados);
        FloatingActionButton btnRegresar = root.findViewById(R.id.floatingfRegresar);
        pagosRealizados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "Iniciarndo segundo fragmento");

                // Obtener el FrameLayout
                FrameLayout frameLayout = getActivity().findViewById(R.id.reemplazarlayout);
                FrameLayout btn = getActivity().findViewById(R.id.btnRegresar);

                // Ocultar el FrameLayout
                frameLayout.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.add(R.id.mostrarlayour, pagos,"pagosRealizados");

                transaction.commit();

                Log.e("TAG", "Terminó segundo fragmento");
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ms.MProgressBarDato();

                // Obtener el FrameLayout
                FrameLayout frameLayout = getActivity().findViewById(R.id.reemplazarlayout);
                FrameLayout btn = getActivity().findViewById(R.id.btnRegresar);

                // Ocultar el FrameLayout
                frameLayout.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                Fragment fragment = manager.findFragmentByTag("pagosRealizados");

                if (fragment != null) {
                    // Remover el fragmento
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(fragment).commit();
                    ms.MCloseProgBar(true);
                } else {
                    ms.MCloseProgBar(true);
                    // El fragmento no fue encontrado
                    Log.d("TAG", "Fragmento no encontrado con el tag especificado");
                }
            }
        });
        return root;
    }
}