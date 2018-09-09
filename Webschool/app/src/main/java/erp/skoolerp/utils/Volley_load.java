package erp.skoolerp.utils;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import erp.skoolerp.R;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 10/07/17.
 */

public class Volley_load
{
    final Context context;
    Fragment frag;
    String url;
    Map<String, String> params;
    private final Dialog dialog;
    Contents contents;

    public interface Contents
    {
        public void returndata(JSONArray s);
    }

    public Volley_load(final Context context, final Fragment frag, String urL, final Map<String, String> params, final Contents contents)
    {
        this.context = context;
        this.frag = frag;
        this.url = Wschool.base_URL + urL;
        this.params = params;
        this.contents = contents;
        System.out.println("URL.... "+url);
        System.out.println("params.... "+params);

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) 
                    {
                        dialog.dismiss();
                        try 
                        {
                            JSONObject jo = new JSONObject(response);
                            System.out.println("Volley_Resp__ "+response );
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                JSONArray ja = jo.getJSONArray("rlt");
                                contents.returndata(ja);
                            } else 
                            {
                                Toast.makeText(context, "Error.", Toast.LENGTH_SHORT).show();
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
                        dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context,
                                    "Network not connected.",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context, "Error.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) 
        {
            @Override
            protected Map<String, String> getParams() 
            {
                return params;
            }

        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
