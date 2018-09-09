package erp.skoolerp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Stdlist_pojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 16/09/17.
 */

public class StudentDetails_adap extends RecyclerView.Adapter<StudentDetails_adap.Holder> {

    Context context;
    ArrayList<Stdlist_pojo> data;
    private static LayoutInflater inflater=null;

    public StudentDetails_adap(Context context, ArrayList<Stdlist_pojo> data)
    {
        this.context = context;
        this.data = data;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView numbr, name, admn;

        public Holder(View itemView) {
            super(itemView);
            numbr = (TextView) itemView.findViewById(R.id.numbr);
            name = (TextView) itemView.findViewById(R.id.name);
            admn = (TextView) itemView.findViewById(R.id.admn);

        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.studntdetails_child, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.numbr.setText(String.valueOf(position+1));
        holder.name.setText(data.get(position).getName());
        holder.admn.setText(data.get(position).getAdm_no());

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void notifyData(ArrayList<Stdlist_pojo> dataq)
    {
        this.data = dataq;
        notifyDataSetChanged();
    }
}
