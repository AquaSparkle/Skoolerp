package erp.skoolerp.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.StudentDetails_adap;
import erp.skoolerp.Pojo.Stdlist_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 20/06/17.
 */

public class Transportation extends Fragment
{
    View view;
    TextView route, trackng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.transportn, container, false);

        route = (TextView) view.findViewById(R.id.route);
        trackng = (TextView) view.findViewById(R.id.trackng);

        trackng.setBackgroundColor(Color.parseColor("#1a6fa7"));
        trackng.setTextColor(Color.parseColor("#729fbd"));
        route.setBackgroundColor(Color.parseColor("#3498db"));
        route.setTextColor(Color.parseColor("#ffffff"));

        Fragment frag = new Route_details();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();

        getInfo();
        route.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                trackng.setBackgroundColor(Color.parseColor("#1a6fa7"));
                trackng.setTextColor(Color.parseColor("#729fbd"));
                route.setBackgroundColor(Color.parseColor("#3498db"));
                route.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Route_details();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });

        trackng.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                route.setBackgroundColor(Color.parseColor("#1a6fa7"));
                route.setTextColor(Color.parseColor("#729fbd"));
                trackng.setBackgroundColor(Color.parseColor("#3498db"));
                trackng.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Trackings();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });
        return view;
    }

    private void getInfo() {

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }

        String url = Wschool.base_URL+"trackingdetails";

        final Dialog dialog = new Dialog(getActivity());
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
                            if (status.equals("1")) {
                                trackng.setVisibility(View.VISIBLE);
                                route.setEnabled(true);
                            }else{
                                trackng.setVisibility(View.GONE);
                                route.setEnabled(false);
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
                            Toast.makeText(getActivity(),
                                    "Network not connected.",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "Error.", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
