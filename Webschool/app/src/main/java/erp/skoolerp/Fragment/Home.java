package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import erp.skoolerp.Pojo.Horizontal_pojo;
import erp.skoolerp.Pojo.Newsfd_POJO;
import erp.skoolerp.R;
import erp.skoolerp.Studentactivity;

/**
 * Created by shalu on 14/06/17.
 */

public class Home extends Fragment
{
    View view;
    ArrayList<Horizontal_pojo> scroll_data;
    //RecyclerView recyclerView, hori_recyc;
    Newsfd_POJO pojo;
    ArrayList<Newsfd_POJO> data;
    FrameLayout frameLayout;
    final int[] ht = new int[1];
    RelativeLayout attend, tymtable, assgn, fees, exm, lib, trans, event;
    HorizontalScrollView hscr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.home, container, false);

        tymtable = (RelativeLayout) view.findViewById(R.id.tymtable);
        assgn = (RelativeLayout) view.findViewById(R.id.assgn);
        fees = (RelativeLayout) view.findViewById(R.id.fees);
        exm = (RelativeLayout) view.findViewById(R.id.exm);
        lib = (RelativeLayout) view.findViewById(R.id.lib);
        trans = (RelativeLayout) view.findViewById(R.id.trans);
        event = (RelativeLayout) view.findViewById(R.id.event);
        attend = (RelativeLayout) view.findViewById(R.id.attend);
        hscr = (HorizontalScrollView) view.findViewById(R.id.hscr);
        hscr.setFocusable(false);

        attend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(1);
            }
        });

        tymtable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(2);
            }
        });

        assgn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(3);
            }
        });

        fees.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(4);
            }
        });

        exm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(5);
            }
        });

        lib.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(6);
            }
        });

        trans.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(7);
            }
        });

        event.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Studentactivity) getActivity()).selectFrag(8);
            }
        });

        Fragment fra2 = new Newsfeed();
        FragmentManager fragmentManager2 = getActivity().getFragmentManager();
        fragmentManager2.beginTransaction().replace(R.id.new_lay, fra2).commit();

        scroll_data = new ArrayList<>();

        return view;
    }
}
