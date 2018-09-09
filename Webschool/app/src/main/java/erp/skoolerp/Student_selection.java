package erp.skoolerp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Std_selectnadap;
import erp.skoolerp.Pojo.Std_selpojo;

/**
 * Created by shalu on 09/06/17.
 */

public class Student_selection extends Activity
{
    ViewPager viewPager, viewPager_s;
    Std_selpojo std_selpojo;
    ArrayList<Std_selpojo> child_data;
    AppCompatImageView left, right;
    RelativeLayout single, multiple;
    ImageView imageView;
    String img_url, inst_name;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_selection);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager_s = (ViewPager) findViewById(R.id.viewpager_s);
        left = (AppCompatImageView) findViewById(R.id.leftArrow);
        right = (AppCompatImageView) findViewById(R.id.rightArrow);
        single = (RelativeLayout) findViewById(R.id.single);
        multiple = (RelativeLayout) findViewById(R.id.multiple);
        imageView = (ImageView) findViewById(R.id.imageView);
        title = (TextView) findViewById(R.id.title);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(40);
        viewPager.setClipChildren(false);

        viewPager_s.setOffscreenPageLimit(1);
        viewPager_s.setPageMargin(40);
        viewPager_s.setClipChildren(false);

        child_data = new ArrayList<>();

        //new Show_students().execute();


        Display_data();

        left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final int pos = viewPager.getCurrentItem()-1;
                viewPager.setCurrentItem(pos);
            }
        });

        right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final int pos = viewPager.getCurrentItem()+1;
                viewPager.setCurrentItem(pos);
            }
        });
    }

    private void Display_data()
    {
        final Dialog dialog;
        dialog = new Dialog(Student_selection.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL+"homepage",
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
                            if(status.equals("1"))
                            {
                                JSONArray ja = jo.getJSONArray("rlt");

                                int count = ja.length();
                                for(int i = 0; i < count; i++)
                                {
                                    JSONObject jo1 = ja.getJSONObject(i);
                                    std_selpojo = new Std_selpojo();
                                    std_selpojo.setStd_id(jo1.getString("studentid"));
                                    std_selpojo.setAdm_no(jo1.getString("student_admissionno"));
                                    std_selpojo.setName(jo1.getString("name"));
                                    std_selpojo.setCourse(jo1.getString("course"));
                                    std_selpojo.setBatch(jo1.getString("batch"));
                                    std_selpojo.setImg_url(jo1.getString("student_photo"));
                                    child_data.add(std_selpojo);
                                    inst_name = jo1.getString("instutn_name");
                                    img_url = jo1.getString("instutn_logo");
                                }

                                Picasso.with(Student_selection.this).load(img_url).resize(160, 160).error(R.drawable.dummy).into(imageView);
                                title.setText(inst_name);

                                if(count==1)
                                {
                                    single.setVisibility(View.VISIBLE);
                                    multiple.setVisibility(View.GONE);
                                    viewPager_s.setAdapter(new Std_selectnadap(Student_selection.this, child_data));
                                }else
                                {
                                    multiple.setVisibility(View.VISIBLE);
                                    single.setVisibility(View.GONE);
                                    viewPager.setAdapter(new Std_selectnadap(Student_selection.this, child_data));
                                }


                            }else
                            {
                                Toast.makeText(Student_selection.this, "Error.", Toast.LENGTH_SHORT).show();
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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(Student_selection.this,
                                    "Network not connected.",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Student_selection.this, "Error.", Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
                return params;
            }

        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Wschool.from_main){
            Intent i = new Intent(Student_selection.this, Studentactivity.class);
            i.putExtra("back_presd", true);
            startActivity(i);
            finish();
        }
    }

    //    private class Show_students extends AsyncTask<String, String, String>
//    {
//        Dialog dialog;
//        RequestHandler rh = new RequestHandler();
//        String result = null;
//
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//            dialog = new Dialog(Student_selection.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.progressdialog);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings)
//        {
//            try
//            {
//                String UPLOAD_URL1 = "http://grips.web-school.us/index.php/user/login/homepage";
//
//                HashMap<String, String> data = new HashMap<>();
//                data.put("username", "1DMS1g");
//                result = rh.sendPostRequest(UPLOAD_URL1, data);
//
//            }catch(Exception e)
//            {
//                System.out.println("Recieve_excep "+e);
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s)
//        {
//            super.onPostExecute(s);
//            dialog.dismiss();
//            System.out.println("DATE RECIVED>>>>>> "+s);
//            try
//            {
//                JSONObject jo = new JSONObject(s);
//                String status = jo.getString("sts");
//                if(status.equals("1"))
//                {
//                    JSONArray ja = jo.getJSONArray("rlt");
//                    for(int i = 0; i < ja.length(); i++)
//                    {
//                        JSONObject jo1 = ja.getJSONObject(i);
//                        std_selpojo = new Std_selpojo();
//                        std_selpojo.setStd_id(jo1.getString("studentid"));
//                        std_selpojo.setAdm_no(jo1.getString("student_admissionno"));
//                        std_selpojo.setAuthor(jo1.getString("name"));
//                        std_selpojo.setCourse(jo1.getString("course"));
//                        std_selpojo.setBatch(jo1.getString("batch"));
//                        std_selpojo.setImg_url(jo1.getString("student_photo"));
//                        child_data.add(std_selpojo);
//                    }
//
//                    viewPager.setAdapter(new Std_selectnadap(Student_selection.this, child_data));
//                }else
//                {
//                    Toast.makeText(Student_selection.this, "Error.", Toast.LENGTH_SHORT).show();
//                }
//
//            }catch (Exception e)
//            {
//                System.out.println("DISPLAY_excep \"Monday\" "+e);
//            }
//        }
//    }
}
