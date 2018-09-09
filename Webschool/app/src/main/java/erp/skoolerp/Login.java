package erp.skoolerp;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.utils.FBInstanceIDservice;

/**
 * Created by shalu on 07/06/17.
 */

public  class Login extends Activity
{
    TextView sc, em, pas, copywrite;
    EditText scode, email, paswrd;
    Button login;
    LinearLayout line;
    String username, password, code;
    boolean flag = false;
    BroadcastReceiver broadcastReceiver;
    String regID;
    RelativeLayout logTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Wschool.from_main = false;
        if(Wschool.sharedPreferences.getBoolean("logFLAG", false))
        {
            switch (Wschool.sharedPreferences.getString("login", "error"))
            {
                case "student":
                    Intent intent = new Intent(Login.this, Studentactivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                    break;

                case "employee":
                    Intent intent1 = new Intent(Login.this, Employactivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent1);
                    finish();
                    break;

                case "guardian":
                    Intent intent2 = new Intent(Login.this, Student_selection.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }else
        {
            sc = (TextView) findViewById(R.id.sch_co);
            em = (TextView) findViewById(R.id.em);
            copywrite = (TextView) findViewById(R.id.copywrite);
            pas = (TextView) findViewById(R.id.pas);
            scode = (EditText) findViewById(R.id.school_code);
            email = (EditText) findViewById(R.id.email);
            paswrd = (EditText) findViewById(R.id.password);
            login = (Button) findViewById(R.id.login);
            line = (LinearLayout) findViewById(R.id.line);
            logTotal = (RelativeLayout) findViewById(R.id.logTotal);

            scode.setTypeface(Wschool.tf1);
            email.setTypeface(Wschool.tf1);
            paswrd.setTypeface(Wschool.tf1);
            login.setTypeface(Wschool.tf1);


            scode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    copywrite.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    copywrite.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    copywrite.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    copywrite.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            paswrd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    copywrite.setVisibility(View.GONE);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    copywrite.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            if(Wschool.sharedPreferences.getBoolean("alrdy_login", false))
            {
                sc.setVisibility(View.GONE);
                scode.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                flag = false;
            }else
            {
                sc.setVisibility(View.VISIBLE);
                scode.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                flag = true;
            }
            regID = Wschool.sharedPreferences.getString("regid", "0");
            Log.d("Showid", regID);
            broadcastReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    regID = Wschool.sharedPreferences.getString("regid", "0");
                    Log.d("Showid", regID);
                }
            };

            registerReceiver(broadcastReceiver, new IntentFilter(FBInstanceIDservice.Token_broadcast));

            login.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    username = email.getText().toString();
                    password = paswrd.getText().toString();
                    if (flag){
                        code = scode.getText().toString();

                        if(username.equals("") || password.equals("") || code.equals("")){
                            Toast.makeText(Login.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                        }else{
                            getBaseURL();
                        }
                    }else{
                        if(username.equals("") || password.equals("")){
                            Toast.makeText(Login.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                        }else{
                            getBaseURL();
                        }
                    }
                }
            });
        }

    }

    private void getBaseURL(){
        final Dialog dialog;
        dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        String url = null;
        if(flag){
            url = "http://schoolcode.web-school.us/index.php/user/login/geturlforhttps?username="
                    +username+"&password="+password+"&schoolcode="+code;
        }else{
            url = "http://schoolcode.web-school.us/index.php/user/login/geturlforhttps?username="
                    +username+"&password="+password+"&schoolcode="+Wschool.sharedPreferences.getString("scode", "");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        dialog.dismiss();
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if(status.equals("SUCCESS")) {
                                Wschool.base_URL = jo.getString("url");
                                login_fun();
                            }else{
                                Toast.makeText(Login.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(Login.this,
                                    "Network not connected.",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Login.this, "Error.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void login_fun()
    {
        final Dialog dialog;
        dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        String url = Wschool.base_URL + "index.php/user/login/loginwithschoolcode?username="+
            username+"&password="+password+"&registrationid="+regID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        dialog.dismiss();
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if(status.equals("1"))
                            {
                                String usertype = jo.getString("usertype");
                                switch (usertype)
                                {
                                    case "0":
                                        Wschool.editor.putBoolean("logFLAG", false);
                                        Wschool.editor.commit();
                                        Toast.makeText(Login.this, "Error.", Toast.LENGTH_SHORT).show();
                                        break;

                                    case "1":
                                        Wschool.base_URL = jo.getString("url");
                                        Wschool.editor.putString("base_URL", jo.getString("url"));
                                        Wschool.editor.putString("userid", username);
                                        if(flag)
                                            Wschool.editor.putString("scode", code);
                                        Wschool.editor.putString("login", "student");
                                        Wschool.editor.putBoolean("logFLAG", true);
                                        Wschool.editor.apply();

                                        Wschool.employee_log = false;

                                        Intent intent = new Intent(Login.this, Studentactivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                        finish();
                                        break;

                                    case "2":
                                        Wschool.base_URL = jo.getString("url");
                                        Wschool.editor.putString("base_URL", jo.getString("url"));
                                        Wschool.editor.putString("userid", username);
                                        if(flag)
                                            Wschool.editor.putString("scode", code);
                                        Wschool.editor.putString("login", "employee");
                                        Wschool.editor.putBoolean("logFLAG", true);
                                        Wschool.editor.apply();

                                        Wschool.employee_log = true;

                                        Intent intent1 = new Intent(Login.this, Employactivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent1);
                                        finish();
                                        break;

                                    case "3":
                                        Wschool.base_URL = jo.getString("url");
                                        Wschool.editor.putString("base_URL", jo.getString("url"));
                                        Wschool.editor.putString("userid", username);
                                        if(flag)
                                            Wschool.editor.putString("scode", code);
                                        Wschool.editor.putString("login", "guardian");
                                        Wschool.editor.putBoolean("logFLAG", true);
                                        Wschool.editor.apply();

                                        Wschool.employee_log = false;

                                        Intent intent2 = new Intent(Login.this, Student_selection.class);
                                        intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent2);
                                        finish();
                                        break;
                                }
                            }else{

                                Toast.makeText(Login.this, "Please enter valid username details.", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(Login.this,
                                    "Network not connected.",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Login.this, "Error.", Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("registrationid", regID);

                System.out.println("REG>>>>>>>  "+regID);
                if(flag){
                    params.put("schoolcode", code);
                }else{
                    params.put("schoolcode", Wschool.sharedPreferences.getString("scode", ""));
                }
                        ///............ add registration ID also..........
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unregisterReceiver(broadcastReceiver);
    }
}
