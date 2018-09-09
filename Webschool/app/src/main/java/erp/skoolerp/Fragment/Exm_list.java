package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.skoolerp.Adapter.Exam_listadap;
import erp.skoolerp.Pojo.Exm_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 15/06/17.
 */

public class Exm_list extends Fragment
{
    View view;
    ExpandableListView expandableListView;
    List<String> header;
    HashMap<String, List<Exm_pojo>> listDataChild;
    Exm_pojo pojo;
    Exam_listadap exam_listadap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.exm_list, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.exp_list);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                expandableListView.expandGroup(i);
                return true;
            }
        });

        show_exm_list();
        return view;
    }

    private void show_exm_list() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "recentexamlist";

        header = new ArrayList<>();
        listDataChild = new HashMap<>();
        new Volley_load(getActivity(), Exm_list.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {

                int len = s.length();

                if(len > 0){
                    for (int i = 0; i < len; i++) {
                        try {
                            JSONObject jsonObject = s.getJSONObject(i);
                            header.add(jsonObject.getString("exam_title"));

                            JSONArray ja = jsonObject.getJSONArray("table");
                            System.out.println("Dataarray__"+ja);
                            int list = ja.length();
                            ArrayList<Exm_pojo> sub_data = new ArrayList<>();

                            for (int j = 0; j < list; j++){

                                JSONObject joo = ja.getJSONObject(j);
                                pojo = new Exm_pojo();
                                pojo.setSub(joo.getString("subject"));
                                pojo.setDate(joo.getString("date"));
                                pojo.setSt_time(joo.getString("start_time"));
                                pojo.setEd_time(joo.getString("end_time"));
                                sub_data.add(pojo);
                            }

                            listDataChild.put(header.get(i), sub_data);
                            System.out.println("Datachild__"+listDataChild);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    exam_listadap = new Exam_listadap(getActivity(), header, listDataChild);
                    expandableListView.setAdapter(exam_listadap);
                    int grp_len = header.size();
                    for (int i = 0; i < grp_len; i++) {
                        expandableListView.expandGroup(i);
                    }
                }else{
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
