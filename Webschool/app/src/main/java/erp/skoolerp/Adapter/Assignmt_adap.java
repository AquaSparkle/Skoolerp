package erp.skoolerp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import erp.skoolerp.Pojo.Assignmnt_Pojo;
import erp.skoolerp.R;
import erp.skoolerp.utils.InputStreamVolleyRequest;


/**
 * Created by shalu on 23/06/17.
 */

public class Assignmt_adap extends BaseAdapter
{
    Context context;
    ArrayList<Assignmnt_Pojo> data;
    private static LayoutInflater inflater=null;
    String filename;
    long total = 0;
    private Dialog dialog;
    Fragment frag;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    public Assignmt_adap(Context context, ArrayList<Assignmnt_Pojo> data, Fragment frag)
    {
        this.context = context;
        this.data = data;
        this.frag = frag;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        TextView subj_code, title, dtext, descrp, submn_date;
        ImageView lib_img;
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
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        Holder holder = new Holder();
        view = inflater.inflate(R.layout.assgn_child, null);
        holder.subj_code = (TextView) view.findViewById(R.id.sub_code);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.descrp = (TextView) view.findViewById(R.id.descrp);
        holder.submn_date = (TextView) view.findViewById(R.id.date);
        holder.dtext = (TextView) view.findViewById(R.id.dtext);
        holder.lib_img = (ImageView) view.findViewById(R.id.lib_img);


        holder.subj_code.setText(data.get(i).getSubj_code());
        holder.title.setText(data.get(i).getTitle());
        holder.descrp.setText(data.get(i).getDescrp());
        holder.submn_date.setText("Date of submission: "+data.get(i).getDate());


        String link = data.get(i).getLink();
        if(link.equals("NIL")){
            holder.lib_img.setVisibility(View.GONE);
            holder.dtext.setVisibility(View.GONE);
        }else{

            holder.lib_img.setVisibility(View.VISIBLE);
            holder.dtext.setVisibility(View.VISIBLE);
            holder.lib_img.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(!checkPermission()){
                        Toast.makeText(context, "Storage permission allows to download assignment. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                    }else{

                        filename = data.get(i).getFilename();
                        dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.progressdialog);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        download_now(data.get(i).getLink());
                    }
                }
            });
        }

        return view;
    }

    private void download_now(String url) {

        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response!=null) {


//                                FileOutputStream outputStream;
//                                String name="assignment";
//                                outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
//                                outputStream.write(response);
//                                outputStream.close();
                                File folder = new File(Environment.getExternalStorageDirectory() + "/Webschool");
                                boolean success = true;
                                if (!folder.exists()) {
                                    success = folder.mkdir();
                                }
                                if (success) {
                                    // Do something on success
                                    File newone = new File(folder, filename);

                                    if(!newone.exists())
                                    {

                                        FileOutputStream fos = new FileOutputStream(newone);
                                        fos.write(response);
                                        fos.close();
                                        dialog.dismiss();
                                        String filepath = "Document downloaded in storage/Webschool/"+filename;
                                        Toast.makeText(context, "Download complete. Document downloaded in storage/Webschool/"+filename, Toast.LENGTH_LONG).show();
                                    }else
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(context, "File is already saved.", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    // Do something else on failure
                                    dialog.dismiss();
                                }

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            dialog.dismiss();
                            Toast.makeText(context, "UNABLE TO DOWNLOAD FILE", Toast.LENGTH_LONG).show();
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }

                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                dialog.dismiss();
                Toast.makeText(context, "UNABLE TO DOWNLOAD FILE", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }

        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
        mRequestQueue.add(request);
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
}
