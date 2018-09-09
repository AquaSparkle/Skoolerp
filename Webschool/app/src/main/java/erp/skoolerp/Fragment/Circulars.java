package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Circulr_adap;
import erp.skoolerp.Pojo.Circ_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 23/06/17.
 */

public class Circulars extends Fragment
{
    View view;
    ListView listView;
    ArrayList<Circ_pojo> data;
    Circ_pojo pojo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.circulars, container, false);

        listView = (ListView) view.findViewById(R.id.circular_list);
        data = new ArrayList<Circ_pojo>();

        circular_display();
        return view;
    }

    private void circular_display() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "circulardetails";
        new Volley_load(getActivity(), Circulars.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {

                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try {
                            JSONObject jo1 = s.getJSONObject(i);
                            pojo = new Circ_pojo();
                            pojo.setTitle(jo1.getString("circular_subject"));
                            pojo.setId(jo1.getString("circularid"));
                            pojo.setDescrp(jo1.getString("circular_contents"));
                            pojo.setDate(jo1.getString("circular_date"));
                            data.add(pojo);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listView.setAdapter(new Circulr_adap(getActivity(), data));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Bundle bundle = new Bundle();
                            bundle.putString("circid", data.get(i).getId());

                            Fragment frag = new Circular_detail();
                            frag.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("cdet").commit();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "No circulars found.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
