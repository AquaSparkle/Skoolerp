package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import erp.skoolerp.Adapter.Details_adp;
import erp.skoolerp.Pojo.Details_POJO;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 21/06/17.
 */

public class Issue_details extends Fragment
{
    View view;
    ListView listView;
    ArrayList<Details_POJO> data;
    Details_POJO pojo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.issue_details, container, false);

        data = new ArrayList<Details_POJO>();

        issued_display();
        return view;
    }

    private void issued_display() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "bookissuedetails";
        new Volley_load(getActivity(), Issue_details.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s) {

                int len = s.length();
                if(len > 0){
                    for (int i = 0; i < s.length(); i++) {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            pojo = new Details_POJO();
                            pojo.setBook_name(jo1.getString("bookname"));
                            pojo.setIsbn(jo1.getString("isbnno"));
                            pojo.setIssu_date(jo1.getString("issudate"));
                            pojo.setReq_date(jo1.getString("requesteddate"));
                            pojo.setDue_date(jo1.getString("returndate"));

                            String book_status = jo1.getString("book_status");
                            String status = jo1.getString("status");
                            switch (book_status){
                                case "1"://isse

                                    if(status.equals("notreturn")){
                                        pojo.setB_status("Not returned");
                                        pojo.setStatus("1");
                                    }else {
                                        pojo.setB_status("Returned");
                                        pojo.setStatus("2");
                                    }

                                    break;

                                case "2"://req
                                    pojo.setB_status("Requested");
                                    pojo.setStatus("3");
                                    break;

                                case "3":// acc
                                    pojo.setB_status("Accepted");
                                    pojo.setStatus("4");
                                    break;

                                case "4":// rejec
                                    pojo.setB_status("Rejected");
                                    pojo.setStatus("5");
                                    break;
                            }
//                            if(status.equals("")){
//                                pojo.setBook_req(false);
//                            }else{
//                                pojo.setBook_req(true);
//                            }
                            data.add(pojo);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    listView = (ListView) view.findViewById(R.id.detail_list);
                    listView.setAdapter(new Details_adp(getActivity(), data));
                }else{
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
