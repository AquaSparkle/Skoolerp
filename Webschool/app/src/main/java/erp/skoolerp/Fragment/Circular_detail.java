package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Employactivity;
import erp.skoolerp.R;
import erp.skoolerp.Studentactivity;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 10/07/17.
 */

public class Circular_detail extends Fragment
{
    View view;
    ImageView icon;
    TextView title, descrp, nf_date;
    String circ_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.newsfeed_details, container, false);
        icon = (ImageView) view.findViewById(R.id.news_img);
        title = (TextView) view.findViewById(R.id.title);
        descrp = (TextView) view.findViewById(R.id.descrp);
        nf_date = (TextView) view.findViewById(R.id.nf_date);

        icon.setVisibility(View.GONE);
        if(getArguments()!=null)
        {
            circ_id = getArguments().getString("circid");

            showDetails();
        }


        if(Wschool.employee_log){
            ((Employactivity) getActivity()).title.setText("Circular");
        }else{
            ((Studentactivity) getActivity()).title.setText("Circular");
        }

        return view;
    }

    private void showDetails() {

        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("circularid", circ_id);
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "circulardetails1";

        new Volley_load(getActivity(), Circular_detail.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {
                try
                {
                    JSONObject jo = s.getJSONObject(0);
                    title.setText(jo.getString("circular_subject"));
                    String nfdate = jo.getString("circular_date");
                    descrp.setText(jo.getString("circular_contents"));
//                    Date format giving in api is correct hence commented.....
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
//                    Date date = sdf.parse(nfdate);
//                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
//                    String formattedDate = outputFormat.format(date);

                    nf_date.setText(nfdate);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}
