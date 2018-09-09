package erp.skoolerp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import erp.skoolerp.Pojo.SalaryPojo;
import erp.skoolerp.R;

/**
 * Created by shalu on 16/09/17.
 */

public class SalarayEXPadap extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<SalaryPojo>> _listDataChild;

    public SalarayEXPadap(Context _context, List<String> _listDataHeader, HashMap<String, List<SalaryPojo>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
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
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.salry_grp, null);
        }
        TextView bold_1 = (TextView) convertView.findViewById(R.id.bold_1);
        bold_1.setText(_listDataHeader.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosititon, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.salry_child, null);

        SalaryPojo pojo = _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        TextView pay1 = (TextView) convertView.findViewById(R.id.pay1);
        TextView pay1_value = (TextView) convertView.findViewById(R.id.pay1_value);
        pay1.setText(pojo.getPayhead());
        pay1_value.setText(pojo.getAmount());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosititon) {
        return false;
    }

    public void notify_data(List<String> header, HashMap<String, List<SalaryPojo>> data){
        _listDataChild = data;
        _listDataHeader = header;
        notifyDataSetChanged();
    }
}
