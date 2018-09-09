package erp.skoolerp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import erp.skoolerp.Pojo.Result_pojo;
import erp.skoolerp.R;
import erp.skoolerp.Wschool;

/**
 * Created by shalu on 04/08/17.
 */

public class Results_adap extends BaseExpandableListAdapter {

    Context context;
    private List<String> _listDataHeader;
    private HashMap<String, List<Result_pojo>> _listDataChild;
    TextView grp_title, subject, assess_marks, writn_marks, total, assess, t1, t2, t3, t4;
    LinearLayout asess_line, ase_line;
    boolean typ = false;


    public Results_adap(Context context, List<String> _listDataHeader, HashMap<String, List<Result_pojo>> _listDataChild, boolean typ) {
        this.context = context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        this.typ = typ;
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosititon) {
        return childPosititon;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(groupPosition);
        if (view == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.reslt_grp, null);
        }

        grp_title = (TextView) view.findViewById(R.id.resut_title);
        assess = (TextView) view.findViewById(R.id.t2);
        t1 = (TextView) view.findViewById(R.id.t1);
        t3 = (TextView) view.findViewById(R.id.t3);
        t4 = (TextView) view.findViewById(R.id.t4);
        asess_line = (LinearLayout) view.findViewById(R.id.asess_line);
        grp_title.setText(headerTitle);

        if(typ){
            t1.setText(Wschool.title1);
            assess.setText(Wschool.title2);
            t3.setText(Wschool.title3);
            t4.setText(Wschool.title4);
            assess.setVisibility(View.VISIBLE);
            asess_line.setVisibility(View.VISIBLE);
        }else{
            t1.setText(Wschool.title1);
            t3.setText(Wschool.title3);
            t4.setText(Wschool.title4);
            assess.setVisibility(View.GONE);
            asess_line.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosititon, boolean b, View view, ViewGroup viewGroup) {
        final Result_pojo childpojo = (Result_pojo) getChild(groupPosition, childPosititon);

        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.reslt_child, null);

        subject = (TextView) view.findViewById(R.id.r_sub);
        assess_marks = (TextView) view.findViewById(R.id.r_assess);
        writn_marks = (TextView) view.findViewById(R.id.r_writn);
        total = (TextView) view.findViewById(R.id.r_tot);
        ase_line = (LinearLayout) view.findViewById(R.id.r_assess_line);

        subject.setText(childpojo.getSubj());
        writn_marks.setText(childpojo.getWrittn_mark());
        total.setText(childpojo.getTotal());
        if(typ){
            assess_marks.setText(childpojo.getAsses_mark());
            ase_line.setVisibility(View.VISIBLE);
        }else{
            ase_line.setVisibility(View.GONE);
            assess_marks.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int i1) {
        return false;
    }
}
