package com.example.datawork.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datawork.R;
import com.example.datawork.menuInicio.ejecutarPagos;
import com.example.datawork.menuInicio.generarNomina;
import com.example.datawork.menuInicio.hacerInforme;
import com.example.datawork.util.Mensaje;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentInicio extends Fragment {
    private TextView textView;
    private Mensaje ms;
    generarNomina pagos = new generarNomina();
    ejecutarPagos EjecutarPagos = new ejecutarPagos();
    hacerInforme HacerInforme = new hacerInforme();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        ms = new Mensaje(root.getContext());

        TextView generarNomina = root.findViewById(R.id.generarNomina);
        TextView ejecutarPagos = root.findViewById(R.id.ejecutarPagos);
        TextView hacerInforme = root.findViewById(R.id.hacerInforme);
        TextView verResultados = root.findViewById(R.id.verResultados);
        TextView cciTrabajadores = root.findViewById(R.id.cciTrabajadores);
        TextView numerosYape = root.findViewById(R.id.numerosYape);

        ImageView btnRegresar = root.findViewById(R.id.txtRegresar);

        ejecutarPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.reemplazarlayout);
                FrameLayout back = getActivity().findViewById(R.id.layoutRegresar);

                frameLayout.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.add(R.id.mostrarlayour, EjecutarPagos,"ejecutarPagos").commit();
            }
        });
        generarNomina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG", "Iniciarndo segundo fragmento");

                // Obtener el FrameLayout
                FrameLayout frameLayout = getActivity().findViewById(R.id.reemplazarlayout);
                FrameLayout btn = getActivity().findViewById(R.id.layoutRegresar);

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

                FrameLayout frameLayout = getActivity().findViewById(R.id.reemplazarlayout);
                FrameLayout btn = getActivity().findViewById(R.id.layoutRegresar);

                frameLayout.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                FragmentManager manager = getActivity().getSupportFragmentManager();

                Fragment fragment1 = manager.findFragmentByTag("pagosRealizados");
                Fragment fragment2 = manager.findFragmentByTag("ejecutarPagos");

                if (fragment1 != null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(fragment1).commit();
                    ms.MCloseProgBar(true);
                } else {

                    ms.MCloseProgBar(true);
                    Log.d("TAG", "Fragmento no encontrado con el tag especificado");

                }

                if (fragment2 != null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(fragment2).commit();
                    ms.MCloseProgBar(true);
                } else {

                    ms.MCloseProgBar(true);
                    Log.d("TAG", "Fragmento no encontrado con el tag especificado");

                }
            }
        });
        return root;
    }
}