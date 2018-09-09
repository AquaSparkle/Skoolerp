package erp.skoolerp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Attendance_pojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 19/06/17.
 */

public class Attend_adap extends RecyclerView.Adapter<Attend_adap.viewholder>
{

    Onitemclicklistner onitemclicklistner;
    Context context;
    ArrayList<Attendance_pojo> data;

    public interface Onitemclicklistner
    {
        void onItemClick(Attendance_pojo pojo);
    }

    public class viewholder extends RecyclerView.ViewHolder
    {
        TextView date, month, reason;
        LinearLayout layout;

        public viewholder(View itemView)
        {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            month = (TextView) itemView.findViewById(R.id.mnth);
            reason = (TextView) itemView.findViewById(R.id.resn);
            layout = (LinearLayout) itemView.findViewById(R.id.lay_bg);
        }

        public void bind(final Attendance_pojo pojo, final Attend_adap.Onitemclicklistner listener)
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

    public Attend_adap(Context context, ArrayList<Attendance_pojo> data, Onitemclicklistner onitemclicklistner)
    {
        this.context = context;
        this.onitemclicklistner = onitemclicklistner;
        this.data = data;
    }

    @Override
    public Attend_adap.viewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attnd_child, parent, false);
        return new Attend_adap.viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Attend_adap.viewholder holder, int position)
    {
        holder.date.setText(data.get(position).getDate());
        holder.month.setText(data.get(position).getMonth());
        holder.reason.setText(data.get(position).getReason());
        holder.bind(data.get(position), onitemclicklistner);

        if(data.get(position).isAbsent()){
            holder.layout.setBackgroundColor(Color.parseColor("#F5281A"));
        }else{
            holder.layout.setBackgroundColor(Color.parseColor("#3182D9"));
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
