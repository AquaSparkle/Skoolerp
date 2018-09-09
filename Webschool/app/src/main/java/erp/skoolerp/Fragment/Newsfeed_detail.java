package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class Newsfeed_detail extends Fragment
{
    View view;
    TextView title, descrp, nf_date;
    ImageView news_img;
    String newsid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.newsfeed_details, container, false);
        title = (TextView) view.findViewById(R.id.title);
        descrp = (TextView) view.findViewById(R.id.descrp);
        nf_date = (TextView) view.findViewById(R.id.nf_date);
        news_img = (ImageView) view.findViewById(R.id.news_img);

        if(getArguments()!=null)
        {
            newsid = getArguments().getString("newsid");

            showDetails();
        }

        if(Wschool.employee_log){
            ((Employactivity) getActivity()).title.setText("News Feeds");
        }else{
            ((Studentactivity) getActivity()).title.setText("News Feeds");
        }
        return view;
    }

    private void showDetails()
    {
        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        params.put("newsfeedsid", newsid);
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "newsfeedsdetails";

        new Volley_load(getActivity(), Newsfeed_detail.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {
                try
                {
                   JSONObject jo = s.getJSONObject(0);
                    title.setText(jo.getString("newsfeeds_title"));
                    String nfdate = jo.getString("newsfeeds_date");
                    descrp.setText(jo.getString("newsfeeds_description"));

                    Picasso.with(getActivity())
                            .load(jo.getString("newsfeeds_image"))
                            .into(news_img);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(nfdate);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
                    String formattedDate = outputFormat.format(date);

                    nf_date.setText(formattedDate);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

    }
}
