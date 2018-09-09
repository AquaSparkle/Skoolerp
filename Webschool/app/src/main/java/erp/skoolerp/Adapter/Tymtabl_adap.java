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

import erp.skoolerp.Pojo.Tymtabl_Pojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 19/06/17.
 */

public class Tymtabl_adap extends BaseAdapter
{
    Context context;
    ArrayList<Tymtabl_Pojo> data;
    private static LayoutInflater inflater=null;
    boolean grn = true;

    public Tymtabl_adap(Context context, ArrayList<Tymtabl_Pojo> data)
    {
        this.context = context;
        this.data = data;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView sub, sub_code, count, staff, from, to;
        LinearLayout green_lay, bottom_lay;
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
    public View getView(int pos, View view, ViewGroup viewGroup)
    {
        Holder holder = new Holder();

        view = inflater.inflate(R.layout.tymtab_child, null);
        holder.count = (TextView) view.findViewById(R.id.count);
        holder.sub_code = (TextView) view.findViewById(R.id.subcode);
        holder.sub = (TextView) view.findViewById(R.id.sub);
        holder.staff = (TextView) view.findViewById(R.id.staff);
        holder.from = (TextView) view.findViewById(R.id.from);
        holder.to = (TextView) view.findViewById(R.id.to);
        holder.green_lay = (LinearLayout) view.findViewById(R.id.green_lay);
        holder.bottom_lay = (LinearLayout) view.findViewById(R.id.bottom_lay);

        holder.count.setText(data.get(pos).getCode());
        holder.sub_code.setText(data.get(pos).getSub_code());
        holder.sub.setText(data.get(pos).getSub());
        holder.staff.setText(data.get(pos).getStaff());
        holder.from.setText(data.get(pos).getStart_tym());
        holder.to.setText(data.get(pos).getEnd_tym());

        if(grn)
        {
            holder.green_lay.setBackgroundColor(Color.parseColor("#27ae60"));
            grn = false;
        }else
        {
            holder.green_lay.setBackgroundColor(Color.parseColor("#158b48"));
            grn = true;
        }

        if(pos == (data.size()-1))
        {
            holder.bottom_lay.setVisibility(View.VISIBLE);
        }else
        {
            holder.bottom_lay.setVisibility(View.GONE);
        }

        return view;
    }
}


