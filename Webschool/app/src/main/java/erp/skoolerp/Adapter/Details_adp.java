package erp.skoolerp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Details_POJO;
import erp.skoolerp.R;

/**
 * Created by shalu on 21/06/17.
 */

public class Details_adp extends BaseAdapter
{
    Context context;
    ArrayList<Details_POJO> data;
    private static LayoutInflater inflater=null;

    public Details_adp(Context context, ArrayList<Details_POJO> data)
    {
        this.context = context;
        this.data = data;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView isbn, book_name, issue_date, status, book_req;
        LinearLayout first_lay;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int i)
    {
        return i;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Holder holder = new Holder();

        view = inflater.inflate(R.layout.details_child, null);
        holder.isbn = (TextView) view.findViewById(R.id.isbn);
        holder.book_name = (TextView) view.findViewById(R.id.book_name);
        holder.issue_date = (TextView) view.findViewById(R.id.issue_date);
        holder.status = (TextView) view.findViewById(R.id.status);
        holder.first_lay = (LinearLayout) view.findViewById(R.id.first_lay);

        holder.isbn.setText("ISBN Book no: "+data.get(i).getIsbn());
        holder.book_name.setText("Book name: "+data.get(i).getBook_name());

        String status = data.get(i).getStatus();
        switch (status){
            case "1":
                holder.status.setText("Not returned");
                holder.status.setTextColor(Color.BLACK);
                holder.status.setBackgroundColor(Color.WHITE);
                holder.issue_date.setText("Issued date: "+data.get(i).getIssu_date());
                break;

            case "2":
                holder.status.setText("Returned");
                holder.status.setTextColor(Color.BLACK);
                holder.status.setBackgroundColor(Color.WHITE);
                holder.issue_date.setText("Returned date: "+data.get(i).getRetn_date());
                break;

            case "3":
                holder.status.setText("Requested");
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundColor(Color.parseColor("#3498db"));  // blue
                holder.issue_date.setText("Requested date: "+data.get(i).getReq_date());
                break;

            case "4":
                holder.status.setText("Accepted");
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundColor(Color.parseColor("#33A245"));  // green
                holder.issue_date.setText("Requested date: "+data.get(i).getReq_date());
                break;

            case "5":
                holder.status.setText("Rejected");
                holder.status.setTextColor(Color.WHITE);
                holder.status.setBackgroundColor(Color.parseColor("#F5281A"));  // red
                holder.issue_date.setText("Requested date: "+data.get(i).getReq_date());
                break;
        }

        return view;
    }
}
