package erp.skoolerp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import erp.skoolerp.R;

/**
 * Created by shalu on 11/06/17.
 */

public class Navlistadap extends BaseAdapter
{
    Context context;
    String[] list;
    int[] images;
    View view;
    int id;
    Holder holder;
    private static LayoutInflater inflater=null;

    public Navlistadap(Context context, String[] list, int[] images)
    {
      this.context = context;
      this.list = list;
      this.images = images;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        LinearLayout line;
    }
    @Override
    public int getCount()
    {
        return (list.length);
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
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        holder = new Holder();
            view = inflater.inflate(R.layout.nav_child, null);
            holder.tv = (TextView) view.findViewById(R.id.nav_stext);
            holder.img = (ImageView) view.findViewById(R.id.nav_smicon);
            holder.line = (LinearLayout) view.findViewById(R.id.line);


        holder.tv.setText(list[i]);
        holder.img.setImageResource(images[i]);

        return view;
    }
}
