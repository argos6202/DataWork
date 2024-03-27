package com.example.datawork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.util.Patterns;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.datawork.menuInicio.generarNomina;
import com.example.datawork.util.ConnectivityReceiver;
import com.example.datawork.util.Mensaje;
import com.example.datawork.util.Notificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityListener {

    private RequestQueue requestQueue;
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=Lima&appid=301d7dc2188e2532dad24881f5e0e283&units=metric\"c";
    private ConnectivityManager connectivityManager;
    private ConnectivityReceiver connectivityReceiver;
    FirebaseAuth mAuth;
    private int backPressCount = 0;
    private Notificacion nt;
    private Mensaje ms;
    private ImageView logo;
    private TextView textViewTemperatura;
    private TextView txtDataWorks, txtiniciar;
    private TextInputEditText txtUsuario, txtContraseña;
    private Button btnLogin, btnRegistrar;
    private Handler handler = new Handler(Looper.getMainLooper());
    ArrayAdapter<String> adapter;

    public MainActivity() {
    }

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

        // Registra el BroadcastReceiver
        connectivityReceiver = new ConnectivityReceiver(this);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);

        obtenerTemperatura();
        validacionOnTime();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        Spinner app;
        app = this.findViewById(R.id.appq);

        String[] datos = {"", "Yape", "BBVA", "BCP"};

        adapter = new ArrayAdapter<>(this, R.layout.simple_spinner, datos);
        adapter.setDropDownViewResource(R.layout.drop_spinner);
        app.setAdapter(adapter);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedAppName = app.getSelectedItem().toString();
                if (!selectedAppName.isEmpty()) {
                    String packageName = getPackageFromAppName(selectedAppName);
                    if (packageName != null) {
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                        if (launchIntent != null) {
                            startActivity(launchIntent);
                            Toast.makeText(getApplicationContext(), "Se encontró la app: " + packageName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo abrir la aplicación", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No se encontró la aplicación", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor selecciona una aplicación", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
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
                            textViewTemperatura.setText("Clima hoy: " + descripcionClima + String.format("\n%.1f °C", temperaturaCelsius));

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

    public void OnClick_btnEntrar(View v) {

        ms = new Mensaje(this);

        String email, password;
        email = txtUsuario.getText().toString();
        password = txtContraseña.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Ingresa tu usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Ingresa tu contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        ms.MProgressBarDato();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this, Frm_Menu.class));
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

    public void OnClick_IniciarSesion(View view) {
        startActivity(new Intent(MainActivity.this, Frm_Menu.class));
    }

    public String getPackageFromAppName(String appName) {
        PackageManager pm = this.getPackageManager();
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
    private boolean isConnected() {
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected()) {
            // Conexión a Internet disponible
            Snackbar.make(findViewById(android.R.id.content), "Conexión a Internet disponible", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.green))
                    .setTextColor(getResources().getColor(R.color.black))
                    .show();
        } else {
            // Sin conexión a Internet
            Snackbar.make(findViewById(android.R.id.content), "Sin conexión a Internet", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(this, R.color.red))
                    .setTextColor(getResources().getColor(R.color.white))
                    .setAction("Reintentar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // No es necesario hacer nada aquí ya que la conectividad se verifica automáticamente
                        }
                    }).show();
        }
    }
}