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

import erp.skoolerp.Pojo.Newsfd_POJO;
import erp.skoolerp.R;

/**
 * Created by shalu on 15/06/17.
 */

public class Newsfeed_Adp extends RecyclerView.Adapter<Newsfeed_Adp.Viewholder>
{
    Onitemclicklistner onitemclicklistner;
    ArrayList<Newsfd_POJO> data;

    public interface Onitemclicklistner
    {
        void onItemClick(Newsfd_POJO pojo);
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView title, descrp, date;
        ImageView image;

        public Viewholder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            descrp = (TextView) itemView.findViewById(R.id.descrp);
            date = (TextView) itemView.findViewById(R.id.date);
            image = (ImageView) itemView.findViewById(R.id.feedimg);
        }

        public void bind(final Newsfd_POJO pojo, final Onitemclicklistner listener)
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

    public Newsfeed_Adp(ArrayList<Newsfd_POJO> data, Onitemclicklistner onitemclicklistner)
    {
        this.onitemclicklistner = onitemclicklistner;
        this.data = data;
    }

    @Override
    public Newsfeed_Adp.Viewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsfeed_child, parent, false);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Newsfeed_Adp.Viewholder holder, int position)
    {
        Context context = holder.itemView.getContext();
        holder.bind(data.get(position), onitemclicklistner);
        holder.title.setText(data.get(position).getTitle());
        holder.descrp.setText(data.get(position).getDescrp());
        holder.date.setText(data.get(position).getDate());

        Picasso.with(context).
                load(data.get(position).getImg_url()).resize(100, 100).error(R.drawable.placelder).
                into(holder.image);

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
