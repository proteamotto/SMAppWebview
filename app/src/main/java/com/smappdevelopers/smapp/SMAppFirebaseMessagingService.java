package com.smappdevelopers.smapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Catt on 23/2/2018.
 */

public class SMAppFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "SMAPPTAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG, "Mensaje recibido de: "+ from);

        if (remoteMessage.getNotification() != null){
            Log.d(TAG, "Notificación: "+ remoteMessage.getNotification().getBody());

            mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: "+ remoteMessage.getData());
        }
    }

    private void mostrarNotificacion(String title, String body, Map<String,String> data) {

        String smappurl = "";

        Intent intent = new Intent(this, HomeActivity.class);

        for (String key : data.keySet()) {
            String value = data.get(key);
            //Log.d(TAG, "Key: "+ key +", Value: "+ value);
            if (key.equals("smappurl")) {
                smappurl = value;
            }
        }

        if (smappurl.length() > 0)
            {
                intent.putExtra("smappurl", data.get("smappurl"));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder notificationBuilder;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Oreo
                    String chanel_id = "3000";
                    CharSequence name = "SMApp";
                    String description = "SMApp";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.BLUE);
                    notificationManager.createNotificationChannel(mChannel);
                    notificationBuilder = new NotificationCompat.Builder(this, chanel_id);
                } else {
                    notificationBuilder = new NotificationCompat.Builder(this);
                }

                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.splash)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                if (Build.VERSION.SDK_INT >= 21)
                    notificationBuilder.setVibrate(new long[0]);


                notificationManager.notify(0, notificationBuilder.build());
            }
    }
}
