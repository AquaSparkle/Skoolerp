package erp.skoolerp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Route_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 22/07/17.
 */

public class Route_adap extends RecyclerView.Adapter<Route_adap.Viewholder> {

    Onitemclicklistner onitemclicklistner;
    Context context;
    ArrayList<Route_pojo> data;
    private static final int      CALL_PHONE_CONSTANT = 100;

    public Route_adap(Context context, ArrayList<Route_pojo> data, Onitemclicklistner onitemclicklistner) {
        this.context = context;
        this.onitemclicklistner = onitemclicklistner;
        this.data = data;
    }

    public interface Onitemclicklistner {
        void onItemClick(Route_pojo pojo);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView code, veh_nmbr, pick_tym, dest;
        Button contact;

        public Viewholder(View itemView) {
            super(itemView);
            code = (TextView) itemView.findViewById(R.id.route_code);
            veh_nmbr = (TextView) itemView.findViewById(R.id.veh_no);
            pick_tym = (TextView) itemView.findViewById(R.id.pick_tym);
            dest = (TextView) itemView.findViewById(R.id.dest);
            contact = (Button) itemView.findViewById(R.id.contact);
            contact.setTypeface(Wschool.tf2);
        }

        public void bind(final Route_pojo pojo, final Onitemclicklistner listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pojo);
                }
            });
        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route, parent, false);
        return new Route_adap.Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        holder.bind(data.get(position), onitemclicklistner);
        holder.code.setText(data.get(position).getRoute_code());
        holder.veh_nmbr.setText(data.get(position).getVeh_number());
        holder.pick_tym.setText(data.get(position).getPick_tym());
        holder.dest.setText(data.get(position).getDestination());

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + data.get(position).getContact()));

                if(!checkPermission()){
                    Toast.makeText(context, "Phone permission allows to make a call. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_CONSTANT);

                }else{
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


//    private void askforpermisn() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
//            //Show Information about why you need the permission
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Need Phone Permission");
//            builder.setMessage("This app needs phone permission.");
//            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_CONSTANT);
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            builder.show();
//        }
//    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

}
