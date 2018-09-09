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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.skoolerp.Adapter.SalarayEXPadap;
import erp.skoolerp.Pojo.SalaryPojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 27/06/17.
 */

public class Salary extends Fragment
{
    View view;
    Spinner year, month;
    ArrayList<String> mnth_ar, year_ar;
    ArrayList<SalaryPojo> addtnarray, subtarray;
    TextView salary_title;
    SalaryPojo addtnPojo, subtPojo;
    String mnth_selected, yr_selected, monthValue, salMonth, salYear;
    List<String> _listDataHeader;
    HashMap<String, List<SalaryPojo>> _listDataChild;
    ExpandableListView expandList;
    SalarayEXPadap salarayEXPadap;
    int salary = 0;
    TextView total_pay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.salary, container, false);

        year = (Spinner) view.findViewById(R.id.yr_spinner);
        month = (Spinner) view.findViewById(R.id.month_spinner);
        salary_title = (TextView) view.findViewById(R.id.salary_title);
        expandList = (ExpandableListView) view.findViewById(R.id.expList);
        total_pay = (TextView) view.findViewById(R.id.total_pay);

        mnth_ar = new ArrayList<>();
        year_ar = new ArrayList<>();
        addtnarray = new ArrayList<>();
        subtarray = new ArrayList<>();
        _listDataHeader = new ArrayList<>();
        _listDataChild = new HashMap<>();
        salMonth = salYear = "";
        Calendar calendar = Calendar.getInstance();
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisyr = calendar.get(Calendar.YEAR);

        year_ar = Wschool.year;
        mnth_ar = Wschool.month;

        ArrayAdapter<String> yr_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_black, year_ar);
        yr_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        year.setAdapter(yr_adap);

        ArrayAdapter<String> mnth_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_black, mnth_ar);
        mnth_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        month.setAdapter(mnth_adap);

        int yvalue = year_ar.indexOf(String.valueOf(thisyr));
        if(yvalue != -1){
            year.setSelection(yvalue);
        }
        month.setSelection(thisMonth);
        yr_selected = String.valueOf(thisyr);
        mnth_selected = String.valueOf(thisMonth+1);
        monthValue = mnth_ar.get(thisMonth);

        expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                expandList.expandGroup(i);
                return true;
            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                yr_selected = year_ar.get(i);
                showSalary();
                salary_title.setText("Salary Details: "+monthValue+" "+yr_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                monthValue = mnth_ar.get(i);
                mnth_selected = String.valueOf(i+1);
                showSalary();
                salary_title.setText("Salary Details: "+monthValue+" "+yr_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        return view;
    }

    private void showSalary() {
        addtnarray = new ArrayList<>();
        subtarray = new ArrayList<>();
        _listDataHeader = new ArrayList<>();
        _listDataChild = new HashMap<>();
        salary = 0;

        if(salYear.equals("")){
            salYear = yr_selected;
            salMonth = mnth_selected;
            salaryCall();
        }else{
            if(!salYear.equals(yr_selected) || !salMonth.equals(mnth_selected)){
                salYear = yr_selected;
                salMonth = mnth_selected;
                salaryCall();
            }
        }
    }

    private void salaryCall() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        params.put("month", mnth_selected);
        params.put("year", yr_selected);
        String url = "mysalary";

        new Volley_load(getActivity(), Salary.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < len; i++) {
                        try {
                            addtnPojo = new SalaryPojo();
                            subtPojo = new SalaryPojo();

                            JSONObject jo = s.getJSONObject(i);
                            String type = jo.getString("type");
                            if(type.equals("Addition")){
                                String amt = jo.getString("amount");
                                addtnPojo.setPayhead(jo.getString("payheadname"));
                                addtnPojo.setAmount(amt);
                                int amount = Integer.valueOf(amt);
                                salary += amount;
                                addtnarray.add(addtnPojo);
                            }
                            if(type.equals("Deduction")){
                                String amt1 = jo.getString("amount");
                                subtPojo.setPayhead(jo.getString("payheadname"));
                                subtPojo.setAmount(amt1);
                                int amount = Integer.valueOf(amt1);
                                salary = salary - amount;
                                subtarray.add(subtPojo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    int a1 = addtnarray.size();
                    int a2 = subtarray.size();
                    if(a1 > 0){
                        _listDataHeader.add("Additions");
                        _listDataChild.put(_listDataHeader.get(0), addtnarray);
                    }

                    if(a2 > 0){
                        _listDataHeader.add("Deductions");
                        _listDataChild.put(_listDataHeader.get(1), subtarray);
                    }


                }else{
                    Toast.makeText(getActivity(), "Salary details unavailable.", Toast.LENGTH_SHORT).show();
                }

                salarayEXPadap = new SalarayEXPadap(getActivity(), _listDataHeader, _listDataChild);
                expandList.setAdapter(salarayEXPadap);


                int len1 = _listDataHeader.size();
                for (int i = 0; i < len1; i++) {
                    expandList.expandGroup(i);
                }

                total_pay.setText("Total: INR "+String.valueOf(salary));
            }
        });
    }
}
