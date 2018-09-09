package erp.skoolerp.utils;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import erp.skoolerp.Wschool;

/**
 * Created by shalu on 10/07/17.
 */

public class FBInstanceIDservice extends FirebaseInstanceIdService
{
    public static final String Token_broadcast = "token_recieved";

    @Override
    public void onTokenRefresh()
    {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d("Firebasetoken", "Refreshed token: " + refreshedToken);

        //calling the method store token and passing token
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {

        Wschool.editor.putString("regid", token);
        Wschool.editor.commit();

        getApplicationContext().sendBroadcast(new Intent(Token_broadcast));
    }
}
