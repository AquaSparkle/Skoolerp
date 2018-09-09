package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 20/06/17.
 */

public class Profile extends Fragment
{
    View view;
    TextView st_name, st_course, st_roll, st_admno, st_email, st_phone, st_add, st_dob, fname, fjob,
    fmob, m_name, m_job, m_mob, gname, gemail, gadd, gphn, gmob, grel, gedtn, parents1, grdn1;
    RelativeLayout parentsInfo, grdnInfo;
    ImageView std_pic;
    boolean employ = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.profile, container, false);

        st_name = (TextView) view.findViewById(R.id.std_name);
        st_course = (TextView) view.findViewById(R.id.cours);
        st_roll = (TextView) view.findViewById(R.id.roll);
        st_admno = (TextView) view.findViewById(R.id.admn);
        st_email = (TextView) view.findViewById(R.id.email);
        st_phone = (TextView) view.findViewById(R.id.phone);
        st_add = (TextView) view.findViewById(R.id.address);
        st_dob = (TextView) view.findViewById(R.id.dob);
        fname = (TextView) view.findViewById(R.id.father_name);
        fjob = (TextView) view.findViewById(R.id.father_job);
        fmob = (TextView) view.findViewById(R.id.father_mob);
        m_name = (TextView) view.findViewById(R.id.mother_name);
        m_job = (TextView) view.findViewById(R.id.mother_job);
        m_mob = (TextView) view.findViewById(R.id.mother_mob);
        gname = (TextView) view.findViewById(R.id.gdrn_name);
        gemail = (TextView) view.findViewById(R.id.e_mail);
        gadd = (TextView) view.findViewById(R.id.addresss);
        gphn = (TextView) view.findViewById(R.id.phne);
        gmob = (TextView) view.findViewById(R.id.gmob);
        grel = (TextView) view.findViewById(R.id.relatn);
        gedtn = (TextView) view.findViewById(R.id.edctn);
        parents1 = (TextView) view.findViewById(R.id.parents1);
        grdn1 = (TextView) view.findViewById(R.id.grdn1);
        parentsInfo = (RelativeLayout) view.findViewById(R.id.parentsInfo);
        grdnInfo = (RelativeLayout) view.findViewById(R.id.grdnInfo);
        std_pic = (ImageView) view.findViewById(R.id.std_pic);

        show_profile();
        return view;
    }

    private void show_profile() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));

        String login_session = Wschool.sharedPreferences.getString("login", "0");
        if(login_session.equals("employee")){
            employ = true;
        }else{
            employ = false;
        }
        if (login_session.equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "profile";
        new Volley_load(getActivity(), Profile.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {
                try{

                    if(employ){
                        parents1.setVisibility(View.GONE);
                        grdn1.setVisibility(View.GONE);
                        parentsInfo.setVisibility(View.GONE);
                        grdnInfo.setVisibility(View.GONE);

                        JSONObject jo = s.getJSONObject(0);
                        String dpt = jo.getString("department");
                        String addrs = jo.getString("presentaddress");
                        String city = jo.getString("employee_city");
                        String state = jo.getString("employee_state");
                        String cntry = jo.getString("employee_country");
                        String pin = jo.getString("employee_pincode");

                        String address = addrs + "\n" + city + "\n" + state +" - " + cntry + "\n" + "Pincode - "+ pin;
                        String dept = "Dept: "+dpt;

                        st_course.setText(dept);
                        st_roll.setText("Designation: "+jo.getString("designation"));
                        st_admno.setText("Employee code: "+jo.getString("employee_code"));

                        st_email.setText(jo.getString("employee_email"));
                        st_name.setText(jo.getString("name"));
                        st_phone.setText(jo.getString("employee_mobile"));
                        st_dob.setText(jo.getString("employee_dob"));
                        st_add.setText(address);

                        Picasso.with(getActivity()).load(jo.getString("proflie_pic")).error(R.drawable.dummy).into(std_pic);

                    }else{
                        parents1.setVisibility(View.VISIBLE);
                        grdn1.setVisibility(View.VISIBLE);
                        parentsInfo.setVisibility(View.VISIBLE);
                        grdnInfo.setVisibility(View.VISIBLE);

                        JSONObject jo = s.getJSONObject(0);
                        String co = jo.getString("course");
                        String ba = jo.getString("batch");
                        String addrs = jo.getString("presentaddress");
                        String city = jo.getString("st_city");
                        String state = jo.getString("st_state");
                        String cntry = jo.getString("st_country");
                        String pin = jo.getString("st_pincode");

                        String address = addrs + "\n" + city + "\n" + state +" - " + cntry + "\n" + "Pincode - "+ pin;
                        String course = "Course: "+co+" - "+ba;

                        st_course.setText(course);
                        st_roll.setText("Roll No. "+jo.getString("st_rollno"));
                        st_admno.setText("Admission No. "+jo.getString("st_admissionno"));

                        st_email.setText(jo.getString("st_email"));
                        st_name.setText(jo.getString("name"));
                        st_phone.setText(jo.getString("st_phone"));
                        st_dob.setText(jo.getString("st_dob"));
                        st_add.setText(address);

                        fname.setText(jo.getString("father_name"));
                        fjob.setText(jo.getString("father_job"));
                        fmob.setText(jo.getString("father_mobile"));
                        m_name.setText(jo.getString("mother_name"));
                        m_job.setText(jo.getString("mother_job"));
                        m_mob.setText(jo.getString("mother_mobile"));
                        gname.setText(jo.getString("gu_name"));
                        gemail.setText(jo.getString("gu_email"));
                        gadd.setText(jo.getString("gu_address"));
                        gphn.setText(jo.getString("gu_phone"));
                        gmob.setText(jo.getString("gu_mobile"));
                        grel.setText(jo.getString("gu_relation"));
                        gedtn.setText(jo.getString("gu_education"));

                        Picasso.with(getActivity()).load(jo.getString("proflie_pic")).error(R.drawable.dummy).into(std_pic);
                    }

                }catch(Exception e){

                }
            }
        });
    }
}
