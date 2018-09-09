package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Subjectlist_adap;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 20/06/17.
 */

public class Classroom extends Fragment
{
    View view;
    ListView listView;
    ArrayList<String> sub, sub_code;
    TextView teach_name, teach_email, teach_numb;
    ImageView teach_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.classroom, container, false);

        listView = (ListView) view.findViewById(R.id.sub_list);
        teach_name = (TextView) view.findViewById(R.id.teach_name);
        teach_email = (TextView) view.findViewById(R.id.teach_email);
        teach_numb = (TextView) view.findViewById(R.id.teach_numb);
        teach_pic = (ImageView) view.findViewById(R.id.teach_pic);

        sub = new ArrayList<String>();
        sub_code = new ArrayList<String>();

        teacher_details();
        subject_details();

        return view;
    }

    private void teacher_details()
    {
        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "classteacherdetails";

        new Volley_load(getActivity(), Classroom.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {
                try
                {
                    JSONObject jo = s.getJSONObject(0);
                    teach_name.setText(jo.getString("name"));
                    teach_email.setText(jo.getString("email"));
                    teach_numb.setText(jo.getString("phone"));

                    Picasso.with(getActivity())
                            .load(jo.getString("photo"))
                            .error(R.drawable.dummy).into(teach_pic);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    private void subject_details()
    {
        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "mysubjects";

        new Volley_load(getActivity(), Classroom.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {

                int len = s.length();
                if(len > 0){
                    try
                    {
                        for(int i = 0; i < len; i++)
                        {
                            JSONObject jo = s.getJSONObject(i);
                            sub.add(jo.getString("subject"));
                            sub_code.add(jo.getString("sub_code"));
                        }

                        listView.setAdapter(new Subjectlist_adap(getActivity(), sub, sub_code));
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(), "Subject details unavailable.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
