package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Tymtabl_adap;
import erp.skoolerp.Pojo.Tymtabl_Pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 19/06/17.
 */

public class Timetable extends Fragment
{
    View view;
    ListView listView;
    ArrayList<Tymtabl_Pojo> data;
    Tymtabl_Pojo pojo;
    String userid;
    TextView mon, tue, wed, thu, fri, sat, sun;
    String day;
    Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.tymtabl, container, false);

        listView = (ListView) view.findViewById(R.id.table_display);
        mon = (TextView) view.findViewById(R.id.mon);
        tue = (TextView) view.findViewById(R.id.tue);
        wed = (TextView) view.findViewById(R.id.wed);
        thu = (TextView) view.findViewById(R.id.thu);
        fri = (TextView) view.findViewById(R.id.fri);
        sat = (TextView) view.findViewById(R.id.sat);
        sun = (TextView) view.findViewById(R.id.sun);

        data = new ArrayList<>();
        userid = Wschool.sharedPreferences.getString("userid", "0");

        calendar = Calendar.getInstance();
        int day_value = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("DAy....."+day_value);

        switch (day_value){
            case Calendar.MONDAY:
                mon.setTextColor(Color.parseColor("#ffffff"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Monday\"";
                break;

            case Calendar.TUESDAY:
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#ffffff"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Tuesday\"";
                break;

            case Calendar.WEDNESDAY:
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#ffffff"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Wednesday\"";
                break;

            case Calendar.THURSDAY:
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#ffffff"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Thursday\"";
                break;

            case Calendar.FRIDAY:
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#ffffff"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Friday\"";
                break;

            case Calendar.SATURDAY:
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#ffffff"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Saturday\"";
                break;

            case Calendar.SUNDAY:
                sun.setTextColor(Color.parseColor("#ffffff"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Sunday\"";
                break;
        }

        Show_timetable();

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mon.setTextColor(Color.parseColor("#ffffff"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Monday\"";
                Show_timetable();
            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tue.setTextColor(Color.parseColor("#ffffff"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Tuesday\"";
                Show_timetable();
            }
        });

        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wed.setTextColor(Color.parseColor("#ffffff"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Wednesday\"";
                Show_timetable();
            }
        });

        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thu.setTextColor(Color.parseColor("#ffffff"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Thursday\"";
                Show_timetable();
            }
        });

        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fri.setTextColor(Color.parseColor("#ffffff"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Friday\"";
                Show_timetable();
            }
        });

        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sat.setTextColor(Color.parseColor("#ffffff"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sun.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Saturday\"";
                Show_timetable();
            }
        });

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sun.setTextColor(Color.parseColor("#ffffff"));
                mon.setTextColor(Color.parseColor("#729fbd"));
                tue.setTextColor(Color.parseColor("#729fbd"));
                wed.setTextColor(Color.parseColor("#729fbd"));
                thu.setTextColor(Color.parseColor("#729fbd"));
                fri.setTextColor(Color.parseColor("#729fbd"));
                sat.setTextColor(Color.parseColor("#729fbd"));
                day = "\"Sunday\"";
                Show_timetable();
            }
        });

        return view;
    }

    private void Show_timetable()
    {
        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("day", day);
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "viewtimetabledaywise";

        new Volley_load(getActivity(), Timetable.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {
                try
                {
                    data = new ArrayList<>();
                    int len = s.length();
                    if(len > 0){

                        if(Wschool.employee_log){
                            for(int i = 0; i < s.length(); i++)
                            {
                                JSONObject jo = s.getJSONObject(i);
                                Tymtabl_Pojo pojo = new Tymtabl_Pojo();
                                pojo.setCode(String.valueOf(i+1));
                                pojo.setSub_code(jo.getString("sub_code"));
                                pojo.setSub(jo.getString("subject"));
                                pojo.setStaff(jo.getString("course") +" - "+jo.getString("batch"));
                                pojo.setStart_tym(jo.getString("start_tym"));
                                pojo.setEnd_tym(jo.getString("end_tym"));
                                data.add(pojo);
                            };
                        }else{
                            for(int i = 0; i < s.length(); i++)
                            {
                                JSONObject jo = s.getJSONObject(i);
                                Tymtabl_Pojo pojo = new Tymtabl_Pojo();
                                pojo.setCode(String.valueOf(i+1));
                                pojo.setSub_code(jo.getString("sub_code"));
                                pojo.setSub(jo.getString("subject"));
                                pojo.setStaff(jo.getString("employee"));
                                pojo.setStart_tym(jo.getString("start_tym"));
                                pojo.setEnd_tym(jo.getString("end_tym"));
                                data.add(pojo);
                            };
                        }


                        listView.setAdapter(new Tymtabl_adap(getActivity(), data));
                    }else{
                        listView.setAdapter(new Tymtabl_adap(getActivity(), data));
                        Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}
