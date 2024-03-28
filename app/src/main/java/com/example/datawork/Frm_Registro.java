package com.example.datawork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.datawork.util.Mensaje;
import com.example.datawork.util.snackbarutil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Frm_Registro extends AppCompatActivity {
    private Mensaje ms;
    Button btnrecuperar;
    EditText emailrecuperar;
    private snackbarutil snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_registro);

        btnrecuperar = findViewById(R.id.btnRecuperar);
        emailrecuperar = findViewById(R.id.emailRecuperar);

        btnrecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snack.showSnackbar(view, "Click");
                validate();
            }
        });
    }

    public void validate(){
        String email = emailrecuperar.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailrecuperar.setError("Correo Inválido");
        }
        enviarEmail(email);
    }
    public void enviarEmail(String email){
        ms = new Mensaje(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String correo = email;
        ms.MProgressBarDato();

        auth.sendPasswordResetEmail(correo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            View view = findViewById(android.R.id.content);
                            snackbarutil snack = new snackbarutil();
                            snack.showSnackbarWithActionOk(getApplicationContext(), view, "Correo enviado", "Hecho", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Frm_Registro.this, MainActivity.class));
                                }
                            });
                            ms.MCloseProgBar(true);
                        } else {
                            View view = findViewById(android.R.id.content);
                            snack.showSnackbarNo(view,"Correo inválido", getApplicationContext());
                            ms.MCloseProgBar(true);
                        }
                    }
                });
    }

}