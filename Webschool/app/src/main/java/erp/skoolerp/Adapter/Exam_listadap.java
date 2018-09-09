package erp.skoolerp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import erp.skoolerp.Pojo.Exm_pojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 30/07/17.
 */

public class Exam_listadap extends BaseExpandableListAdapter {

    Context context;
    private List<String> _listDataHeader;
    private HashMap<String, List<Exm_pojo>> _listDataChild;
    TextView grp_title, subject, date, st_time, ed_time;

    public Exam_listadap(Context context, List<String> _listDataHeader, HashMap<String, List<Exm_pojo>> _listDataChild) {
        this.context = context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
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
            view = infalInflater.inflate(R.layout.exm_list_grp, null);
        }

        grp_title = (TextView) view.findViewById(R.id.grp_title);
        grp_title.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosititon, boolean b, View view, ViewGroup viewGroup) {

        final Exm_pojo childpojo = (Exm_pojo) getChild(groupPosition, childPosititon);

        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.exm_list_child, null);

        subject = (TextView) view.findViewById(R.id.subj);
        date = (TextView) view.findViewById(R.id.date);
        st_time = (TextView) view.findViewById(R.id.st_tym);
        ed_time = (TextView) view.findViewById(R.id.ed_tym);

        subject.setText(childpojo.getSub());
        date.setText(childpojo.getDate());
        st_time.setText(childpojo.getSt_time());
        ed_time.setText(childpojo.getEd_time());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosititon) {
        return false;
    }
}
