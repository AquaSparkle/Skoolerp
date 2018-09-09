package erp.skoolerp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import erp.skoolerp.R;

/**
 * Created by shalu on 20/06/17.
 */

public class Subjectlist_adap extends BaseAdapter
{
    Context context;
    ArrayList<String> subject;
    ArrayList<String> sub_code;
    private static LayoutInflater inflater=null;

    public Subjectlist_adap(Context context, ArrayList<String> subject, ArrayList<String> sub_code)
    {
        this.context = context;
        this.subject = subject;
        this.sub_code = sub_code;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView subj, subj_code;
    }

    @Override
    public int getCount()
    {
        return subject.size();
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

        view = inflater.inflate(R.layout.sublist_child, null);
        holder.subj = (TextView) view.findViewById(R.id.sub);
        holder.subj_code = (TextView) view.findViewById(R.id.sub_code);

        holder.subj.setText(subject.get(i));
        holder.subj_code.setText(sub_code.get(i));

        return view;
    }
}
