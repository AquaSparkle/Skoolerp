package erp.skoolerp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Std_selpojo;
import erp.skoolerp.R;
import erp.skoolerp.Studentactivity;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 09/06/17.
 */

public class Std_selectnadap extends PagerAdapter
{
    private Context context;
    ArrayList<Std_selpojo> child_data;
    TextView name, course;
    ImageView pic;
    LinearLayout main_viewlay;

    public Std_selectnadap(Context context, ArrayList<Std_selpojo> child_data)
    {
        this.context = context;
        this.child_data = child_data;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.selection_viewpagerchild, collection, false);


        name = (TextView) layout.findViewById(R.id.name);
        course = (TextView) layout.findViewById(R.id.course);
        pic = (ImageView) layout.findViewById(R.id.pic);
        main_viewlay = (LinearLayout) layout.findViewById(R.id.main_viewlay);

        name.setText(child_data.get(position).getName());
        course.setText(child_data.get(position).getCourse()+" - "+child_data.get(position).getBatch());
        Picasso.with(context).load(child_data.get(position).getImg_url()).error(R.drawable.dummy).into(pic);
        main_viewlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Wschool.name = child_data.get(position).getName();
                Wschool.editor.putString("studentid", child_data.get(position).getStd_id());
                Wschool.editor.commit();

                Intent i = new Intent(context, Studentactivity.class);
                context.startActivity(i);
                ((Activity)context).finish();
            }
        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount()
    {
        return child_data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return null;
    }

}

