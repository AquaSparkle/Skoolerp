package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import erp.skoolerp.R;

/**
 * Created by shalu on 15/06/17.
 */

public class Exams extends Fragment
{
    View view;
    TextView exmlist, result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.exam, container, false);

        exmlist = (TextView) view.findViewById(R.id.exmlist);
        result = (TextView) view.findViewById(R.id.result);

        result.setBackgroundColor(Color.parseColor("#1a6fa7"));
        result.setTextColor(Color.parseColor("#729fbd"));
        exmlist.setBackgroundColor(Color.parseColor("#3498db"));
        exmlist.setTextColor(Color.parseColor("#ffffff"));

        Fragment frag = new Exm_list();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();

        exmlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                result.setBackgroundColor(Color.parseColor("#1a6fa7"));
                result.setTextColor(Color.parseColor("#729fbd"));
                exmlist.setBackgroundColor(Color.parseColor("#3498db"));
                exmlist.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Exm_list();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });

        result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                exmlist.setBackgroundColor(Color.parseColor("#1a6fa7"));
                exmlist.setTextColor(Color.parseColor("#729fbd"));
                result.setBackgroundColor(Color.parseColor("#3498db"));
                result.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Results();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });
        return view;
    }
}
