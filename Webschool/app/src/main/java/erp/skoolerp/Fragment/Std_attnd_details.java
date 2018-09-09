package erp.skoolerp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.StudentDetails_adap;
import erp.skoolerp.Pojo.Stdlist_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 28/06/17.
 */

public class Std_attnd_details extends Fragment
{
    View view;
    Spinner course, batch, subject;
    static TextView date;
    ArrayAdapter<String> course_adap, batch_adap, sub_adap;
    ArrayList<String> course_ar, batch_ar, sub_ar, courIdar, batchIdar, subIdar;
    static String DATE, coursID, batchID;
    static String subID = "";
    boolean showSubjct = false;
    RecyclerView absentee;
    static ArrayList<Stdlist_pojo> data;
    StudentDetails_adap adapter;
    static Std_attnd_details context;
    static TableRow list;
    TableRow label;
    String dateToday;
    Calendar calendar;
    int marginRight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.std_sttnd_details, container, false);

        course = (Spinner) view.findViewById(R.id.cours_spinner);
        batch = (Spinner) view.findViewById(R.id.batch_spinner);
        subject = (Spinner) view.findViewById(R.id.sub_list);
        date = (TextView) view.findViewById(R.id.date_picker);
        absentee = (RecyclerView) view.findViewById(R.id.absentee);
        list = (TableRow) view.findViewById(R.id.list);
        label = (TableRow) view.findViewById(R.id.label);

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        dateToday = format.format(calendar.getTime());
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DATE = yy+"-"+(mm+1)+"-"+dd;
        date.setText(dateToday);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        absentee.setLayoutManager(mLayoutManager);

        course_ar = new ArrayList<>();
        batch_ar = new ArrayList<>();
        sub_ar = new ArrayList<>();
        courIdar = new ArrayList<>();
        batchIdar = new ArrayList<>();
        subIdar = new ArrayList<>();
        data = new ArrayList<>();

        date.setVisibility(View.GONE);
        subject.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        label.setVisibility(View.GONE);

        marginRight = Math.round(15*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

        context = Std_attnd_details.this;
        course_ar.add("Select course");
        course_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, course_ar);
        course_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        course.setAdapter(course_adap);

        batch_ar.add("Select batch");
        batch_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, batch_ar);
        batch_adap.setDropDownViewResource(R.layout.spinner_drop_item);
        batch.setAdapter(batch_adap);

        courseListing();

        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                absentee.setVisibility(View.GONE);
                label.setVisibility(View.GONE);
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
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

                    date.setVisibility(View.GONE);
                    subject.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                    label.setVisibility(View.GONE);
                    absentee.setVisibility(View.GONE);

                    calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dateToday = format.format(calendar.getTime());
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    DATE = yy+"-"+(mm+1)+"-"+dd;
                    date.setText(dateToday);

                    if(adapter!=null){
                        data = new ArrayList<>();
                        adapter.notifyData(data);
                    }

                    sub_ar.add("Select subject");
                    sub_adap = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_lightblack, sub_ar);
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
                    date.setVisibility(View.GONE);
                    subject.setVisibility(View.GONE);
                    list.setVisibility(View.GONE);
                    label.setVisibility(View.GONE);

                    if(showSubjct){
                        subjListing();
                        subject.setVisibility(View.VISIBLE);
                        subject.setBackgroundResource(R.drawable.spinner_bg);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)subject.getLayoutParams();
                        params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
                        subject.setLayoutParams(params);
                    }else{
                        date.setVisibility(View.VISIBLE);
                        date.setBackgroundResource(R.drawable.cal_bg);    // big one
                        date.setVisibility(View.VISIBLE);
                        studentListing();
                        list.setVisibility(View.VISIBLE);
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

                    subject.setBackgroundResource(R.drawable.spinner_smallbg);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)subject.getLayoutParams();
                    params.setMargins(0, 0, marginRight, 0); //substitute parameters for left, top, right, bottom
                    subject.setLayoutParams(params);
                    date.setBackgroundResource(R.drawable.cal_bg_450);   /// small one
                    studentListing();
                    date.setVisibility(View.VISIBLE);
                    list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentListing();
            }
        });
        return view;
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
            list.setVisibility(View.VISIBLE);
        }
    }

    private void subjListing() {

        sub_ar.clear();
        sub_ar.add("Select subject");
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("courseid", coursID);
        params.put("batchid", batchID);

        String url = "subjectlistforemployee";
        new Volley_load(getActivity(), Std_attnd_details.this, url, params, new Volley_load.Contents() {
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
        batch_ar.clear();
        batchIdar.clear();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void courseListing() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));

        String url = "courselistforemployee";
        new Volley_load(getActivity(), Std_attnd_details.this, url, params, new Volley_load.Contents() {
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

    void studentListing() {
        data = new ArrayList<>();

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("courseid", coursID);
        params.put("batchid", batchID);
        params.put("subjectid", subID);
        params.put("date", DATE);

        String url = Wschool.base_URL+"absenteeslist";

        final Dialog dialog = new Dialog(getActivity());
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
                            System.out.println("Volley_Resp__ "+response );
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {

                                String attendanceStatus = jo.getString("status");
                                if(attendanceStatus.equals("1")){
                                    Toast.makeText(getActivity(), "All students were present.", Toast.LENGTH_SHORT).show();
                                }else if(attendanceStatus.equals("2")){
                                    JSONArray ja = jo.getJSONArray("rlt");
                                    int len = ja.length();
                                    if(len > 0){
                                        for (int i = 0; i < ja.length(); i++) {
                                            try
                                            {
                                                JSONObject jo1 = ja.getJSONObject(i);
                                                Stdlist_pojo pojo = new Stdlist_pojo();
                                                pojo.setName(jo1.getString("name"));
                                                pojo.setAdm_no(jo1.getString("st_admissionno"));
                                                data.add(pojo);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        label.setVisibility(View.VISIBLE);
                                        absentee.setVisibility(View.VISIBLE);

                                    }else{
                                        Toast.makeText(getActivity(), "List unavailable.", Toast.LENGTH_SHORT).show();
                                    }

                                    if(adapter==null){
                                        adapter = new StudentDetails_adap(getActivity(), data);
                                        absentee.setAdapter(adapter);
                                        absentee.setNestedScrollingEnabled(false);
                                    }else{
                                        adapter.notifyData(data);
                                    }

                                }else if(attendanceStatus.equals("3")){
                                    Toast.makeText(getActivity(), "Attendance is not entered for selected date.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), "Invalid entry.", Toast.LENGTH_SHORT).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
