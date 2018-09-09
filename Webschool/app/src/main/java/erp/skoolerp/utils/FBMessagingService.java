package erp.skoolerp.utils;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import erp.skoolerp.Splash_screen;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 10/07/17.
 */

public class FBMessagingService extends FirebaseMessagingService
{
    public static String TAG = "MessageService";
    Intent i;
    int notfy_id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d("Show_content...", "Data dfgdsfgsgdfgdfgdfg: " + remoteMessage.getNotification().getBody());
        System.out.println("Data_string "+remoteMessage.getData().toString());

//
            try
            {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                System.out.println("Response_____:::"+json);
               // String stat = json.getString("status");
                notfy_id = Integer.valueOf(json.getString("notificationid"));

                i = new Intent(getApplicationContext(), Splash_screen.class);
                //i.putExtra("section", stat);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Wschool.editor.putBoolean("from_notftn", true);
              //  Wschool.editor.putString("section", stat);
                Wschool.editor.apply();



               // System.out.println("stat:::"+stat);
            } catch (Exception e)
            {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        NotifyManager notifyManager = new NotifyManager(FBMessagingService.this);
        notifyManager.showNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody(), i, notfy_id);
//        }
    }
    
}
