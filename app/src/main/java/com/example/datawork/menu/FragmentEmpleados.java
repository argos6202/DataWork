package com.example.datawork.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private RecyclerView recyclerView;
    private Mensaje ms;
    DatabaseReference db;
    RVadapter adapter;
    ArrayList<User> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empleados, container, false);

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

                AlertDialog alerta = new AlertDialog.Builder(root.getContext())
                        .setTitle("Agregar")
                        .setView(view1)
                        .setPositiveButton("Agregar Empleado", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(Objects.requireNonNull(nombre.getText()).toString().isEmpty()){
                                    titlelayout.setError("Debes llenar este campo");
                                } else if (numero.getText().toString().isEmpty()){
                                    numberlayout.setError("Debes llenar este campo");
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

                AlertDialog alert = new AlertDialog.Builder(root.getContext())
                        .setTitle("Editar")
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
                                    databaseInsert.getReference().child("Users").child(user.getKey()).setValue(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                databaseInsert.getReference().child("Users").child(user.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                alert.show();
            }
        });

        recyclerView1.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    Objects.requireNonNull(user).setKey(dataSnapshot.getKey());
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}