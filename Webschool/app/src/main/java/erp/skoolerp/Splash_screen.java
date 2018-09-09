package erp.skoolerp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by shalu on 05/06/17.
 */

public class Splash_screen extends Activity
{
    Thread splashTread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(2000);

                } catch (Exception e)
                {
                } finally
                {
                    Intent intent = new Intent(Splash_screen.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }
        };
        splashTread.start();
    }
}
