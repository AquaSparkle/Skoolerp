package erp.skoolerp;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Navlistadap;
import erp.skoolerp.Fragment.Assignments;
import erp.skoolerp.Fragment.Circulars;
import erp.skoolerp.Fragment.Classroom;
import erp.skoolerp.Fragment.Events;
import erp.skoolerp.Fragment.Exams;
import erp.skoolerp.Fragment.Fees;
import erp.skoolerp.Fragment.Home;
import erp.skoolerp.Fragment.Library;
import erp.skoolerp.Fragment.Profile;
import erp.skoolerp.Fragment.Student_attendance;
import erp.skoolerp.Fragment.Timetable;
import erp.skoolerp.Fragment.Transportation;

public class Studentactivity extends AppCompatActivity
{
    ImageView hm, circ, prof, cls, logo, inst_logo;
    public TextView title;
    TextView hmt, inst_name, locatn;
    TextView circt;
    TextView proft;
    TextView clst;
    DrawerLayout dl;
    FrameLayout fl;
    Toolbar tb;
    ListView lv;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout left_lay, classrm, profile, circular, nav_icon, home, logout, tot_lay;
    public static int[] drawer_images = {R.drawable.ic_dashboard, R.drawable.ic_attendance, R.drawable.ic_timetable, R.drawable.ic_assignment, R.drawable.ic_fees, R.drawable.ic_exam, R.drawable.ic_library, R.drawable.ic_transportation, R.drawable.ic_event};
    public static String[] list_items = {"Dashboard", "Attendance", "Time table", "Assignments", "Fees", "Exams", "Library", "Transportation", "Events"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentactivity);

        tb = (Toolbar) findViewById(R.id.toolbar);
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        fl = (FrameLayout) findViewById(R.id.frame_layout);
        lv = (ListView) findViewById(R.id.nav_listvw);
        title = (TextView) findViewById(R.id.nav_title);
        nav_icon = (LinearLayout) findViewById(R.id.nav_icon);
        left_lay = (LinearLayout) findViewById(R.id.left_lay);
        classrm = (LinearLayout) findViewById(R.id.classrm);
        profile = (LinearLayout) findViewById(R.id.profile);
        circular = (LinearLayout) findViewById(R.id.circular);
        logout = (LinearLayout) findViewById(R.id.logout);
        tot_lay = (LinearLayout) findViewById(R.id.tot_lay);
        home = (LinearLayout) findViewById(R.id.home);
        hm = (ImageView) findViewById(R.id.hm);
        circ = (ImageView) findViewById(R.id.circ);
        prof = (ImageView) findViewById(R.id.prof);
        cls = (ImageView) findViewById(R.id.cls);
        logo = (ImageView) findViewById(R.id.logo);
        inst_logo = (ImageView) findViewById(R.id.inst_logo);
        hmt = (TextView) findViewById(R.id.hmt);
        circt = (TextView) findViewById(R.id.circt);
        proft = (TextView) findViewById(R.id.proft);
        clst = (TextView) findViewById(R.id.clst);
        inst_name = (TextView) findViewById(R.id.inst_name);
        locatn = (TextView) findViewById(R.id.locatn);

