package com.example.datawork.util;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.datawork.R;
import com.google.android.material.snackbar.Snackbar;

public class snackbarutil {
    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        //SnackbarUtils.showSnackbar(view, "Mensaje de texto");

    }
    public static void showSnackbarOk(View view, String message, Context context) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.green))
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .show();
        //SnackbarUtils.showSnackbarOk(view, "Mensaje de texto", getContext()); // Para fragmentos
        //SnackbarUtils.showSnackbarOk(view, "Mensaje de texto", this); // Para actividades
    }

    public static void showSnackbarWithAction(View view, String message, String actionText, View.OnClickListener listener) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(actionText, listener)
                .show();
        /*SnackbarUtils.showSnackbarWithAction(view, "Mensaje de texto con acción", "Reintentar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción al hacer clic en el botón de acción
            }
        });*/

    }
}
