package erp.skoolerp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Circ_pojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 21/06/17.
 */

public class Event_adap extends BaseAdapter
{
    Context context;
    ArrayList<Circ_pojo> data;
    private static LayoutInflater inflater=null;

    public Event_adap(Context context, ArrayList<Circ_pojo> data)
    {
        this.context = context;
        this.data = data;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView id, title, descrp, date;
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

        view = inflater.inflate(R.layout.events_child, null);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.descrp = (TextView) view.findViewById(R.id.descrp);
        holder.date = (TextView) view.findViewById(R.id.date);

        holder.title.setText(data.get(i).getTitle());
        holder.descrp.setText(data.get(i).getDescrp());
        holder.date.setText(data.get(i).getDate());

        return view;
    }
}

