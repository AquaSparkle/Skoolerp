package erp.skoolerp;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import erp.skoolerp.Fragment.Emply_attendnce;
import erp.skoolerp.Fragment.Events;
import erp.skoolerp.Fragment.Home_emply;
import erp.skoolerp.Fragment.Leave;
import erp.skoolerp.Fragment.Library;
import erp.skoolerp.Fragment.Profile;
import erp.skoolerp.Fragment.Salary;
import erp.skoolerp.Fragment.Timetable;
import erp.skoolerp.Fragment.Transportation;

/**
 * Created by shalu on 27/06/17.
 */

public class Employactivity extends AppCompatActivity {
    ImageView hm, circ, prof, even, inst_logo, logo;
    public TextView title;
    TextView hmt;
    TextView circt;
    TextView proft;
    TextView event;
    TextView inst_name;
    TextView locatn;
    DrawerLayout dl;
    FrameLayout fl;
    Toolbar tb;
    ListView lv;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout left_lay, events, profile, circular, nav_icon, home, logout, tot_lay;
    public static int[] drawer_images = {R.drawable.ic_attendance, R.drawable.ic_timetable, R.drawable.ic_assignment, R.drawable.ic_salary, R.drawable.ic_leave, R.drawable.ic_library, R.drawable.ic_transportation};
    public static String[] list_items = {"Attendance", "Time table", "Assignments", "Salary", "Leave", "Library", "Transportation"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employactvty);
        tb = (Toolbar) findViewById(R.id.toolbar);
        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        fl = (FrameLayout) findViewById(R.id.frame_layout);
        lv = (ListView) findViewById(R.id.nav_listvw);
        title = (TextView) findViewById(R.id.nav_title);
        nav_icon = (LinearLayout) findViewById(R.id.nav_icon);
        left_lay = (LinearLayout) findViewById(R.id.left_lay);
        events = (LinearLayout) findViewById(R.id.events);
        profile = (LinearLayout) findViewById(R.id.profile);
        circular = (LinearLayout) findViewById(R.id.circular);
        logout = (LinearLayout) findViewById(R.id.logout);
        home = (LinearLayout) findViewById(R.id.home);
        tot_lay = (LinearLayout) findViewById(R.id.tot_lay);
        hm = (ImageView) findViewById(R.id.hm);
        circ = (ImageView) findViewById(R.id.circ);
        prof = (ImageView) findViewById(R.id.prof);
        even = (ImageView) findViewById(R.id.evn_img);
        inst_logo = (ImageView) findViewById(R.id.inst_logo);
        hmt = (TextView) findViewById(R.id.hmt);
        circt = (TextView) findViewById(R.id.circt);
        proft = (TextView) findViewById(R.id.proft);
        event = (TextView) findViewById(R.id.eve);
        inst_name = (TextView) findViewById(R.id.inst_name);
        locatn = (TextView) findViewById(R.id.locatn);
        logo = (ImageView) findViewById(R.id.logo);

