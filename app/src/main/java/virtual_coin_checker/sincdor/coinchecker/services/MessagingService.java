package virtual_coin_checker.sincdor.coinchecker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import virtual_coin_checker.sincdor.coinchecker.MainActivity;
import virtual_coin_checker.sincdor.coinchecker.R;

import static android.content.ContentValues.TAG;

/**
 * Created by andre on 27-11-2017.
 */

public class MessagingService extends FirebaseMessagingService {

    public MessagingService() {
        Log.e(TAG, "onCreate: Serviço Mensagem criado");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: MessaginService created!");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getFrom() != null &&
                remoteMessage.getFrom().startsWith("/topics/") &&
                remoteMessage.getData() != null
                ) { //Se a mensagem for do tipo Topico

            String title = remoteMessage.getData().get("title");
            String coin = remoteMessage.getData().get("coin");
            String price = remoteMessage.getData().get("price");
            String percentage = remoteMessage.getData().get("percentage");

            HashMap<String, String> information = new HashMap<>();
            information.put("title", title);
            information.put("coin", coin);
            information.put("price", price);
            information.put("percentage", percentage);

            createNotification(information);

        }
    }

    private void createNotification(HashMap<String, String> information) {

        Intent intent = new Intent(this, MainActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle(information.get("title"))
                .setContentText(information.get("coin") + " " + information.get("price") + " " + information.get("percentage"))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(notificationManager != null)
            notificationManager.notify(0, n);
    }

}
