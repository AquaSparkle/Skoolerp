package erp.skoolerp.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Route_adap;
import erp.skoolerp.Pojo.Route_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 20/06/17.
 */

public class Route_details extends Fragment
{
    View view;
    RecyclerView recyclerView;
    private static final int CALL_PHONE_CONSTANT = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.route_detls, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.trans_recy);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!checkPermission()) {
            requestPermission();
        }
        display_details();
        return view;
    }

    private void display_details() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "transportdetails";
        new Volley_load(getActivity(), Route_details.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {
                int len = s.length();
                ArrayList<Route_pojo> data = new ArrayList<>();

                if(len > 0){
                    for(int i = 0; i < len; i++)
                    {
                        try
                        {
                            Route_pojo pojo = new Route_pojo();
                            JSONObject jo1 = s.getJSONObject(i);
                            pojo.setRoute_code(jo1.getString("route_code"));
                            pojo.setVeh_number(jo1.getString("vehicle_no"));
                            pojo.setPick_tym(jo1.getString("time"));
                            pojo.setContact(jo1.getString("driver_phone"));
                            pojo.setDestination(jo1.getString("destination"));
                            data.add(pojo);
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    recyclerView.setAdapter(new Route_adap(getActivity(), data, new Route_adap.Onitemclicklistner() {
                        @Override
                        public void onItemClick(Route_pojo pojo) {


                        }
                    }));
                }else{
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CALL_PHONE)){

            Toast.makeText(getActivity(), "Phone permission allows to make a call. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_CONSTANT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  CALL_PHONE_CONSTANT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "Permission Granted, Now you can make a call.", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getActivity(), "Permission Denied, You cannot make a call.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
