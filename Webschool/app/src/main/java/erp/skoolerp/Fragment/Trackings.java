package erp.skoolerp.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import erp.skoolerp.R;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 22/10/17.
 */

public class Trackings extends Fragment implements OnMapReadyCallback {

    View view;
    GoogleMap map;
    private Marker mMarcadorActual;
    MapFragment mapFragment = null;
    TextView locat;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private static final int REQUEST_CONSTANT = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.tracking, container, false);
            locat = (TextView) view.findViewById(R.id.text);
            requestQueue = Volley.newRequestQueue(getActivity());
            if (!checkPermission()) {
                requestPermission();
            }
            getLocation();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getLocation();
                }
            }, 0, 5000);
        } catch (InflateException e) {
            System.out.println("EXCeppppp "+e);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status != ConnectionResult.SUCCESS) {
            updateGoogleplay();
        }
        else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(mapFragment == null){
                    mapFragment = (MapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_view_map);
                    mapFragment.getMapAsync(this);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private void getLocation() {
        String url = Wschool.base_URL+"vehicletracking";

        final Map<String, String> params = new HashMap<>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            System.out.println("Volley_Resp__ "+response );
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                JSONObject jsonObject = jo.getJSONObject("rlt");
                                String lat = jsonObject.getString("latitude");
                                String lon = jsonObject.getString("longitude");

                                setData_map(lat, lon);
                            } else
                            {
                                Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
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
        if(requestQueue != null){
            stringRequest.setShouldCache(false);
            requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    public void updateGoogleplay() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Update Google Play Services");
        alertDialogBuilder
                .setMessage("This Application Want To Update You Google Play Services App")
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                callMarketPlace();
                            }
                        });
        alertDialogBuilder.show();
    }

    public void callMarketPlace() {
        try {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+ "com.google.android.gms")), 1);
        }
        catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+ "com.google.android.gms")), 1);
        }
    }

    public void setData_map(String lato, String lono) throws IOException {

        double lat = Double.parseDouble(lato);
        double lon = Double.parseDouble(lono);
        LatLng point = new LatLng(lat, lon);
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);

        String address = "";
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lon, 1);
            address = addresses.get(0).getAddressLine(0);
            String locality = addresses.get(0).getLocality();
            String admin_area = addresses.get(0).getAdminArea();
            locat.setText(address + " " + locality + " " + admin_area);
        }

        if(mMarcadorActual!= null){
            mMarcadorActual.remove();
            map.clear();
        }
        mMarcadorActual = map.addMarker(new MarkerOptions().position(point).title(address));
        mMarcadorActual.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.van));
        map.moveCamera(CameraUpdateFactory.newLatLng(point));
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18F));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        FragmentManager fm = getFragmentManager();
        Fragment xmlFragment = fm.findFragmentById(R.id.fragment_view_map);
        if (xmlFragment != null) {
            fm.beginTransaction().remove(xmlFragment).commit();
        }
        requestQueue.cancelAll(stringRequest);
        requestQueue = null;
        super.onDestroyView();
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {
            requestPermission();
            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(getActivity(), "Location permission allows to track vehicle. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CONSTANT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  REQUEST_CONSTANT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "Permission Granted, Now you can track the vehicle.", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getActivity(), "Permission Denied, You cannot track vehicle.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

}
