package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import erp.skoolerp.R;
import erp.skoolerp.Employactivity;

/**
 * Created by shalu on 28/06/17.
 */

public class Home_emply extends Fragment
{
    View view;
    RecyclerView recyclerView;
    RelativeLayout attend, tymtable, assgn, salry, leav, lib, trans;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.hme_emply, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyc);
        tymtable = (RelativeLayout) view.findViewById(R.id.tymtable);
        assgn = (RelativeLayout) view.findViewById(R.id.assgn);
        salry = (RelativeLayout) view.findViewById(R.id.salry);
        leav = (RelativeLayout) view.findViewById(R.id.leav);
        lib = (RelativeLayout) view.findViewById(R.id.lib);
        trans = (RelativeLayout) view.findViewById(R.id.trans);
        attend = (RelativeLayout) view.findViewById(R.id.attend);

        attend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(0);
            }
        });

        tymtable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(1);
            }
        });

        assgn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(2);
            }
        });

        salry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(3);
            }
        });

        leav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(4);
            }
        });

        lib.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(5);
            }
        });

        trans.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((Employactivity) getActivity()).selectFrag(6);
            }
        });

        Fragment fra2 = new Newsfeed();
        FragmentManager fragmentManager2 = getActivity().getFragmentManager();
        fragmentManager2.beginTransaction().replace(R.id.new_lay, fra2).commit();
        return view;
    }
}
