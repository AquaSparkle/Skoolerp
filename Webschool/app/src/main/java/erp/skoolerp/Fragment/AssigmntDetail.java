package erp.skoolerp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import erp.skoolerp.Employactivity;
import erp.skoolerp.Pojo.Assignmnt_Pojo;
import erp.skoolerp.R;
import erp.skoolerp.Studentactivity;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 16/09/17.
 */

public class AssigmntDetail extends Fragment
{
    View view;
    ImageView icon;
    TextView title, descrp, nf_date;
    String circ_id;
    Assignmnt_Pojo pojo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newsfeed_details, container, false);

        icon = (ImageView) view.findViewById(R.id.news_img);
        title = (TextView) view.findViewById(R.id.title);
        descrp = (TextView) view.findViewById(R.id.descrp);
        nf_date = (TextView) view.findViewById(R.id.nf_date);

        icon.setVisibility(View.GONE);

        if(Assignments.selectedassignmnt != null){
            pojo = Assignments.selectedassignmnt;
            title.setText(pojo.getTitle());
            descrp.setText(pojo.getDescrp());
            nf_date.setText("Submission date: "+pojo.getDate());
        }
        if(Wschool.employee_log){
            ((Employactivity) getActivity()).title.setText("Assignment");
        }else{
            ((Studentactivity) getActivity()).title.setText("Assignment");
        }
        return view;
    }
}
