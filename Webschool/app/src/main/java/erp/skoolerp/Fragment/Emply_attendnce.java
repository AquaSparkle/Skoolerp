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
 * Created by shalu on 28/06/17.
 */

public class Emply_attendnce extends Fragment
{
    View view;
    static TextView std_list;
    static TextView details;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.emply_attnd, container, false);

        details = (TextView) view.findViewById(R.id.details);
        std_list = (TextView) view.findViewById(R.id.std_list);

        details.setBackgroundColor(Color.parseColor("#1a6fa7"));
        details.setTextColor(Color.parseColor("#729fbd"));
        std_list.setBackgroundColor(Color.parseColor("#3498db"));
        std_list.setTextColor(Color.parseColor("#ffffff"));

        Fragment frag = new Students_list();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();

        std_list.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                details.setBackgroundColor(Color.parseColor("#1a6fa7"));
                details.setTextColor(Color.parseColor("#729fbd"));
                std_list.setBackgroundColor(Color.parseColor("#3498db"));
                std_list.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Students_list();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });

        details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                std_list.setBackgroundColor(Color.parseColor("#1a6fa7"));
                std_list.setTextColor(Color.parseColor("#729fbd"));
                details.setBackgroundColor(Color.parseColor("#3498db"));
                details.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Std_attnd_details();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });

        return view;
    }
}
