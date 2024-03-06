package com.example.datawork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private int backPressCount = 0;
    private ImageView logo;
    private TextView txtDataWorks, txtiniciar;
    private EditText txtUsuario, txtContraseña;
    private Button btnLogin, btnRegistrar;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logo = (ImageView) findViewById(R.id.logo);
        txtDataWorks = (TextView) findViewById(R.id.txtDataWorks);
        txtiniciar = (TextView) findViewById(R.id.txtiniciar);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContraseña = (EditText) findViewById(R.id.txtcontraseña);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPressCount++;

            if (backPressCount == 3) {
                finishAffinity();
                System.exit(0);
            } else {
                Toast.makeText(this, "Presione atrás nuevamente para salir de la aplicación", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void OnClick_btnEntrar(View v){

        String email, password;
        email = txtUsuario.getText().toString();
        password = txtContraseña.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.this,"Ingresa tu usuario",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this,"Ingresa tu contraseña",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this,Frm_Menu.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed." + email + password,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void OnClick_btnRegistrarse(View v){
        startActivity(new Intent(MainActivity.this,Frm_Registro.class));
    }
    public void OnClick_IniciarSesion(View view){
        startActivity(new Intent(MainActivity.this,Frm_Menu.class));
    }

}