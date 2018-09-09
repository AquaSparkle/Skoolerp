package erp.skoolerp;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

/**
 * Created by shalu on 07/06/17.
 */

public class Wschool extends Application
{
    String fontPath1 = "fonts/Montserrat_Regular.ttf";
    String fontPath2 = "fonts/Montserrat_Medium.ttf";
    String fontPath3 = "fonts/Montserrat_SemiBold.ttf";
    public static Typeface tf1;
    public static Typeface tf2;
    public static Typeface tf3;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String wschool_shared = "wschool";
    public static String base_URL, api_link, req_link, salt, title1, title2, title3, title4;
    public static String name, email, phone, city, zipcode;
    public static boolean from_main = false;
    public static boolean from_pay = false;
    public static boolean employee_log = false;
    public static int prev_frag;
    public static ArrayList<String> month, year;

    @Override
    public void onCreate()
    {
        super.onCreate();

        tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        tf3 = Typeface.createFromAsset(getAssets(), fontPath3);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        sharedPreferences = getSharedPreferences(wschool_shared, 0);
        editor = sharedPreferences.edit();
        editor.apply();

        base_URL = sharedPreferences.getString("base_URL", "");
        month = new ArrayList<>();
        year = new ArrayList<>();

        name = email = phone = email = city = zipcode = "";
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++)
        {
            month.add(months[i]);
        }

        if(Wschool.sharedPreferences.getString("login", "0").equals("employee")){
            employee_log = true;
        }else {
            employee_log = false;
        }
    }
}