        setSupportActionBar(tb);
        personal_details();
        inst_details();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(Studentactivity.this, dl, tb, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager c = (InputMethodManager) Studentactivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                c.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                float xPositionOpenDrawer = drawerView.getWidth();
                float xPositionWindowContent = (slideOffset * xPositionOpenDrawer);

                tot_lay.setX(xPositionWindowContent);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        dl.setDrawerListener(actionBarDrawerToggle);

        lv.setAdapter(new Navlistadap(Studentactivity.this, list_items, drawer_images));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                if (dl.isDrawerOpen(left_lay))
                {
                    dl.closeDrawer(left_lay);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectFrag(i);
                    }
                }, 0);

            }
        });

        nav_icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dl.openDrawer(GravityCompat.START);
                if (dl.isDrawerOpen(left_lay))
                {
                    dl.closeDrawer(left_lay);
                }
            }
        });

        if(Wschool.sharedPreferences.getBoolean("from_notftn", false)){
            String sectn = Wschool.sharedPreferences.getString("section", "");
            Call_section(sectn);

            Wschool.editor.putBoolean("from_notftn", false);
            Wschool.editor.putString("section", "");
            Wschool.editor.apply();
        }else{
            Fragment frag = new Home();
            FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();
        }


        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String typw = Wschool.sharedPreferences.getString("login", "0");

                if (typw.equals("guardian")) {

                    Wschool.from_main = true;
                    Intent i = new Intent(Studentactivity.this, Student_selection.class);
                    finish();
                    startActivity(i);
                }else{
                    hm.setImageResource(R.drawable.ic_home_blue);
                    circ.setImageResource(R.drawable.ic_circular);
                    prof.setImageResource(R.drawable.ic_profile);
                    cls.setImageResource(R.drawable.ic_classroom);
                    hmt.setTextColor(Color.parseColor("#1a6fa7"));
                    proft.setTextColor(Color.parseColor("#666666"));
                    clst.setTextColor(Color.parseColor("#666666"));
                    circt.setTextColor(Color.parseColor("#666666"));

                    title.setText("Dashboard");
                    Fragment frag = new Home();
                    FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();
                }
            }
        });

        classrm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile);
                cls.setImageResource(R.drawable.ic_classroom_blue);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                clst.setTextColor(Color.parseColor("#1a6fa7"));
                circt.setTextColor(Color.parseColor("#666666"));
                Wschool.prev_frag = 10;
                title.setText("My Class Room");

                Fragment frag = new Classroom();
                FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("cls").commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile_blue);
                cls.setImageResource(R.drawable.ic_classroom);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#1a6fa7"));
                clst.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#666666"));
                Wschool.prev_frag = 12;
                title.setText("My Profile");

                Fragment frag = new Profile();
                FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("prof").commit();
            }
        });

        circular.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular_blue);
                prof.setImageResource(R.drawable.ic_profile);
                cls.setImageResource(R.drawable.ic_classroom);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                clst.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#1a6fa7"));
                Wschool.prev_frag = 11;
                title.setText("Circulars");

                Fragment frag = new Circulars();
                FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("circ").commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dl.isDrawerOpen(left_lay))
                {
                    dl.closeDrawer(left_lay);
                }

                final Dialog dialog = new Dialog(Studentactivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.progressdialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Thread splashTread;
                splashTread = new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            sleep(2000);

                        } catch (Exception e)
                        {
                        } finally
                        {
                            dialog.dismiss();
                            Wschool.editor.putString("userid", "");
                            Wschool.editor.putString("login", "");
                            Wschool.editor.putBoolean("logFLAG", false);
                            Wschool.editor.putBoolean("alrdy_login", true);
                            Wschool.editor.apply();
                            Intent intent = new Intent(Studentactivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                splashTread.start();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            String sectn = intent.getStringExtra("section");

            System.out.println("Section : "+sectn);
            Call_section(sectn);

            if(intent.getBooleanExtra("back_presd", false))
                selectFrag(Wschool.prev_frag);
        }else if(Wschool.from_pay){
            Fragment frag_name = new Fees();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, frag_name).addToBackStack("").commit();
            manager.popBackStack();}
        else{
           // Toast.makeText(this, "home called new int", Toast.LENGTH_SHORT).show();
//            Fragment frag = new Home();
//            FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            String sectn = intent.getStringExtra("section");

            System.out.println("Section : "+sectn);
            Call_section(sectn);

            if(intent.getBooleanExtra("back_presd", false))
                selectFrag(Wschool.prev_frag);
        }else if(Wschool.from_pay){
            Fragment frag_name = new Fees();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.frame_layout, frag_name).addToBackStack("").commit();
            manager.popBackStack();}
        else{
        }
    }

    private void inst_details() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));

        String url = "homepage";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                try{
                                    JSONArray ja = jo.getJSONArray("rlt");
                                    JSONObject joo = ja.getJSONObject(0);
                                    String logo_url = joo.getString("instutn_logo");
                                    String insname = joo.getString("instutn_name");
                                    String insloc = joo.getString("inst_location");

                                    inst_name.setText(insname);
                                    inst_name.setSelected(true);
                                    locatn.setText(insloc);
                                    Picasso.with(Studentactivity.this).load(logo_url).resize(200, 200).error(R.drawable.dummy).into(inst_logo);
                                }catch (Exception e){

                                }


                            } else{
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
                    public void onErrorResponse(VolleyError error){
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Studentactivity.this);
        requestQueue.add(stringRequest);
    }

    private void Call_section(String sec) {
        switch (sec){
            case "1":// attendance
                title.setText("Attendance");
                Fragment frag0 = new Student_attendance();
                FragmentManager fragmentManager0 = Studentactivity.this.getFragmentManager();
                fragmentManager0.beginTransaction().replace(R.id.frame_layout, frag0).addToBackStack("std_att").commit();
                break;

            case "2":// Fees
                title.setText("Fees");
                Fragment frag3 = new Fees();
                FragmentManager fragmentManager3 = Studentactivity.this.getFragmentManager();
                fragmentManager3.beginTransaction().replace(R.id.frame_layout, frag3).addToBackStack("fee").commit();
                break;

            case "3":// Exam list
                break;

            case "4":// Result
                title.setText("Exams");
                Fragment frag4 = new Exams();
                FragmentManager fragmentManager4 = Studentactivity.this.getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame_layout, frag4).addToBackStack("exm").commit();
                break;

            case "5":// Newsfeed
                title.setText("Dashboard");
                Fragment frag = new Home();
                FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();
                break;

            case "6":// Assgnmnt
                title.setText("Assignments");
                Fragment frag2 = new Assignments();
                FragmentManager fragmentManager2 = Studentactivity.this.getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame_layout, frag2).addToBackStack("assgn").commit();
                break;

            case "7":// libisse
                break;

            case "8":// event
                title.setText("Events");
                Fragment frag7 = new Events();
                FragmentManager fragmentManager7 = Studentactivity.this.getFragmentManager();
                fragmentManager7.beginTransaction().replace(R.id.frame_layout, frag7).addToBackStack("eve").commit();
                break;

            case "9":// circ
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular_blue);
                prof.setImageResource(R.drawable.ic_profile);
                cls.setImageResource(R.drawable.ic_classroom);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                clst.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#1a6fa7"));

                title.setText("Circulars");

                Fragment frag9 = new Circulars();
                FragmentManager fragmentManager9 = Studentactivity.this.getFragmentManager();
                fragmentManager9.beginTransaction().replace(R.id.frame_layout, frag9).addToBackStack("circ").commit();
                break;

            default:
                Fragment frag00 = new Home();
                FragmentManager fragmentManager00 = Studentactivity.this.getFragmentManager();
                fragmentManager00.beginTransaction().replace(R.id.frame_layout, frag00).addToBackStack("hom").commit();
                break;
        }
    }

    private void personal_details() {
        final String loginType = Wschool.sharedPreferences.getString("login", "error");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }

        String url = "profile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                try{
                                    JSONArray ja = jo.getJSONArray("rlt");
                                    JSONObject joo = ja.getJSONObject(0);
                                    if (loginType.equals("guardian")) {
                                        Wschool.email = joo.getString("gu_email");
                                        Wschool.name = joo.getString("gu_name");
                                        Wschool.phone = joo.getString("gu_phone");
                                    }else{
                                        Wschool.email = joo.getString("st_email");
                                        Wschool.name = joo.getString("st_name");
                                        Wschool.phone = joo.getString("st_phone");
                                    }
                                    Wschool.city = joo.getString("st_city");
                                    Wschool.zipcode = joo.getString("st_pincode");
                                    String y1 = joo.getString("start_yr");
                                    String y2 = joo.getString("end_yr");

                                    Wschool.year = new ArrayList<>();
                                    Wschool.year.add(y1);
                                    Wschool.year.add(y2);


                                    Picasso.with(Studentactivity.this).load(joo.getString("proflie_pic")).error(R.drawable.dummy).into(logo);
                                }catch (Exception e){

                                }


                            } else{
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
                    public void onErrorResponse(VolleyError error){
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Studentactivity.this);
        requestQueue.add(stringRequest);
    }

    public void selectFrag(int i)
    {
        setImagecolor();
        switch (i)
        {
            case 0:
                Wschool.prev_frag = 0;
                title.setText("Dashboard");
                Fragment frag = new Home();
                FragmentManager fragmentManager = Studentactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();
                break;

            case 1:
                Wschool.prev_frag = 1;
                title.setText("Attendance");
                Fragment frag0 = new Student_attendance();
                FragmentManager fragmentManager0 = Studentactivity.this.getFragmentManager();
                fragmentManager0.beginTransaction().replace(R.id.frame_layout, frag0).addToBackStack("std_att").commit();
                break;

            case 2:
                Wschool.prev_frag = 2;
                title.setText("Time Table");
                Fragment frag1 = new Timetable();
                FragmentManager fragmentManager1 = Studentactivity.this.getFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.frame_layout, frag1).addToBackStack("tymtble").commit();
                break;

            case 3:
                Wschool.prev_frag = 3;
                title.setText("Assignments");
                Fragment frag2 = new Assignments();
                FragmentManager fragmentManager2 = Studentactivity.this.getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame_layout, frag2).addToBackStack("assgn").commit();
                break;

            case 4:
                Wschool.prev_frag = 4;
                title.setText("Fees");
                Fragment frag3 = new Fees();
                FragmentManager fragmentManager3 = Studentactivity.this.getFragmentManager();
                fragmentManager3.beginTransaction().replace(R.id.frame_layout, frag3).addToBackStack("fee").commit();
                break;

            case 5:
                Wschool.prev_frag = 5;
                title.setText("Exams");
                Fragment frag4 = new Exams();
                FragmentManager fragmentManager4 = Studentactivity.this.getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame_layout, frag4).addToBackStack("exm").commit();
                break;

            case 6:
                Wschool.prev_frag = 6;
                title.setText("Library");
                Fragment frag5 = new Library();
                FragmentManager fragmentManager5 = Studentactivity.this.getFragmentManager();
                fragmentManager5.beginTransaction().replace(R.id.frame_layout, frag5).addToBackStack("lib").commit();
                break;

            case 7:
                Wschool.prev_frag = 7;
                title.setText("Transportation");
                Fragment frag6 = new Transportation();
                FragmentManager fragmentManager6 = Studentactivity.this.getFragmentManager();
                fragmentManager6.beginTransaction().replace(R.id.frame_layout, frag6).addToBackStack("trans").commit();
                break;

            case 8:
                Wschool.prev_frag = 8;
                title.setText("Events");
                Fragment frag7 = new Events();
                FragmentManager fragmentManager7 = Studentactivity.this.getFragmentManager();
                fragmentManager7.beginTransaction().replace(R.id.frame_layout, frag7).addToBackStack("eve").commit();
                break;

            case 10:
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile);
                cls.setImageResource(R.drawable.ic_classroom_blue);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                clst.setTextColor(Color.parseColor("#1a6fa7"));
                circt.setTextColor(Color.parseColor("#666666"));

                title.setText("My Class Room");

                Fragment frag8 = new Classroom();
                FragmentManager fragmentManager8 = Studentactivity.this.getFragmentManager();
                fragmentManager8.beginTransaction().replace(R.id.frame_layout, frag8).addToBackStack("cls").commit();
                break;

            case 11:
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular_blue);
                prof.setImageResource(R.drawable.ic_profile);
                cls.setImageResource(R.drawable.ic_classroom);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                clst.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#1a6fa7"));

                title.setText("Circulars");

                Fragment frag9 = new Circulars();
                FragmentManager fragmentManager9 = Studentactivity.this.getFragmentManager();
                fragmentManager9.beginTransaction().replace(R.id.frame_layout, frag9).addToBackStack("circ").commit();
                break;

            case 12:
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile_blue);
                cls.setImageResource(R.drawable.ic_classroom);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#1a6fa7"));
                clst.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#666666"));

                title.setText("My Profile");

                Fragment frag10 = new Profile();
                FragmentManager fragmentManager10 = Studentactivity.this.getFragmentManager();
                fragmentManager10.beginTransaction().replace(R.id.frame_layout, frag10).addToBackStack("prof").commit();
                break;
        }
    }

    public void setImagecolor()
    {
        hm.setImageResource(R.drawable.ic_home);
        circ.setImageResource(R.drawable.ic_circular);
        prof.setImageResource(R.drawable.ic_profile);
        cls.setImageResource(R.drawable.ic_classroom);
        proft.setTextColor(Color.parseColor("#666666"));
        clst.setTextColor(Color.parseColor("#666666"));
        circt.setTextColor(Color.parseColor("#666666"));
        hmt.setTextColor(Color.parseColor("#666666"));
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        if (dl.isDrawerOpen(left_lay))
        {
            dl.closeDrawer(left_lay);
        }else
        {
            int count = getFragmentManager().getBackStackEntryCount();
            FragmentManager fm = getFragmentManager();

            System.out.println("Count::: "+count);
            if (count > 0)
            {
                int c = count - 2;
                if (c >= 0)
                {
                    String ide = fm.getBackStackEntryAt(c).getName();
                    switch (ide)
                    {
                        case "hom": title.setText("Dashboard");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular);
                            prof.setImageResource(R.drawable.ic_profile);
                            cls.setImageResource(R.drawable.ic_classroom);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            clst.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#666666"));
                            break;

                        case "cls": title.setText("My Class Room");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular);
                            prof.setImageResource(R.drawable.ic_profile);
                            cls.setImageResource(R.drawable.ic_classroom_blue);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            clst.setTextColor(Color.parseColor("#1a6fa7"));
                            circt.setTextColor(Color.parseColor("#666666"));
                            break;

                        case "prof": title.setText("My Profile");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular);
                            prof.setImageResource(R.drawable.ic_profile_blue);
                            cls.setImageResource(R.drawable.ic_classroom);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#1a6fa7"));
                            clst.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#666666"));
                            break;

                        case "circ": title.setText("Circular");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular_blue);
                            prof.setImageResource(R.drawable.ic_profile);
                            cls.setImageResource(R.drawable.ic_classroom);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            clst.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#1a6fa7"));
                            break;

                        case "std_att": title.setText("Attendance");
                            setImagecolor();
                            break;

                        case "tymtble": title.setText("Time Table");
                            setImagecolor();
                            break;

                        case "assgn": title.setText("Assignments");
                            setImagecolor();
                            break;

                        case "fee": title.setText("Fees");
                            setImagecolor();
                            break;

                        case "exm": title.setText("Exams");
                            setImagecolor();
                            break;

                        case "lib": title.setText("Library");
                            setImagecolor();
                            break;

                        case "trans": title.setText("Transportation");
                            setImagecolor();
                            break;

                        case "eve": title.setText("Events");
                            setImagecolor();
                            break;

                        case "eedet": title.setText("Event");
                            setImagecolor();
                            break;

                        case "nfd": title.setText("News Feeds");
                            setImagecolor();
                            break;

                        case "cdet": title.setText("Circular");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular_blue);
                            prof.setImageResource(R.drawable.ic_profile);
                            cls.setImageResource(R.drawable.ic_classroom);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            clst.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#1a6fa7"));
                            break;
                    }
                    fm.popBackStack();
                }

                if(count == 1)
                {
                    Studentactivity.this.finish();

                }

            }
        }
    }
}
