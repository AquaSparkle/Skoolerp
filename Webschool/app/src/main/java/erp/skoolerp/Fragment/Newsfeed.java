package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Newsfeed_Adp;
import erp.skoolerp.Pojo.Newsfd_POJO;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 18/08/17.
 */

public class Newsfeed extends Fragment
{
    View view;
    RecyclerView recyclerView;
    ArrayList<Newsfd_POJO> data;
    int ht = 100;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.newsfeed, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyc);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        data = new ArrayList<>();
        show_newsfeed();
        recyclerView.setHasFixedSize(true);
    }

    private void show_newsfeed()
    {
        Map<String,String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if(Wschool.sharedPreferences.getString("login", "0").equals("guardian"))
        {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "newsfeeds";
        new Volley_load(getActivity(), Newsfeed.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {

                int len = s.length();
                if(len > 0){
                    for(int i = 0; i < s.length(); i++)
                    {
                        try
                        {
                            Newsfd_POJO pojo = new Newsfd_POJO();
                            JSONObject jo1 = s.getJSONObject(i);
                            pojo.setId(jo1.getString("newsfeedsid"));
                            pojo.setTitle(jo1.getString("newsfeeds_title"));
                            pojo.setDescrp(jo1.getString("newsfeeds_description"));
                            pojo.setImg_url(jo1.getString("newsfeeds_image"));
                            pojo.setDate(jo1.getString("newsfeeds_date"));
                            data.add(pojo);
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    recyclerView.setAdapter(new Newsfeed_Adp(data, new Newsfeed_Adp.Onitemclicklistner() {
                        @Override
                        public void onItemClick(Newsfd_POJO pojo) {

                            Bundle bundle = new Bundle();
                            bundle.putString("newsid", pojo.getId());
                            Fragment frag = new Newsfeed_detail();
                            frag.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frame_layout, frag).addToBackStack("nfd").commit();
                        }
                    }));
                }else{
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