        setSupportActionBar(tb);
        inst_details();
        personal_details();
        nav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(GravityCompat.START);
                if (dl.isDrawerOpen(left_lay)) {
                    dl.closeDrawer(left_lay);
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(Employactivity.this, dl, tb, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager c = (InputMethodManager) Employactivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

        lv.setAdapter(new Navlistadap(Employactivity.this, list_items, drawer_images));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dl.isDrawerOpen(left_lay))
                {
                    dl.closeDrawer(left_lay);
                }

                final Dialog dialog = new Dialog(Employactivity.this);
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
                            Intent intent = new Intent(Employactivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                splashTread.start();
            }
        });

        hm.setImageResource(R.drawable.ic_home_blue);
        circ.setImageResource(R.drawable.ic_circular);
        prof.setImageResource(R.drawable.ic_profile);
        even.setImageResource(R.drawable.ic_event_lightblck);
        hmt.setTextColor(Color.parseColor("#1a6fa7"));
        proft.setTextColor(Color.parseColor("#666666"));
        event.setTextColor(Color.parseColor("#666666"));
        circt.setTextColor(Color.parseColor("#666666"));

        Fragment frag = new Home_emply();
        FragmentManager fragmentManager = Employactivity.this.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (dl.isDrawerOpen(left_lay)) {
                    dl.closeDrawer(left_lay);
                }
                selectFrag(i);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.setImageResource(R.drawable.ic_home_blue);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile);
                even.setImageResource(R.drawable.ic_event_lightblck);
                hmt.setTextColor(Color.parseColor("#1a6fa7"));
                proft.setTextColor(Color.parseColor("#666666"));
                event.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#666666"));

                title.setText("Dashboard");

                Fragment frag = new Home_emply();
                FragmentManager fragmentManager = Employactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("hom").commit();
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile);
                even.setImageResource(R.drawable.ic_event_lightblck_blue);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                event.setTextColor(Color.parseColor("#1a6fa7"));
                circt.setTextColor(Color.parseColor("#666666"));

                title.setText("Events");

                Fragment frag = new Events();
                FragmentManager fragmentManager = Employactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("eve").commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular);
                prof.setImageResource(R.drawable.ic_profile_blue);
                even.setImageResource(R.drawable.ic_event_lightblck);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#1a6fa7"));
                event.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#666666"));

                title.setText("My Profile");

                Fragment frag = new Profile();
                FragmentManager fragmentManager = Employactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("prof").commit();
            }
        });

        circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hm.setImageResource(R.drawable.ic_home);
                circ.setImageResource(R.drawable.ic_circular_blue);
                prof.setImageResource(R.drawable.ic_profile);
                even.setImageResource(R.drawable.ic_event_lightblck);
                hmt.setTextColor(Color.parseColor("#666666"));
                proft.setTextColor(Color.parseColor("#666666"));
                event.setTextColor(Color.parseColor("#666666"));
                circt.setTextColor(Color.parseColor("#1a6fa7"));

                title.setText("Circular");

                Fragment frag = new Circulars();
                FragmentManager fragmentManager = Employactivity.this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("circ").commit();
            }
        });

    }

    public void selectFrag(int i) {
        setImagecolor();
        switch (i) {

            case 0:
                title.setText("Attendance");
                Fragment frag0 = new Emply_attendnce();
                FragmentManager fragmentManager0 = Employactivity.this.getFragmentManager();
                fragmentManager0.beginTransaction().replace(R.id.frame_layout, frag0).addToBackStack("std_att").commit();
                break;

            case 1:
                title.setText("Time Table");
                Fragment frag1 = new Timetable();
                FragmentManager fragmentManager1 = Employactivity.this.getFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.frame_layout, frag1).addToBackStack("tymtble").commit();
                break;

            case 2:
                title.setText("Assignments");
                Fragment frag2 = new Assignments();
                FragmentManager fragmentManager2 = Employactivity.this.getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame_layout, frag2).addToBackStack("assgn").commit();
                break;

            case 3:
                title.setText("My Salary");
                Fragment frag3 = new Salary();
                FragmentManager fragmentManager3 = Employactivity.this.getFragmentManager();
                fragmentManager3.beginTransaction().replace(R.id.frame_layout, frag3).addToBackStack("sal").commit();
                break;

            case 4:
                title.setText("Leave");
                Fragment frag4 = new Leave();
                FragmentManager fragmentManager4 = Employactivity.this.getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame_layout, frag4).addToBackStack("leav").commit();
                break;

            case 5:
                title.setText("Library");
                Fragment frag5 = new Library();
                FragmentManager fragmentManager5 = Employactivity.this.getFragmentManager();
                fragmentManager5.beginTransaction().replace(R.id.frame_layout, frag5).addToBackStack("lib").commit();
                break;

            case 6:
                title.setText("Transportation");
                Fragment frag6 = new Transportation();
                FragmentManager fragmentManager6 = Employactivity.this.getFragmentManager();
                fragmentManager6.beginTransaction().replace(R.id.frame_layout, frag6).addToBackStack("trans").commit();
                break;
        }
    }

    public void setImagecolor() {
        hm.setImageResource(R.drawable.ic_home);
        circ.setImageResource(R.drawable.ic_circular);
        prof.setImageResource(R.drawable.ic_profile);
        even.setImageResource(R.drawable.ic_event_lightblck);
        proft.setTextColor(Color.parseColor("#666666"));
        event.setTextColor(Color.parseColor("#666666"));
        circt.setTextColor(Color.parseColor("#666666"));
        hmt.setTextColor(Color.parseColor("#666666"));
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
                                    Wschool.year = new ArrayList<>();
                                    Wschool.year.add(joo.getString("start_yr"));
                                    Wschool.year.add(joo.getString("end_yr"));
                                    String logo_url = joo.getString("instutn_logo");
                                    String insname = joo.getString("instutn_name");

                                    Picasso.with(Employactivity.this).load(logo_url).resize(200, 200).error(R.drawable.dummy).into(inst_logo);

                                    inst_name.setSelected(true);
                                    inst_name.setText(insname);
                                    String insloc = joo.getString("inst_location");
                                    locatn.setText(insloc);
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

        RequestQueue requestQueue = Volley.newRequestQueue(Employactivity.this);
        requestQueue.add(stringRequest);
    }

    private void personal_details() {

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));

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
                                    Picasso.with(Employactivity.this).load(joo.getString("proflie_pic")).error(R.drawable.dummy).into(logo);
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

        RequestQueue requestQueue = Volley.newRequestQueue(Employactivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        /// add for sal   ,, leav


        if (dl.isDrawerOpen(left_lay)) {
            dl.closeDrawer(left_lay);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();
            FragmentManager fm = getFragmentManager();

            System.out.println("Count::: " + count);
            if (count > 0) {
                int c = count - 2;
                if (c >= 0) {
                    String ide = fm.getBackStackEntryAt(c).getName();
                    switch (ide) {
                        case "hom":
                            title.setText("Dashboard");
                            hm.setImageResource(R.drawable.ic_home_blue);
                            circ.setImageResource(R.drawable.ic_circular);
                            prof.setImageResource(R.drawable.ic_profile);
                            even.setImageResource(R.drawable.ic_event_lightblck);
                            hmt.setTextColor(Color.parseColor("#1a6fa7"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            event.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#666666"));
                            break;

                        case "eve":
                            title.setText("Events");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular);
                            prof.setImageResource(R.drawable.ic_profile);
                            even.setImageResource(R.drawable.ic_event_lightblck_blue);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            event.setTextColor(Color.parseColor("#1a6fa7"));
                            circt.setTextColor(Color.parseColor("#666666"));
                            break;

                        case "prof":
                            title.setText("My Profile");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular);
                            prof.setImageResource(R.drawable.ic_profile_blue);
                            even.setImageResource(R.drawable.ic_event_lightblck);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#1a6fa7"));
                            event.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#666666"));
                            break;

                        case "circ":
                            title.setText("Circular");
                            hm.setImageResource(R.drawable.ic_home);
                            circ.setImageResource(R.drawable.ic_circular_blue);
                            prof.setImageResource(R.drawable.ic_profile);
                            even.setImageResource(R.drawable.ic_event_lightblck);
                            hmt.setTextColor(Color.parseColor("#666666"));
                            proft.setTextColor(Color.parseColor("#666666"));
                            event.setTextColor(Color.parseColor("#666666"));
                            circt.setTextColor(Color.parseColor("#1a6fa7"));
                            break;

                        case "std_att":
                            title.setText("Attendance");
                            setImagecolor();
                            break;

                        case "tymtble":
                            title.setText("Time Table");
                            setImagecolor();
                            break;

                        case "assgn":
                            title.setText("Assignments");
                            setImagecolor();
                            break;

                        case "sal":
                            title.setText("Salary");
                            setImagecolor();
                            break;

                        case "leav":
                            title.setText("Leave");
                            setImagecolor();
                            break;

                        case "lib":
                            title.setText("Library");
                            setImagecolor();
                            break;

                        case "trans":
                            title.setText("Transportation");
                            setImagecolor();
                            break;
                    }
                    fm.popBackStack();
                }

                if (count == 1) {
                    Employactivity.this.finish();

                }

            }
        }
    }
}