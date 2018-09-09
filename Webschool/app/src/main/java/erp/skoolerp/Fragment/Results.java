package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.skoolerp.Adapter.Results_adap;
import erp.skoolerp.Pojo.Result_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 15/06/17.
 */

public class Results extends Fragment
{
    View view;
    Spinner term, exam;
    ArrayList<String> term_list, exm_list, term_id, exm_id;
    ArrayAdapter<String> term_adap, exm_adap;
    String sel_termid, sel_exmid, sel_term, sel_exm, title;
    ArrayList<Result_pojo> r_data;
    Result_pojo pojo;
    ExpandableListView exp_list;
    HashMap<String, List<Result_pojo>> listDataChild;
    List<String> header;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.results, container, false);

        term = (Spinner) view.findViewById(R.id.term_spinner);
        exam = (Spinner) view.findViewById(R.id.exm_spinner);
        exp_list = (ExpandableListView) view.findViewById(R.id.exp_list);

        term_list = new ArrayList<>();
        exm_list = new ArrayList<>();
        r_data = new ArrayList<>();
        term_id = new ArrayList<>();
        exm_id = new ArrayList<>();

        term_listng();

        term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sel_termid = term_id.get(i);
                sel_term = term_list.get(i);
                exm_listng();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sel_exmid = exm_id.get(i);
                sel_exm = exm_list.get(i);
                display_result();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                exp_list.expandGroup(i);
                return true;
            }
        });
        return view;
    }

    private void display_result() {

        final boolean[] cbse = {false};
        header = new ArrayList<>();
        listDataChild = new HashMap<>();
        r_data = new ArrayList<>();

        title = sel_term + " - " +sel_exm;

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        params.put("termid", sel_termid);
        params.put("examid", sel_exmid);

        String url = "examresult";
        new Volley_load(getActivity(), Results.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {

            try{
                JSONObject json = s.getJSONObject(0);
                String type = json.getString("markformat");

                Wschool.title1 = json.getString("title1");
                Wschool.title2 = json.getString("title2");
                Wschool.title3 = json.getString("title3");
                Wschool.title4 = json.getString("title4");

                if(type.equals("GPA")){  // assement mark present
                    cbse[0] = true;
                }

                header.add(title);
                JSONArray ja = json.getJSONArray("marks");
                int sub_len = ja.length();

                if(sub_len > 0){
                    for (int i = 0; i < sub_len; i++) {

                        JSONObject jo = ja.getJSONObject(i);
                        pojo = new Result_pojo();
                        pojo.setSubj(jo.getString("subject"));
                        if(cbse[0])
                            pojo.setAsses_mark(jo.getString("assessmentmarks"));
                        pojo.setWrittn_mark(jo.getString("writtenmarks"));
                        pojo.setTotal(jo.getString("total"));

                        r_data.add(pojo);
                    }
                    exp_list.setVisibility(View.VISIBLE);
                    listDataChild.put(header.get(0), r_data);
                    exp_list.setAdapter(new Results_adap(getActivity(), header, listDataChild, cbse[0]));

                    for (int i = 0; i < sub_len; i++) {
                        exp_list.expandGroup(i);
                    }
                }else{
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                    exp_list.setVisibility(View.GONE);
                }

            }catch (Exception e){

            }

            }
        });
    }

    private void term_listng() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "termdetails";
        new Volley_load(getActivity(), Results.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();

                if(len > 0){
                    for (int i = 0; i < len; i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            term_id.add(jo1.getString("termid"));
                            term_list.add("Term "+jo1.getString("name"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    term_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_black, term_list);
                    term_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    term.setAdapter(term_adap);

                    sel_termid = term_id.get(0);
                }else{
                    Toast.makeText(getActivity(), "Terms unavailable.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void exm_listng() {
        exm_id = new ArrayList<>();
        exm_list = new ArrayList<>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        params.put("termid", sel_termid);

        String url = "examdetails";
        new Volley_load(getActivity(), Results.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();

                if(len > 0){
                    for (int i = 0; i < len; i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            exm_id.add(jo1.getString("examid"));
                            exm_list.add(jo1.getString("name"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    exm_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_black, exm_list);
                    exm_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    exam.setAdapter(exm_adap);
                }else{
                    exm_list.add("Select exam");
                    exm_id.add("");
                    exm_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_black, exm_list);
                    exm_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    exam.setAdapter(exm_adap);
                    Toast.makeText(getActivity(), "Exams unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}