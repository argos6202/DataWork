package com.example.datawork.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datawork.R;
import com.example.datawork.adapter.RVPadapter;
import com.example.datawork.model.Pagos;
import com.example.datawork.util.Mensaje;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FragmentPagos extends Fragment {
    private Mensaje ms;
    private Context ct;
    private static final String PACKAGE_NAME = "com.bcp.innovacxion.yapeapp";
    Workbook workbook = new HSSFWorkbook();
    DatabaseReference databaseReference;
    RVPadapter adaptadorPagos;
    ArrayList<Pagos> listaPagos;
    ArrayAdapter<String> adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ct = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View raiz = inflater.inflate(R.layout.fragment_pagos, container, false);


        FirebaseApp.initializeApp(raiz.getContext());
        FirebaseDatabase databaseInsertar = FirebaseDatabase.getInstance();

        FloatingActionButton agregar = raiz.findViewById(R.id.AgregarPago);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View raiz1 = LayoutInflater.from(raiz.getContext())
                        .inflate(R.layout.agregar_pago_dialog,null);

                ms = new Mensaje(raiz.getContext());
                PackageManager pm = ct.getPackageManager();

                Spinner app;
                app = raiz1.findViewById(R.id.app);

                String[] datos = {"⇊⇊⇊⇊","Yape", "BBVA", "BCP"};

                adapter = new ArrayAdapter<>(ct, R.layout.simple_spinner, datos);
                adapter.setDropDownViewResource(R.layout.drop_spinner);
                app.setAdapter(adapter);

                TextInputLayout nombreLayout, pagoLayout;
                nombreLayout = raiz1.findViewById(R.id.titulolayout);
                pagoLayout = raiz1.findViewById(R.id.pagoLayout);

                TextInputEditText name, pay;
                name = raiz1.findViewById(R.id.NombreEmpleado);
                pay = raiz1.findViewById(R.id.PagoEmpleado);


                SpannableString agregar = new SpannableString("Agregar");
                agregar.setSpan(new ForegroundColorSpan(Color.BLACK),0,agregar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                AlertDialog agregarEmpleado = new AlertDialog.Builder(raiz.getContext())
                        .setTitle(agregar)
                        .setView(raiz1)
                        .setPositiveButton("Agregar Registro", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //CAPTURAR NOMBRE DEL SPINNER Y REDIRECCIONAR A LA APP
                                String selectedAppName = app.getSelectedItem().toString();
                                if (!selectedAppName.isEmpty()) {
                                    String packageName = getPackageFromAppName(selectedAppName);
                                    if (packageName != null) {
                                        Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
                                        SpannableString confirmar = new SpannableString("Confirmar transferencia");
                                        agregar.setSpan(new ForegroundColorSpan(Color.BLACK),0,agregar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        SpannableString realizo = new SpannableString("¿Ya realizó el pago?");
                                        agregar.setSpan(new ForegroundColorSpan(Color.BLACK),0,agregar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        //PREGUNTA AL USUARIO SI YA HIZO LA TRANSFERENCIA PARA MANDARLO A OTRA APLICACIÓN
                                        AlertDialog pregunta = new AlertDialog.Builder(raiz.getContext())
                                                .setTitle(confirmar)
                                                .setMessage(realizo)
                                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        ms.MProgressBarDato();

                                                        String payString = pay.getText().toString();
                                                        double payValue;

                                                        try {
                                                            // Attempt to convert the String to a double
                                                            payValue = Double.parseDouble(payString);
                                                        } catch (NumberFormatException e) {
                                                            // Handle the case where the input is not a valid number
                                                            pagoLayout.setError("Formato de pago inválido");
                                                            return;
                                                        }
                                                        Pagos pagos = new Pagos();
                                                        //Falta agregar método de pago aquí

                                                        pagos.setNombre(name.getText().toString());
                                                        pagos.setPago(payValue);

                                                        databaseInsertar.getReference().child("Pagos")
                                                                .push().setValue(pagos)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        ms.MCloseProgBar(true);
                                                                    }
                                                                });
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if (launchIntent != null) {
                                                            startActivity(launchIntent);
                                                        } else {
                                                            Toast.makeText(ct, "No se pudo abrir la aplicación", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                })
                                                .create();
                                        pregunta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                                        pregunta.show();
                                    } else {
                                        Toast.makeText(ct, "No se encontró la aplicación", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ct, "Por favor selecciona una aplicación", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                agregarEmpleado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                agregarEmpleado.show();
            }
        });

        RecyclerView rv = raiz.findViewById(R.id.vPagos);
        databaseReference = FirebaseDatabase.getInstance().getReference("Pagos");

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(raiz.getContext()));

        listaPagos = new ArrayList<>();
        adaptadorPagos = new RVPadapter(raiz.getContext(), listaPagos);

        adaptadorPagos.setClickListener(new RVPadapter.ClickListener() {
            @Override
            public void OnClick(Pagos pagos) {
                View view = LayoutInflater.from(raiz.getContext())
                        .inflate(R.layout.agregar_pago_dialog,null);

                TextInputLayout nombreLayout, pagoLayout;
                nombreLayout = view.findViewById(R.id.titulolayout);
                pagoLayout = view.findViewById(R.id.pagoLayout);

                TextInputEditText name, pay;
                name = view.findViewById(R.id.NombreEmpleado);
                pay = view.findViewById(R.id.PagoEmpleado);

                name.setText(pagos.getNombre());

                double pago = pagos.getPago();
                if (!Double.isNaN(pago)) { // Check for invalid double values (e.g., NaN)
                    String pagoString = String.valueOf(pago);
                    pay.setText(pagoString);
                } else {
                    // Handle invalid double value appropriately, e.g., set a default text or display an error message
                    pay.setText("Invalido else onclick adaptador"); // Example: Clear the EditText if the value is invalid
                }

                ms = new Mensaje(raiz.getContext());

                SpannableString editar = new SpannableString("Editar");
                editar.setSpan(new ForegroundColorSpan(Color.BLACK),0, editar.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                AlertDialog editarEmpleado = new AlertDialog.Builder(raiz.getContext())
                        .setTitle(editar)
                        .setView(view)
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(name.getText()).toString().isEmpty()){
                                    nombreLayout.setError("Llena este campo por favor");
                                } else if (pay.getText().toString().isEmpty()){
                                    pagoLayout.setError("Llena este campo por favor");
                                } else {
                                    ms.MProgressBarDato();
                                    Pagos pagos1 = new Pagos();
                                    pagos1.setNombre(name.getText().toString());
                                    pagos1.setPago(getDoubleValue(pay.getText().toString()));

                                    databaseInsertar.getReference().child("Pagos")
                                            .child(pagos.getKey())
                                            .setValue(pagos1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    ms.MCloseProgBar(true);
                                                    Toast.makeText(raiz.getContext(), "El pago se guardó con éxito", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    ms.MCloseProgBar(true);
                                                    Toast.makeText(raiz.getContext(), "Hubo un error en guardar la información", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            }
                        })
                        .setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ms.MProgressBarDato();

                                databaseInsertar.getReference()
                                        .child("Pagos")
                                        .child(pagos.getKey())
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                ms.MCloseProgBar(true);
                                                Toast.makeText(raiz.getContext(), "Se borró el registro ", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                ms.MCloseProgBar(true);
                                            }
                                        });
                            }
                        }).create();
                editarEmpleado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                pay.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // No se necesita implementar este método
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // No se necesita implementar este método
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String numeroIngresado = s.toString();
                        if (numeroIngresado.isEmpty()){
                            pagoLayout.setError("Llena este campo por favor");
                        } else {
                            pagoLayout.setError(null);
                        }
                    }
                });
                editarEmpleado.show();
            }
        });

        rv.setAdapter(adaptadorPagos);

        //CAMBIO DE TEXTO CUANDO SE MANTENGA PRESIONADO
        TextView contador, borrar;
        contador = raiz.findViewById(R.id.contador);
        borrar = raiz.findViewById(R.id.btnImprimir);
        //MÉTODOS EN EL ADAPTADOR PARA CAMBIAR TEXTO
        adaptadorPagos.setSelectionTextView(contador);
        adaptadorPagos.setSelectionTextView1(borrar);

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ms = new Mensaje(raiz.getContext());
                ms.MProgressBarDato();
                // Obtén los elementos seleccionados del adaptador
                Set<Integer> selectedItems = adaptadorPagos.getSelectedItems();
                for (Integer pos : selectedItems){
                    Pagos pagos = listaPagos.get(pos);
                    databaseInsertar.getReference()
                            .child("Pagos")
                            .child(pagos.getKey())
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    ms.MCloseProgBar(true);
                                    borrar.setText("Imprimir");
                                    contador.setText("Registro de pagos");
                                    Toast.makeText(raiz.getContext(), "Se borró el registro ", Toast.LENGTH_SHORT).show();
                                    // Una vez que se borra el elemento, quítalo de la lista de elementos seleccionados
                                    adaptadorPagos.getSelectedItems().remove(pos);
                                    // Notifica al adaptador que los datos han cambiado
                                    adaptadorPagos.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPagos.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Pagos pagos = dataSnapshot.getValue(Pagos.class);
                    Objects.requireNonNull(pagos).setKey(dataSnapshot.getKey());
                    listaPagos.add(pagos);
                }
                adaptadorPagos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return raiz;
    }

    private double getDoubleValue(String payString) {
        View view = LayoutInflater.from(this.getContext())
                .inflate(R.layout.agregar_pago_dialog,null);
        TextInputLayout pagoLayout;
        pagoLayout = view.findViewById(R.id.pagoLayout);
        try {
            return Double.parseDouble(payString);
        } catch (NumberFormatException e) {
            // Handle invalid input (not a valid double)
            pagoLayout.setError("Formato de pago inválido");
            return Double.NaN; // Or a suitable default value
        }
    }

    public String getPackageFromAppName(String appName) {
        PackageManager pm = ct.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : apps) {
            if (info.loadLabel(pm).toString().equalsIgnoreCase(appName)) {
                return info.activityInfo.packageName;
            }
        }
        return null;
    }

    public void imprimirPagos(){
        Workbook workbook = new HSSFWorkbook();

        Cell cell = null;
        CellStyle cellStyle1= workbook.createCellStyle();
        cellStyle1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
        //cellStyle1.setFillPattern();
    }
}