package com.example.datawork.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.datawork.R;
import com.example.datawork.adapter.RVadapter;
import com.example.datawork.model.User;
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

import java.util.ArrayList;
import java.util.Objects;

public class FragmentEmpleados extends Fragment {
    private Mensaje ms;
    DatabaseReference db;
    RVadapter adapter;
    ArrayList<User> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empleados, container, false);

        //INICIAIZAS EL MENSAJE DE CARGA AQUÍ PARA EL LLENADO DEL RECYCLERVIEW
        //TAMBIÉN DEBE DECLARARSE COMO VARIABLE EN EL INICIO
        ms = new Mensaje(root.getContext());

        FirebaseApp.initializeApp(root.getContext());
        FirebaseDatabase databaseInsert = FirebaseDatabase.getInstance();

        FloatingActionButton add = root.findViewById(R.id.addEmpleados);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = LayoutInflater.from(root.getContext())
                        .inflate(R.layout.agregar_empleado_dialog, null);

                ms = new Mensaje(root.getContext());

                TextInputLayout titlelayout, numberlayout;
                titlelayout = view1.findViewById(R.id.titlelayout);
                numberlayout = view1.findViewById(R.id.numberlayout);

                TextInputEditText nombre, numero;
                nombre = view1.findViewById(R.id.addNombreEmpleado);
                numero = view1.findViewById(R.id.addNumeroEmpleado);

                //TITULO DEL DIALOGO PERSONALIZADO
                SpannableString Agregar = new SpannableString("Agregar");
                Agregar.setSpan(new ForegroundColorSpan(Color.BLACK), 0, Agregar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                AlertDialog alerta = new AlertDialog.Builder(root.getContext())
                        .setTitle(Agregar)
                        .setView(view1)
                        .setPositiveButton("Agregar Empleado", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(Objects.requireNonNull(nombre.getText()).toString().isEmpty()){
                                    titlelayout.setError("Debes llenar este campo");
                                } else if (numero.getText().toString().isEmpty()){
                                    numberlayout.setError("Debes llenar este campo");
                                } else if (!numero.getText().toString().startsWith("9")) {
                                    numberlayout.setError("El número debe comenzar con 9");
                                } else if (numero.getText().toString().length() != 9) {
                                    numberlayout.setError("El número debe tener 9 dígitos");
                                } else {
                                    ms.MProgressBarDato();
                                    User user = new User();
                                    user.setNombre(nombre.getText().toString());
                                    user.setNumero(numero.getText().toString());
                                    databaseInsert.getReference().child("Users").push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            ms.MCloseProgBar(true);
                                            Toast.makeText(root.getContext(), "Se guardó con éxito la información", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            ms.MCloseProgBar(true);
                                            Toast.makeText(root.getContext(), "Hubo un error en guardar la información", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                alerta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                //VALIDACIÓN CARACTERES
                numero.addTextChangedListener(new TextWatcher() {
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
                        if (!numeroIngresado.startsWith("9")) {
                            numberlayout.setError("El número debe comenzar con 9XX XXX XXX");
                        } else if (numeroIngresado.length() != 9) {
                            numberlayout.setError("El número debe tener 9 dígitos");
                        } else {
                            numberlayout.setError(null);
                        }
                    }
                });

                alerta.show();
            }
        });

        RecyclerView recyclerView1 = root.findViewById(R.id.vEmpleados);
        db = FirebaseDatabase.getInstance().getReference("Users");

        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(root.getContext()));

        list = new ArrayList<>();
        adapter = new RVadapter(root.getContext(),list);

        adapter.setOnItemClickListener(new RVadapter.OnItemClickListener() {
            @Override
            public void onClick(User user) {
                View view = LayoutInflater.from(root.getContext()).inflate(R.layout.agregar_empleado_dialog,null);

                TextInputLayout titlelayout, numberlayout;
                titlelayout = view.findViewById(R.id.titlelayout);
                numberlayout = view.findViewById(R.id.numberlayout);

                TextInputEditText nombre, numero;
                nombre = view.findViewById(R.id.addNombreEmpleado);
                numero = view.findViewById(R.id.addNumeroEmpleado);

                nombre.setText(user.getNombre());
                numero.setText(user.getNumero());

                ms = new Mensaje(root.getContext());

                //TEXTO PERSONALIZADO TITULO DIALOGO
                SpannableString Editar = new SpannableString("EDITAR");
                Editar.setSpan(new ForegroundColorSpan(Color.BLACK), 0, Editar.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                AlertDialog alert = new AlertDialog.Builder(root.getContext())
                        .setTitle(Editar)
                        .setView(view)
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(Objects.requireNonNull(nombre.getText()).toString().isEmpty()){
                                    titlelayout.setError("Debes llenar este campo");
                                } else if (numero.getText().toString().isEmpty()){
                                    numberlayout.setError("Debes llenar este campo");
                                } else {
                                    ms.MProgressBarDato();
                                    User user1 = new User();
                                    user1.setNombre(nombre.getText().toString());
                                    user1.setNumero(numero.getText().toString());
                                    databaseInsert.getReference().child("Users")
                                            .child(user.getKey())
                                            .setValue(user1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            ms.MCloseProgBar(true);
                                            Toast.makeText(root.getContext(), "Se guardó con éxito la información", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            ms.MCloseProgBar(true);
                                            Toast.makeText(root.getContext(), "Hubo un error en guardar la información", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ms.MProgressBarDato();
                                databaseInsert.getReference()
                                        .child("Users")
                                        .child(user.getKey())
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ms.MCloseProgBar(true);
                                        Toast.makeText(root.getContext(), "Se borró el contacto ", Toast.LENGTH_SHORT).show();
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

                numero.addTextChangedListener(new TextWatcher() {
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
                        if (!numeroIngresado.startsWith("9")) {
                            numberlayout.setError("El número debe comenzar con 9XX XXX XXX");
                        } else if (numeroIngresado.length() != 9) {
                            numberlayout.setError("El número debe tener 9 dígitos");
                        } else {
                            numberlayout.setError(null);
                        }
                    }
                });
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                alert.show();
            }
        });

        recyclerView1.setAdapter(adapter);

        //COMIENZA EL DIALOGO DE CARGA HASTA QUE SE LLENE EL RECYCLER
        ms.MProgressBarDato();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    Objects.requireNonNull(user).setKey(dataSnapshot.getKey());
                    list.add(user);
                }
                //CIERRA DIALOGO DE CARGA
                ms.MCloseProgBar(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ms.MCloseProgBar(true);
            }
        });

        return root;
    }
}