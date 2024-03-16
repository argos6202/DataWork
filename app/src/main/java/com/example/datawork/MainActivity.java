package com.example.datawork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.datawork.util.Mensaje;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=Lima&appid=301d7dc2188e2532dad24881f5e0e283&units=metric\"c";

    FirebaseAuth mAuth;
    private int backPressCount = 0;
    private Mensaje ms;
    private ImageView logo;
    private TextView textViewTemperatura;
    private TextView txtDataWorks, txtiniciar;
    private TextInputEditText txtUsuario, txtContraseña;
    private Button btnLogin, btnRegistrar;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        logo = (ImageView) findViewById(R.id.logo);
        textViewTemperatura = findViewById(R.id.textViewTemperatura);
        txtDataWorks = (TextView) findViewById(R.id.txtDataWorks);
        txtiniciar = (TextView) findViewById(R.id.txtiniciar);
        txtUsuario = (TextInputEditText) findViewById(R.id.txtUsuario);
        txtContraseña = (TextInputEditText) findViewById(R.id.txtcontraseña);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        requestQueue = Volley.newRequestQueue(this);

        obtenerTemperatura();
        validacionOnTime();
    }

    private void obtenerTemperatura() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request
                .Method.GET, API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtiene la matriz "weather" del objeto JSON de respuesta
                            JSONArray weatherArray = response.getJSONArray("weather");

                            // Obtiene el primer objeto JSON de la matriz "weather"
                            JSONObject weatherObject = weatherArray.getJSONObject(0);

                            // Obtiene la descripción del clima del objeto "weather"
                            String descripcionClima = weatherObject.getString("description");

                            // Obtiene la temperatura en Kelvin del objeto "main"
                            JSONObject mainObject = response.getJSONObject("main");
                            double temperaturaKelvin = mainObject.getDouble("temp");

                            // Convierte la temperatura de Kelvin a Celsius
                            double temperaturaCelsius = temperaturaKelvin - 273.15;

                            // Muestra la temperatura en Celsius en el TextView
                            textViewTemperatura.setText("Clima hoy: " +descripcionClima + String.format("\n%.1f °C", temperaturaCelsius));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        // Agrega la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
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

        ms = new Mensaje(this);

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
        ms.MProgressBarDato();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this,Frm_Menu.class));
                            ms.MCloseProgBar(true);
                            finish();
                        } else {
                            ms.MCloseProgBar(true);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed." + email + password,
                                    Toast.LENGTH_SHORT).show();
                            txtUsuario.setText("");
                            txtContraseña.setText("");
                        }
                    }
                });

    }
    private void validacionOnTime() {
        txtUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se utiliza en esta implementación
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se utiliza en esta implementación
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validar el correo electrónico en tiempo real
                if (!isValidEmail(s.toString())) {
                    txtUsuario.setError("Correo electrónico inválido");
                } else {
                    txtUsuario.setError(null);
                }
            }
        });

        txtContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se utiliza en esta implementación
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se utiliza en esta implementación
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validar la contraseña en tiempo real
                if (!isValidPassword(s.toString())) {
                    txtContraseña.setError("Contraseña inválida");
                } else {
                    txtContraseña.setError(null);
                }
            }
        });
    }

    // Método para validar un correo electrónico
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para validar una contraseña
    private boolean isValidPassword(String password) {
        return password.length() >= 6; //mínimo 6 caracteres
    }
    public void OnClick_btnRegistrarse(View v){
        startActivity(new Intent(MainActivity.this,Frm_Registro.class));
    }
    public void OnClick_IniciarSesion(View view){
        startActivity(new Intent(MainActivity.this,Frm_Menu.class));
    }

}