package virtual_coin_checker.sincdor.coinchecker.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by andre on 27-11-2017.
 */

public class MyRefreshFireBase extends FirebaseInstanceIdService {

    public MyRefreshFireBase() {
        Log.e(TAG, "MyService: Serviço criado!");
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
