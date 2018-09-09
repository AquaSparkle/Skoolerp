package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import erp.skoolerp.Adapter.Fee_expandadap;
import erp.skoolerp.Pojo.Fee_pojo;
import erp.skoolerp.Pojo.Pay_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;
import erp.skoolerp.utils.Volley_load;

/**
 * Created by shalu on 20/06/17.
 */

public class Fees extends Fragment
{
    View view;
    ExpandableListView fee_list;
    Fee_expandadap listAdapter;
    List<String> listDataHeader, id_array;
    HashMap<String, List<Fee_pojo>> listDataChild;
    Button pay;
    Fees frag_obj;
    String amount, mode, act_amt, fine, cat_id;
    String TAG = "Fees";
    HashMap<Integer, List<Integer>> tick_content;
    int count = 0;
    ArrayList<Pay_pojo> pay_data;
    int total_amount, total_fine, total_pay;
    JSONArray jsonArray;
    String pay_string;
    boolean payON = true;
    private boolean shouldRefreshOnResume = false;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fees, container, false);

        fee_list = (ExpandableListView) view.findViewById(R.id.fee_list);
        pay = (Button) view.findViewById(R.id.pay);
        pay.setTypeface(Wschool.tf3);
        frag_obj = this;
        prepareListData();
        tick_content = new HashMap<Integer, List<Integer>>();
        total_amount = total_fine = total_pay = 0;
        pay.setVisibility(View.GONE);
        jsonArray = new JSONArray();

        get_pay_details();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAmount();
                pay_string = jsonArray.toString();
                total_pay = total_amount + total_fine;
                send_pay_data();

            }
        });

        fee_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group_position, int child_position, long l) {

                Fee_pojo saved_data = listDataChild.get(listDataHeader.get(group_position)).get(child_position);

                boolean paid = saved_data.isPaid();
                if(!paid){
                    if(saved_data.isChecked()){
                        saved_data.setChecked(false);
                        count--;
                        if(count == 0)
                            pay.setVisibility(View.GONE);
                    }else{
                        saved_data.setChecked(true);
                        count++;
                        if(payON)
                            pay.setVisibility(View.VISIBLE);
                    }
                    getAmount();
                    if(total_pay > 0){
                        pay.setText("Pay now (Rs "+total_pay+")");
                    }
                    listDataChild.get(listDataHeader.get(group_position)).set(child_position, saved_data);
                    listAdapter.notify_data(listDataChild);
                }
                return true;
            }
        });
        return view;
    }

    private void getAmount() {
        jsonArray = new JSONArray();
        total_amount = total_fine = total_pay = 0;
        int group_count = listDataHeader.size();
        for(int i = 0; i<group_count; i++){

            int amount = 0;
            int fine = 0;
            List<Fee_pojo> child_data = listDataChild.get(listDataHeader.get(i));
            int child_count = child_data.size();
            for(int j = 0; j<child_count; j++){
                Fee_pojo pojo = child_data.get(j);
                if(pojo.isChecked()){

                    int sub_amt = pojo.getAmt();
                    int sub_fine = pojo.getFine();
                    amount = amount + sub_amt;
                    fine = fine + sub_fine;
                }
            }

            if(amount > 0){
                JSONObject table = new JSONObject();
                try {

                    table.put("feessubcategoryid", id_array.get(i));
                    table.put("amount", String.valueOf(amount));
                    table.put("fine", String.valueOf(fine));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(table);
                total_amount = total_amount + amount;
                total_fine = total_fine + fine;
            }
        }
        total_pay = total_amount + total_fine;
    }

    private void get_pay_details() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL+"onlinepaymentdetails",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            String status = jo.getString("sts");
                            if(status.equals("1")) {
                                JSONObject joo = jo.getJSONObject("rlt");

                                if(joo.getString("api_key").equals(""))
                                {
                                    payON = false;
                                }else{
                                    payON = true;
                                    Wschool.api_link = joo.getString("api_key");
                                    Wschool.req_link = joo.getString("payment_request_link");
                                    Wschool.salt = joo.getString("salt");
                                }

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
                        Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
                if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
                    params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
                }
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void send_pay_data() {

        Random rand = new Random();
        final int orderid = rand.nextInt(100000);

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        System.out.println("orderid__ "+String.valueOf(orderid) );
        params.put("order_id", String.valueOf(orderid));
        params.put("sendarray", pay_string);


        String url = "saveonlinepaymentdetails";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Wschool.base_URL + url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jo = new JSONObject(response);
                            System.out.println("Volley_Resp__ "+response );
                            String status = jo.getString("sts");
                            if (status.equals("1"))
                            {
                                Toast.makeText(getActivity(), "Transaction data is saved.", Toast.LENGTH_LONG).show();
                                mode = "LIVE";

                                Log.d(TAG, "Order Id: " + orderid);
                                Log.d(TAG, "Amount: " + total_pay);
                                Log.d(TAG, "Mode: " + mode);

                                Intent intent = new Intent(getActivity(), PayuMoneyRequest.class);
                                intent.putExtra("order_id", String.valueOf(orderid));
                                intent.putExtra("amount", String.valueOf(total_pay));
                                intent.putExtra("mode", "LIVE");
                                startActivity(intent);

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
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();
        id_array = new ArrayList<>();
        listDataChild = new HashMap<String, List<Fee_pojo>>();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Wschool.sharedPreferences.getString("userid", "0"));
        if (Wschool.sharedPreferences.getString("login", "0").equals("guardian")) {
            params.put("studentid", Wschool.sharedPreferences.getString("studentid", "0"));
        }
        String url = "feedetails";
        new Volley_load(getActivity(), Fees.this, url, params, new Volley_load.Contents() {
            @Override
            public void returndata(JSONArray s)
            {

                int leng = s.length();
                if(leng > 0){
                    for (int i = 0; i < leng; i++)
                    {
                        try
                        {
                            JSONObject jo1 = s.getJSONObject(i);
                            listDataHeader.add(jo1.getString("feessubcategory_name"));
                            id_array.add(jo1.getString("feessubcategoryid"));

                            JSONArray ja = jo1.getJSONArray("table");
                            ArrayList<Fee_pojo> data, data_new;
                            data = new ArrayList<>();
                            data_new = new ArrayList<>();

                            int len = ja.length();
                            for (int j = 0; j < len; j++)
                            {
                                try
                                {
                                    final JSONObject joo = ja.getJSONObject(j);
                                    Fee_pojo pojo = new Fee_pojo();
                                    pojo.setTitle(joo.getString("frequency"));
                                    int amount, fine, total;

                                    amount = joo.getInt("amount");
                                    fine = joo.getInt("fine");
                                    total = amount + fine;

                                    pojo.setAmount("Amount: "+ total);
                                    pojo.setDate(joo.getString("due_date"));
                                    pojo.setAmt(joo.getInt("amount"));
                                    pojo.setFine(joo.getInt("fine"));

                                    switch (joo.getString("due_month")){

                                        case "January":
                                            pojo.setMonth("Jan - "+joo.getString("due_year"));
                                            break;

                                        case "February":
                                            pojo.setMonth("Feb - "+joo.getString("due_year"));
                                            break;

                                        case "March":
                                            pojo.setMonth("Mar - "+joo.getString("due_year"));
                                            break;

                                        case "April":
                                            pojo.setMonth("Apr - "+joo.getString("due_year"));
                                            break;

                                        case "May":
                                            pojo.setMonth("May - "+joo.getString("due_year"));
                                            break;

                                        case "June":
                                            pojo.setMonth("Jun - "+joo.getString("due_year"));
                                            break;

                                        case "July":
                                            pojo.setMonth("Jul - "+joo.getString("due_year"));
                                            break;

                                        case "August":
                                            pojo.setMonth("Aug - "+joo.getString("due_year"));
                                            break;

                                        case "September":
                                            pojo.setMonth("Sep - "+joo.getString("due_year"));
                                            break;

                                        case "October":
                                            pojo.setMonth("Oct - "+joo.getString("due_year"));
                                            break;

                                        case "November":
                                            pojo.setMonth("Nov - "+joo.getString("due_year"));
                                            break;

                                        case "December":
                                            pojo.setMonth("Dec - "+joo.getString("due_year"));
                                            break;
                                    }

                                    if(joo.getString("status").equals("1")){

                                        pojo.setPay("Paid on: "+joo.getString("paid_date"));
                                        pojo.setPaid(true);
                                    }else{

                                        pojo.setPay("Unpaid");
                                        pojo.setPaid(false);
                                    }
                                    data.add(pojo);
                                }catch (Exception e)
                                {

                                }
                            }

                            data_new = data;
                            Log.d("Size...", String.valueOf(len));
                            listDataChild.put(listDataHeader.get(i), data_new);
                            Log.d("Header...", listDataHeader.get(i));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    listAdapter = new Fee_expandadap(getActivity(), listDataHeader, listDataChild, frag_obj, id_array);

                    // setting list adapter
                    fee_list.setAdapter(listAdapter);
                }else{
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        // Check should we need to refresh the fragment
//        if(shouldRefreshOnResume){
//            // refresh fragment
//            Fragment frag_name = new Fees();
//            FragmentManager manager = getFragmentManager();
//            manager.beginTransaction().replace(R.id.frame_layout, frag_name).addToBackStack("").commit();
//            getActivity().getFragmentManager().popBackStack();
////
////            Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();
////            prepareListData();
//            pay.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        Toast.makeText(getActivity(), "Stop called", Toast.LENGTH_SHORT).show();
//        shouldRefreshOnResume = true;
//    }
}
