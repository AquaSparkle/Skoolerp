package erp.skoolerp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Std_list_adap;
import erp.skoolerp.Pojo.Stdlist_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 28/06/17.
 */

public class Students_list extends Fragment
{
    View view;
    ImageView tick;
    RecyclerView recyclerView;
    Spinner course, batch, subject;
    static TextView date;
    ArrayAdapter<String> course_adap, batch_adap, sub_adap;
    ArrayList<String> course_ar, batch_ar, sub_ar, courIdar, batchIdar, subIdar;
    ArrayList<Stdlist_pojo> data;
    Stdlist_pojo pojo;
    boolean selct_all = false, showSubjct = false;
    Std_list_adap adapter;
    Button save;
    String coursID, dateToday;
    String batchID;
    String subID = "";
    static String DATE;
    int count = 0, marginRight;
    Calendar calendar;
    JSONArray stdArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.std_list, container, false);

        tick = (ImageView) view.findViewById(R.id.tick);
        recyclerView = (RecyclerView) view.findViewById(R.id.std_list_recy);
        course = (Spinner) view.findViewById(R.id.cours_spinner);
        batch = (Spinner) view.findViewById(R.id.batch_spinner);
        subject = (Spinner) view.findViewById(R.id.sub_list);
        date = (TextView) view.findViewById(R.id.date_picker);
        save = (Button) view.findViewById(R.id.save);

        course_ar = new ArrayList<>();
        courIdar = new ArrayList<>();
        batch_ar = new ArrayList<>();
        batchIdar = new ArrayList<>();
        sub_ar = new ArrayList<>();
        subIdar = new ArrayList<>();
        data = new ArrayList<>();

        save.setTypeface(Wschool.tf3);

        save.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        subject.setVisibility(View.GONE);

        marginRight = Math.round(15*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        course_ar.add("Select course");
        course_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, course_ar);
        course_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        course.setAdapter(course_adap);

        batch_ar.add("Select batch");
        batch_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, batch_ar);
        batch_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        batch.setAdapter(batch_adap);

        sub_ar.add("Select subject");
        sub_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, sub_ar);
        sub_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        subject.setAdapter(sub_adap);

        adapter = new Std_list_adap(getActivity(), data, new Std_list_adap.onitemclicklistner() {
            @Override
            public void onItemClick(Stdlist_pojo pojo) {

            }
        });

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        dateToday = format.format(calendar.getTime());
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DATE = yy+"-"+(mm+1)+"-"+dd;
        date.setText(dateToday);
        courseListing();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(date.getText().toString().contains("date")){
                    Toast.makeText(getActivity(), "Please select date.", Toast.LENGTH_SHORT).show();
                }else{
                    if(!data.isEmpty()){
                        stdArray = new JSONArray();
                        for(Stdlist_pojo pojo : data){
                            if(pojo.isChecked()){
                                JSONObject jo = new JSONObject();
                                try {
                                    jo.put("studentid", pojo.getId());
                                    stdArray.put(jo);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // show dialog and then save
                        showDialog();
                        //sendAttendance();
                    }
                }
            }
        });

        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                coursID = "";
                if(courIdar != null && i!=0)
                {
                    coursID = courIdar.get(i-1);
                    batchListing();
                    sub_ar.clear();

                    subject.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);

                    calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dateToday = format.format(calendar.getTime());
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    DATE = yy+"-"+(mm+1)+"-"+dd;
                    date.setText(dateToday);

                    data = new ArrayList<>();
                    adapter.notifychange(data);

                    sub_ar.add("Select subject");
                    sub_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, sub_ar);
                    sub_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    subject.setAdapter(sub_adap);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                batchID = "";
                if(batchIdar != null && i!=0)
                {
                    batchID = batchIdar.get(i-1);
                    subject.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    data = new ArrayList<>();
                    adapter.notifychange(data);

                    if(showSubjct){
                        subjListing();
                        subject.setVisibility(View.VISIBLE);
                        subject.setBackgroundResource(R.drawable.spinner_bg);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)subject.getLayoutParams();
                        params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
                        subject.setLayoutParams(params);
                    }else{
                        studentListing();
                        date.setVisibility(View.VISIBLE);
                        date.setBackgroundResource(R.drawable.cal_bg);    // big one
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    subID = subIdar.get(i-1);
                    studentListing();
                    subject.setBackgroundResource(R.drawable.spinner_smallbg);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)subject.getLayoutParams();
                    params.setMargins(0, 0, marginRight, 0); //substitute parameters for left, top, right, bottom
                    subject.setLayoutParams(params);
                    date.setBackgroundResource(R.drawable.cal_bg_450);   /// small one
                    date.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        tick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(selct_all)
                {
                    tick.setImageResource(R.drawable.checkbox);
                    selct_all = false;

                    if(!data.isEmpty())
                    {
                        for(Stdlist_pojo pojo : data)
                        {
                            int pos = data.indexOf(pojo);
                            pojo.setChecked(false);
                            data.set(pos, pojo);
                        }
                        adapter.notifychange(data);
                        save.setVisibility(View.GONE);
                    }
                }else
                {
                    tick.setImageResource(R.drawable.checkbox_checked);
                    selct_all = true;

                    if(!data.isEmpty())
                    {
                        for(Stdlist_pojo pojo : data)
                        {
                            int pos = data.indexOf(pojo);
                            pojo.setChecked(true);
                            data.set(pos, pojo);
                        }
                        adapter.notifychange(data);
                        save.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }

    private void showDialog() {
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attendance_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void sendAttendance() {

        final Dialog ddialog;
        ddialog = new Dialog(getActivity());
        ddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ddialog.setContentView(R.layout.progressdialog);
        ddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ddialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ddialog.show();

        String url = Wschool.base_URL+"savestudentattendance";
        System.out.println("URL.... "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        ddialog.dismiss();
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if(status.equals("1"))
                            {
                                Toast.makeText(getActivity(), "Attendance saved.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                            }

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
                params.put("courseid", coursID);
                params.put("batchid", batchID);
                params.put("date", DATE);
                params.put("subjectid", subID);
                params.put("sendarray", String.valueOf(stdArray));

                System.out.println("params.... "+params);
                Log.d("userid", user);
                Log.d("courseid", coursID);
                Log.d("batchid", batchID);
                Log.d("date", DATE);
                Log.d("subjectid", subID);
                Log.d("sendarray", String.valueOf(stdArray));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void studentListing() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("courseid", coursID);
        params.put("batchid", batchID);
        params.put("subjectid", subID);
        data = new ArrayList<>();
        String url = "studentattendance";
        new Volley_load(getActivity(), Students_list.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            pojo = new Stdlist_pojo();
                            pojo.setName(jo1.getString("name"));
                            pojo.setAdm_no(jo1.getString("st_admissionno"));
                            pojo.setRoll_no(jo1.getString("st_rollno"));
                            pojo.setImg_url(jo1.getString("proflie_pic"));
                            pojo.setId(jo1.getString("studentid"));
                            pojo.setChecked(false);
                            data.add(pojo);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new Std_list_adap(getActivity(), data, new Std_list_adap.onitemclicklistner()
                    {
                        @Override
                        public void onItemClick(Stdlist_pojo pojo)
                        {
                            int pos = data.indexOf(pojo);
                            if(pojo.isChecked()){
                                pojo.setChecked(false);
                                count--;
                                if(count == 0)
                                    save.setVisibility(View.GONE);
                            }else{
                                pojo.setChecked(true);
                                count++;
                                save.setVisibility(View.VISIBLE);
                            }

                            data.set(pos, pojo);
                            adapter.notifychange(data);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(getActivity(), "Students List Unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void subjListing() {

        sub_ar.clear();
        sub_ar.add("Select subject");
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("courseid", coursID);
        params.put("batchid", batchID);

        String url = "subjectlistforemployee";
        new Volley_load(getActivity(), Students_list.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            sub_ar.add(jo1.getString("subjectname"));
                            subIdar.add(jo1.getString("subjectid"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(getActivity(), "Subjects unavailable.", Toast.LENGTH_SHORT).show();
                }

                sub_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, sub_ar);
                sub_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                subject.setAdapter(sub_adap);
            }
        });
    }

    private void batchListing() {

        final Dialog dialog = new Dialog(getActivity());
        batchIdar = new ArrayList<>();
        batch_ar = new ArrayList<>();
        batch_ar.add("Select batch");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("courseid", coursID);

        String url = Wschool.base_URL+"batchlist";

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
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                JSONObject jooo = jo.getJSONObject("rlt");
                                String subjStatus = jooo.getString("subject_status");
                                if(subjStatus.equals("1")){
                                    showSubjct = true;
                                }else{
                                    showSubjct = false;
                                }

                                JSONArray batches = jooo.getJSONArray("table");
                                int len = batches.length();
                                if(len > 0){
                                    for (int i = 0; i < len; i++) {
                                        JSONObject jo1 = batches.getJSONObject(i);
                                        batch_ar.add(jo1.getString("batchname"));
                                        batchIdar.add(jo1.getString("batchid"));
                                    }

                                    batch_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, batch_ar);
                                    batch_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                                    batch.setAdapter(batch_adap);
                                }
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

    private void courseListing() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));

        String url = "courselistforemployee";
        new Volley_load(getActivity(), Students_list.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            course_ar.add(jo1.getString("coursename"));
                            courIdar.add(jo1.getString("courseid"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    course_adap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_lightblack, course_ar);
                    course_adap.setDropDownViewResource(R.layout.spinner_drop_item);
                    course.setAdapter(course_adap);
                }else{
                    Toast.makeText(getActivity(), "Courses unavailable.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            return pickerDialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd)
        {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day)
        {
            date.setText(day+"-"+month+"-"+year);
            DATE = year+"-"+month+"-"+day;
        }

    }
}
