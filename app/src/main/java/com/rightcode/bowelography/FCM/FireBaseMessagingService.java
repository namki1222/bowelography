package com.rightcode.bowelography.FCM;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.MainActivity;
import com.rightcode.bowelography.util.Log;

import java.util.Map;

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d("Message data payload: " + remoteMessage.getData());
            if (true) {
            } else {
                handleNow();
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }
//        if (remoteMessage.getData() != null) {
//            sendNotification();
//        }
    }

    private void handleNow() {
        Log.d("Short lived task is done.");
    }


    private void sendNotification(String messageBody, Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);

        if (data != null) {
            try {
                if (data.get("feedId") != null) {
                    int feedId = Integer.parseInt(data.get("feedId"));
                    intent.putExtra("feedId", feedId);
                }
            } catch (NullPointerException e) {
                Log.e(e);
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}



