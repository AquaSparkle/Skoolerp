package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import erp.skoolerp.R;
import erp.skoolerp.Studentactivity;

/**
 * Created by shalu on 08/08/17.
 */

public class Event_detail extends Fragment {

    View view;
    ImageView icon;
    TextView title, descrp, nf_date;
    Date date;
    SimpleDateFormat formatter, formatter1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.newsfeed_details, container, false);
        icon = (ImageView) view.findViewById(R.id.news_img);
        title = (TextView) view.findViewById(R.id.title);
        descrp = (TextView) view.findViewById(R.id.descrp);
        nf_date = (TextView) view.findViewById(R.id.nf_date);

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter1 = new SimpleDateFormat("dd MMMM yyyy");

        icon.setVisibility(View.GONE);
        if(getArguments()!=null)
        {
            title.setText(getArguments().getString("title"));
            descrp.setText(getArguments().getString("dsp"));

            String dte = getArguments().getString("date");
            try {
                date = formatter.parse(dte);
                String nw_form = formatter1.format(date);
                nf_date.setText(nw_form);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ((Studentactivity) getActivity()).title.setText("Event");
        return view;
    }
}
