package erp.skoolerp.Adapter;

/**
 * Created by shalu on 20/06/17.
 */
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import erp.skoolerp.Fragment.Fees;
import erp.skoolerp.Pojo.Fee_pojo;
import erp.skoolerp.R;

public class Fee_expandadap extends BaseExpandableListAdapter
{
    Fees frag;
    private Context _context;
    private List<String> _listDataHeader, id_array;
    private HashMap<String, List<Fee_pojo>> _listDataChild;
    boolean checked = false;
    HashMap<Integer, List<Integer>> tick_content;
    int count = 0;

    TextView title, amount, paytext, date, month;
    public ImageView tick = null;
    LinearLayout date_lay, bottomspace, topspace;

    public Fee_expandadap(Context context, List<String> listDataHeader, HashMap<String, List<Fee_pojo>> listChildData, Fees frag_obj, List<String> id_array)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.id_array = id_array;
        this.frag = frag_obj;
        tick_content = new HashMap<Integer, List<Integer>>();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent)
    {


        final Fee_pojo childpojo = (Fee_pojo) getChild(groupPosition, childPosition);



            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fee_child, null);
            title = (TextView) convertView.findViewById(R.id.tile);
            amount = (TextView) convertView.findViewById(R.id.amount);
            paytext = (TextView) convertView.findViewById(R.id.paytext);
            date = (TextView) convertView.findViewById(R.id.date);
            month = (TextView) convertView.findViewById(R.id.month);
            tick = (ImageView) convertView.findViewById(R.id.tick);
            date_lay = (LinearLayout) convertView.findViewById(R.id.date_lay);
            bottomspace = (LinearLayout) convertView.findViewById(R.id.bottomspace);
            topspace = (LinearLayout) convertView.findViewById(R.id.topspace);




        title.setText(childpojo.getTitle());
        amount.setText(childpojo.getAmount());
        paytext.setText(childpojo.getPay());
        date.setText(childpojo.getDate());
        month.setText(childpojo.getMonth());

        tick.setVisibility(View.GONE);
        if(childpojo.getPay().equals("Unpaid"))
        {
            tick.setVisibility(View.VISIBLE);
            paytext.setBackgroundColor(Color.parseColor("#F5281A"));
            date_lay.setBackgroundColor(Color.parseColor("#F5281A"));
        }else
        {
            tick.setVisibility(View.GONE);
            paytext.setBackgroundColor(Color.parseColor("#3498db"));
            date_lay.setBackgroundColor(Color.parseColor("#3498db"));
        }

        topspace.setVisibility(View.GONE);
        bottomspace.setVisibility(View.GONE);

        if(childPosition==0)
        {
            topspace.setVisibility(View.VISIBLE);
        }

        if(isLastChild)
        {
            bottomspace.setVisibility(View.VISIBLE);
        }

        if(childpojo.isChecked()){
            tick.setImageResource(R.drawable.checkbox_checked);
        }else {
            tick.setImageResource(R.drawable.checkbox);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fee_grp, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.grp_title);
        ImageView exp_icon = (ImageView) convertView.findViewById(R.id.exp_icon);
        lblListHeader.setText(headerTitle);

        if(isExpanded)
        {
            exp_icon.setImageResource(R.drawable.ic_minus_circ_outlined);
        }else
        {
            exp_icon.setImageResource(R.drawable.ic_add_circular_outlined_button);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    public void notify_data(HashMap<String, List<Fee_pojo>> data){
        _listDataChild = data;
        notifyDataSetChanged();
    }
}
