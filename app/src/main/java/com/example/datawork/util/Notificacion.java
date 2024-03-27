package com.example.datawork.util;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.datawork.R;

public class Notificacion {
    public void mostrarNotificacionApp(Context ct, String titulo, String contenido,Intent intent) {
        // Crear un canal de notificación
        createNotificationChannel(ct);
        // Crear el PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(ct, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ct, "canal")
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setSmallIcon(R.drawable.minilogo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Agregar el PendingIntent a la notificación
                .setAutoCancel(true); // Permitir que la notificación se cierre al hacer clic en ella

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ct);
        if (ActivityCompat.checkSelfPermission(ct, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private static void createNotificationChannel(Context ct){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Mi canal";
            String description = "Notificaciones de mi aplicación";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("canal", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = ct.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
