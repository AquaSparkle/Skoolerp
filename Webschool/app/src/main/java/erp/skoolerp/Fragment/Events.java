package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Attend_adap;
import erp.skoolerp.CalendarView;
import erp.skoolerp.Pojo.Attendance_pojo;
import erp.skoolerp.Pojo.Date_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 23/06/17.
 */

public class Events extends Fragment
{
    View view;
    RecyclerView recyclerView;
    Attend_adap adapter;
    Events fragment;
    CalendarView cv;
    SimpleDateFormat formatter, formater1;
    ArrayList<Date> events;
    ArrayList<Date_pojo> events_data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.events, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.eve_recyc);
        fragment = this;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        formater1 = new SimpleDateFormat("dd-MM-yyyy");

        cv = ((CalendarView)view.findViewById(R.id.calendar_view));//............using............//
        cv.settoolbar(Wschool.year);
        cv.frag_event = fragment;
        cv.Frag_name("events");
        //cv.event_array = events;   //............using............//

        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setadapter(int yr, int mnth)
    {
        display_events(yr, mnth);
    }

    private void display_events(int yr, int mnth) {

        final ArrayList<Attendance_pojo> data = new ArrayList<Attendance_pojo>();
        events = new ArrayList<>();
        events_data = new ArrayList<>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        params.put("month", String.valueOf(mnth));
        params.put("year", String.valueOf(yr));
        String url = "viewevent";

        new Volley_load(getActivity(), Events.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < len; i++) {
                        try {
                            JSONObject jo1 = s.getJSONObject(i);
                            Attendance_pojo pojo;
                            Date_pojo datepojo;

                            String start_date = jo1.getString("event_date");
                           // String end_date = jo1.getString("event_end_event");
                            String hday = jo1.getString("is_holiday");
                            String reason = jo1.getString("event_name");
                            String dscp = jo1.getString("event_description");
                            boolean holiday = false;

                            if(hday.equals("is_holiday")){
                                holiday = true;
                            }
                            Date d1 = formatter.parse(start_date);
                            //Date d2 = formatter.parse(end_date);

//                            List<Date> datesInRange = new ArrayList<>();
//                            Calendar calendar = new GregorianCalendar();
//                            calendar.setTime(d1);

//                            Calendar endCalendar = new GregorianCalendar();
//                            endCalendar.setTime(d2);

                            pojo = new Attendance_pojo();
                            datepojo = new Date_pojo();

                            String date = formater1.format(d1);
                            String[] d = date.split("-");
                            pojo.setDate(d[0]);
                            pojo.setAbsent(holiday);
                            int x = Integer.valueOf(d[1]);
                            String month = Wschool.month.get(x-1);
                            pojo.setMonth(month);
                            pojo.setReason(reason);
                            pojo.setDescrp(dscp);
                            pojo.setEve_date(date);
                            data.add(pojo);

                            datepojo.setDate(date);
                            datepojo.setAbsent(holiday);
                            events_data.add(datepojo);

//                            while (calendar.before(endCalendar) || calendar.equals(endCalendar)){
//                                Date result = calendar.getTime();
//                                datesInRange.add(result);
//
//                                pojo = new Attendance_pojo();
//                                datepojo = new Date_pojo();
//
//                                String date = formater1.format(result);
//                                String[] d = date.split("-");
//                                pojo.setDate(d[0]);
//                                pojo.setAbsent(holiday);
//                                int x = Integer.valueOf(d[1]);
//                                String month = Wschool.month.get(x-1);
//                                pojo.setMonth(month);
//                                pojo.setReason(reason);
//                                pojo.setDescrp(dscp);
//                                pojo.setEve_date(date);
//                                data.add(pojo);
//
//                                datepojo.setDate(date);
//                                datepojo.setAbsent(holiday);
//                                events_data.add(datepojo);
//
//                                calendar.add(Calendar.DATE, 1);
//                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new Attend_adap(getActivity(), data, new Attend_adap.Onitemclicklistner()
                    {
                        @Override
                        public void onItemClick(Attendance_pojo pojo)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("title", pojo.getReason());
                            bundle.putString("dsp", pojo.getDescrp());
                            bundle.putString("date", pojo.getEve_date());

                            Fragment frag = new Event_detail();
                            frag.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("eedet").commit();
                        }
                    });

                    cv.event_array = events_data;
                    cv.updateCalendar(events_data);

                    try {

                        //sleep 5 seconds
                        Thread.sleep(1000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "No events found.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
