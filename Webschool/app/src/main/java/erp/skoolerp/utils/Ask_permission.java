package erp.skoolerp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * Created by shalu on 15/07/17.
 */

public class Ask_permission extends Activity {

    public static final int WRITE_EXTERNAL = 99;
    Context context;

    public Ask_permission(Context context) {

        this.context = context;
        if(!isWriteStorageAllowed())
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, WRITE_EXTERNAL);
        }
    }

    public boolean isWriteStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED )
            return true;

        //If permission is not granted returning false
        return false;
    }
}
