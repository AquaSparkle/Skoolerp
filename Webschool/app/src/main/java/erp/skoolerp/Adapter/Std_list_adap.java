package erp.skoolerp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Stdlist_pojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 29/06/17.
 */

public class Std_list_adap extends RecyclerView.Adapter<Std_list_adap.viewholder>
{
    Context context;
    ArrayList<Stdlist_pojo> data;
    onitemclicklistner onitemclicklistner;

    public interface onitemclicklistner
    {
        void onItemClick(Stdlist_pojo pojo);
    }

    public Std_list_adap(Context context, ArrayList<Stdlist_pojo> data, onitemclicklistner onitemclicklistner)
    {
        this.context = context;
        this.onitemclicklistner = onitemclicklistner;
        this.data = data;
    }

    public class viewholder extends RecyclerView.ViewHolder
    {
        TextView name, roll_no, adm_no;
        ImageView pic, tick;

        public viewholder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.std_name);
            roll_no = (TextView) itemView.findViewById(R.id.roll_no);
            adm_no = (TextView) itemView.findViewById(R.id.adm_no);
            pic = (ImageView) itemView.findViewById(R.id.std_pic_img);
            tick = (ImageView) itemView.findViewById(R.id.std_tick);
        }

        public void bind(final Stdlist_pojo pojo, final onitemclicklistner listener)
        {
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClick(pojo);
                }
            });
        }
    }

    @Override
    public Std_list_adap.viewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stdlist_child, parent, false);
        return new Std_list_adap.viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(final Std_list_adap.viewholder holder, int position)
    {
        final Stdlist_pojo pojo = data.get(position);

        holder.bind(pojo, onitemclicklistner);
        if(pojo.isChecked())
        {
            holder.tick.setImageResource(R.drawable.checkbox_checked);
        }else
        {
            holder.tick.setImageResource(R.drawable.checkbox);
        }

        holder.name.setText(data.get(position).getName());
        holder.adm_no.setText(data.get(position).getAdm_no());
        holder.roll_no.setText(data.get(position).getRoll_no());
        Picasso.with(context).load(data.get(position).getImg_url()).error(R.drawable.dummy).into(holder.pic);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public void notifychange(ArrayList<Stdlist_pojo> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }
}
