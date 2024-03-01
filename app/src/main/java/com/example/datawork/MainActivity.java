package com.example.datawork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class MainActivity extends AppCompatActivity {
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
        // Implement login logic here (e.g., validate credentials, handle errors)
        logo = (ImageView) findViewById(R.id.logo);
        txtDataWorks = (TextView) findViewById(R.id.txtDataWorks);
        txtiniciar = (TextView) findViewById(R.id.txtiniciar);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContraseña = (EditText) findViewById(R.id.txtcontraseña);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        // Assuming successful login, initiate animation and activity transition
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);

        // Apply animation to relevant views
        logo.startAnimation(animation);
        txtDataWorks.startAnimation(animation);
        txtiniciar.startAnimation(animation);
        txtUsuario.startAnimation(animation);
        txtContraseña.startAnimation(animation);
        btnLogin.startAnimation(animation);
        btnRegistrar.startAnimation(animation);

        // Start a new thread for the activity transition after animation finishes
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Frm_Menu.class);
                startActivity(intent);
                finish(); // Optionally close this activity to prevent accidental back navigation
            }
        }, animation.getDuration()); // Delay based on animation duration

    }
    public void OnClick_btnRegistrarse(View v){
        startActivity(new Intent(MainActivity.this,Frm_Registro.class));
    }

}