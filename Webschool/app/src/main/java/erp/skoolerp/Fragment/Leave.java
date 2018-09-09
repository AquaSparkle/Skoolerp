package erp.skoolerp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import erp.skoolerp.Adapter.Attend_adap;
import erp.skoolerp.CalendarView;
import erp.skoolerp.Pojo.Attendance_pojo;
import erp.skoolerp.Pojo.Date_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 27/06/17.
 */

public class Leave extends Fragment
{
    View view;
    RecyclerView recyclerView;
    ImageView fab;
    Attend_adap adapter;
    Leave fragment;
    ArrayList<String> leave, leaveID;
    CalendarView cv;
    SimpleDateFormat formatter;
    boolean one_clicked, mul_clicked;
    Spinner leave_typ;
    String leaveIdselected="", reason;
    static String datefrom;
    static String dateto;
    static TextView date_frm;
    static TextView date_to;
    Dialog dialog;
    Date today;
    ArrayList<Date> events;
    ArrayList<Date_pojo> events_data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.leave, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.emp_attnd_recyc);
        fab = (ImageView) view.findViewById(R.id.fab);
        today = new Date();

        fragment = this;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        events_data = new ArrayList<>();
        leave = new ArrayList<>();
        leaveID = new ArrayList<>();

        cv = ((CalendarView)view.findViewById(R.id.calendar_view));//............using............//
        cv.settoolbar(Wschool.year);
        cv.frag_leave = fragment;
        cv.Frag_name("leave");

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


        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                leaveIdselected = "";
                final EditText tot_days, reasn;
                Button submit;
                final LinearLayout laay;
                final LinearLayout one_dy, mul_day;
                final ImageView close, one, mul;

                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.leave_alert);

                date_frm = (TextView) dialog.findViewById(R.id.date_frm);
                date_to = (TextView) dialog.findViewById(R.id.date_to);
               // tot_days = (EditText) dialog.findViewById(R.id.total_dys);
                reasn = (EditText) dialog.findViewById(R.id.reason);
              //  laay = (LinearLayout) dialog.findViewById(R.id.laay);
                submit = (Button) dialog.findViewById(R.id.submit);
                one_dy = (LinearLayout) dialog.findViewById(R.id.one_dy);
                mul_day = (LinearLayout) dialog.findViewById(R.id.mul_dy);
                close = (ImageView) dialog.findViewById(R.id.close);
                one = (ImageView) dialog.findViewById(R.id.one);
                mul = (ImageView) dialog.findViewById(R.id.mul);
                leave_typ = (Spinner) dialog.findViewById(R.id.leave_typ);

                leaveCategory();

                leave_typ.setVisibility(View.VISIBLE);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              //  laay.setVisibility(View.GONE);
                close.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });

                date_frm.setTypeface(Wschool.tf1);
                date_to.setTypeface(Wschool.tf1);
              //  tot_days.setTypeface(Wschool.tf1);
                reasn.setTypeface(Wschool.tf1);
                submit.setTypeface(Wschool.tf3);

                date_frm.setHint("Date");
                date_to.setVisibility(View.GONE);

                leave_typ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=0){
                            leaveIdselected = leaveID.get(i-1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                one_dy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        one_clicked = true;
                        mul_clicked = false;
                      //  laay.setVisibility(View.GONE);
                        one.setImageResource(R.drawable.radio_sel);
                        mul.setImageResource(R.drawable.radio_unsel);
                        date_frm.setHint("Date");
                        date_to.setVisibility(View.GONE);
                    }
                });

                mul_day.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        one_clicked = false;
                        mul_clicked = true;
                     //   laay.setVisibility(View.VISIBLE);
                        one.setImageResource(R.drawable.radio_unsel);
                        mul.setImageResource(R.drawable.radio_sel);
                        date_frm.setHint("Date from");
                        date_to.setVisibility(View.VISIBLE);
                    }
                });

                date_frm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogFragment newFragment = new SelectDatefromFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");
                    }
                });

                date_to.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogFragment newFragment = new SelectDatetoFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");
                    }
                });

                submit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        reason = reasn.getText().toString();

                        if(reason.equals("") || leaveIdselected.equals("")){
                            Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                        }else{
                            if(mul_clicked)
                            {
                                // no_days = tot_days.getText().toString();
                                boolean proper_dates = false;
                                if(datefrom.equals("") || dateto.equals("") || reason.equals(""))
                                {
                                    Toast.makeText(getActivity(), "Please enter all fields.", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    proper_dates = compare_dates();
                                    if(proper_dates)
                                        apply_leave();
                                }
                            }else
                            {
                                dateto = datefrom;
                                if(datefrom.equals("") || reason.equals(""))
                                {
                                    Toast.makeText(getActivity(), "Please enter all fields.", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    apply_leave();
                                }
                            }
                        }

                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    private void leaveCategory() {
        leave.add("Select leave category");
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        String url = "leavecategorylist";
        new Volley_load(getActivity(), Leave.this, url, params, new Volley_load.Contents() {

            @Override
            public void returndata(JSONArray s) {

                int len = s.length();
                if(len>0){
                    for (int i = 0; i < len; i++) {
                        try{
                            JSONObject jo = s.getJSONObject(i);
                            leave.add(jo.getString("leavecategoryname"));
                            leaveID.add(jo.getString("leavecategoryid"));
                        }catch(Exception e){

                        }
                    }
                    ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, leave);
                    adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    leave_typ.setAdapter(adap);
                }

            }
        });
    }

    public void setadapter(int yr, int mnth)
    {
        display_leave_details(yr, mnth);
    }

    public static class SelectDatefromFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog d = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            DatePicker dp = d.getDatePicker();
            dp.setMinDate(System.currentTimeMillis() - 1000);
            return d;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd)
        {
            populateSetDate(yy, mm+1, dd);
        }

        public void populateSetDate(int year, int month, int day)
        {
            date_frm.setText(day+" / "+month+" / "+year);
            datefrom = day+"-"+month+"-"+year;
        }
    }

    public static class SelectDatetoFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd)
        {
            populateSetDate(yy, mm+1, dd);
        }

        public void populateSetDate(int year, int month, int day)
        {
            date_to.setText(day+" / "+month+" / "+year);
            dateto = day+"-"+month+"-"+year;
        }
    }

    private boolean compare_dates()
    {
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        boolean b = false;
        try
        {
            if(dfDate.parse(dateto).before(dfDate.parse(datefrom)))
            {
                b = false;//If start date is before end date
                Toast.makeText(getActivity(), "End date is before start date.", Toast.LENGTH_SHORT).show();
            }
            else if(dfDate.parse(dateto).equals(dfDate.parse(datefrom)))
            {
                b = false;//If two dates are equal
                Toast.makeText(getActivity(), "Dates are equal.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                b = true; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    private void apply_leave()
    {
        final Dialog ddialog;
        ddialog = new Dialog(getActivity());
        ddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ddialog.setContentView(R.layout.progressdialog);
        ddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ddialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ddialog.show();

        String url = Wschool.base_URL+"leaveapplication";
        Log.d("URL:", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        ddialog.dismiss();
                        dialog.dismiss();
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if(status.equals("1"))
                            {
                                Toast.makeText(getActivity(), "Leave applied.", Toast.LENGTH_SHORT).show();
                            }else if(status.equals("2"))
                            {
                                Toast.makeText(getActivity(), "Leave already applied.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                            }

                            call_refresh();

                        }catch (Exception e)
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
                        ddialog.dismiss();
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                String user = Wschool.sharedPreferences.getString("userid", "0");
                if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
                    params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
                }
                params.put("username", user);
                params.put("fromdate", datefrom);
                params.put("todate", dateto);
                params.put("reason", reason);
                params.put("leavecategoryid", leaveIdselected);  ////// change with leavecategoryidselected...

                Log.d("params:", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void call_refresh() {

        Fragment frag_name = new Leave();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout, frag_name).addToBackStack("").commit();
        getActivity().getFragmentManager().popBackStack();
    }

    private void display_leave_details(int yr, int mnth) {
        final ArrayList<Attendance_pojo> data = new ArrayList<Attendance_pojo>();
        events = new ArrayList<>();
        events_data = new ArrayList<>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));

        params.put("month", String.valueOf(mnth));
        params.put("year", String.valueOf(yr));
        String url = "absentreportmonthly";
        new Volley_load(getActivity(), Leave.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try {

                            Attendance_pojo pojo = new Attendance_pojo();
                            Date_pojo datepojo = new Date_pojo();

                            JSONObject jo1 = s.getJSONObject(i);
                            String date = jo1.getString("date");
                            String[] d = date.split("-");

                            pojo.setDate(d[0]);
                            datepojo.setDate(date);

                            Date newvalue = formatter.parse(date);
                            boolean flag = compare_now(today, newvalue);

                            datepojo.setAbsent(flag);
                            pojo.setAbsent(flag);

                            int x = Integer.valueOf(d[1]);
                            String month = Wschool.month.get(x-1);
                            pojo.setMonth(month);
                            pojo.setReason(jo1.getString("reason"));
                            data.add(pojo);
                            events_data.add(datepojo);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter = new Attend_adap(getActivity(), data, new Attend_adap.Onitemclicklistner()
                    {
                        @Override
                        public void onItemClick(Attendance_pojo pojo)
                        {

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
                    //Toast.makeText(getActivity(), "Vis", Toast.LENGTH_SHORT).show();
                }else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "No absent report found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean compare_now(Date today, Date newvalue) {
        boolean b = false;
        try
        {
            if(newvalue.before(today) || today.equals(newvalue)){
                b = true;
            }else{
                b = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }
}


