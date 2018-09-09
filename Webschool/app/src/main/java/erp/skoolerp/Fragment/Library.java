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
 * Created by shalu on 21/06/17.
 */

public class Library extends Fragment
{
    View view;
    TextView bukreq, details;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.library, container, false);

        bukreq = (TextView) view.findViewById(R.id.bookreq);
        details = (TextView) view.findViewById(R.id.issue_det);

        details.setBackgroundColor(Color.parseColor("#1a6fa7"));
        details.setTextColor(Color.parseColor("#729fbd"));
        bukreq.setBackgroundColor(Color.parseColor("#3498db"));
        bukreq.setTextColor(Color.parseColor("#ffffff"));

        Fragment frag = new Book_request();
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();

        bukreq.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                details.setBackgroundColor(Color.parseColor("#1a6fa7"));
                details.setTextColor(Color.parseColor("#729fbd"));
                bukreq.setBackgroundColor(Color.parseColor("#3498db"));
                bukreq.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Book_request();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });

        details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bukreq.setBackgroundColor(Color.parseColor("#1a6fa7"));
                bukreq.setTextColor(Color.parseColor("#729fbd"));
                details.setBackgroundColor(Color.parseColor("#3498db"));
                details.setTextColor(Color.parseColor("#ffffff"));

                Fragment frag = new Issue_details();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.small_frame, frag).commit();
            }
        });
        return view;
    }
}
