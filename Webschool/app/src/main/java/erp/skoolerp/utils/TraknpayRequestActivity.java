package erp.skoolerp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import erp.skoolerp.R;
import erp.skoolerp.Wschool;

public class TraknpayRequestActivity extends Activity {

    private static final String TAG = "TNPRequestDebugTag";
    JSONObject pay_obj;
    String pay_response;
    String transactionId, responseCode, responseMessage;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traknpay_request);

        /*
        * IMPORTANT: For this to work your return_page_android.php should have following code.
        * This php page should be hosted on your server and return_url is set to http://yourdomain.com/return_page_android.php
        *
        <script type="text/javascript">
            Android.getPaymentResponse('<?php echo isset($_REQUEST)?json_encode($_REQUEST):'{}' ?>');
        </script>
        *
        */

        String salt = Wschool.salt; // put your salt
        String api_key = Wschool.api_link;  // put your api_key
        String return_url = "https://biz.traknpay.in/tnp/return_page_android.php";
        String mode = "LIVE";
        String order_id = "0";
        String amount = "2.00";
        String currency = "INR";
        String description = "Payment from App SDK";
        String address_line_1 = "";
        String address_line_2 = "";
        String state = "";
        String country = "IND";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        // mandatory fields
        String name = "Test_name";
        if(!Wschool.name.isEmpty() && !Wschool.name.equals(" ")){
            name = Wschool.name;
        }
        String email = "test@example.com";
        if(!Wschool.email.isEmpty() && !Wschool.email.equals(" ")){
            email = Wschool.email;
        }
        String phone = "9900123456";
        if(!Wschool.phone.isEmpty() && !Wschool.phone.equals(" ")){
            phone = Wschool.phone;
        }
        String city = "Bangalore";
        if(!Wschool.city.isEmpty() && !Wschool.city.equals(" ")){
            city = Wschool.city;
        }
        String zip_code = "560001";
        if(!Wschool.zipcode.isEmpty() && !Wschool.zipcode.equals(" ")){
            zip_code = Wschool.zipcode;
        }
        // Getting these values from Main activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mode = extras.getString("mode");
            amount = extras.getString("amount");
            order_id = extras.getString("order_id");
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("api_key", api_key);
        map.put("return_url", return_url);
        map.put("mode", "LIVE");
        map.put("order_id", order_id);
        map.put("amount", amount);
        map.put("currency", currency);
        map.put("description", description);
        map.put("name", name);
        map.put("email", email);
        map.put("phone", phone);
        map.put("address_line_1", address_line_1);
        map.put("address_line_2", address_line_2);
        map.put("city", city);
        map.put("state", state);
        map.put("zip_code", zip_code);
        map.put("country", country);
        map.put("udf1", udf1);
        map.put("udf2", udf2);
        map.put("udf3", udf3);
        map.put("udf4", udf4);
        map.put("udf5", udf5);

        String hashData = salt;
        String postData = "";


        System.out.println("Mapkeyset...."+map.keySet());
        // Sort the map by key and create the hashData and postData
        for (String key : new TreeSet<String>(map.keySet())) {
            if (!map.get(key).equals("")) {
                hashData = hashData + "|" + map.get(key);
                postData = postData + key + "=" + map.get(key) + "&";
            }
        }

        // Generate the hash key using hashdata and append the has to postData query string
        String hash = generateSha512Hash(hashData).toUpperCase();
        postData = postData + "hash=" + hash;

        Log.d(TAG, "HashData: " + hashData);
        Log.d(TAG, "Hash: " + hash);
        Log.d(TAG, "PostData: " + postData);

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.postUrl(Wschool.req_link, postData.getBytes());    //// put your integration url....
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "Android");

    }

    /**
     * Generates the SHA-512 hash (same as PHP) for the given string
     *
     * @param toHash string to be hashed
     * @return return hashed string
     */
    public String generateSha512Hash(String toHash) {
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(toHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    /**
     * Converts the given byte[] to a hex string.
     *
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    private String convertToHex(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte aRaw : raw) {
            sb.append(Integer.toString((aRaw & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     *  Interface to bind Javascript from WebView with Android
     */
    public class MyJavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void getPaymentResponse(String jsonResponse) {
            try {

                JSONObject resposeData = new JSONObject(jsonResponse);

                pay_response = jsonResponse;
                System.out.println("RESpiiiiiii - "+pay_response);

                Log.d(TAG, "ResponseJson: " + resposeData.toString());
                transactionId = resposeData.getString("transaction_id");
                responseCode = resposeData.getString("response_code");
                responseMessage = resposeData.getString("response_message");

                send_response();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void send_response() {

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }

        params.put("sendarray", pay_response);
        String url = "tranknpayresponse";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            System.out.println("Volley_Resp response saving__ "+response );
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                Intent intent = new Intent(getApplicationContext(), TraknpayResponseActivity.class);
                                intent.putExtra("transactionId", transactionId);
                                intent.putExtra("responseCode", responseCode);
                                intent.putExtra("responseMessage", responseMessage);
                                Toast.makeText(TraknpayRequestActivity.this, "Transaction response saved.", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                                finish();

                            } else{

                                Toast.makeText(TraknpayRequestActivity.this, "Transaction response not saved.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (Exception e)
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

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(TraknpayRequestActivity.this);
        requestQueue.add(stringRequest);
    }
}
