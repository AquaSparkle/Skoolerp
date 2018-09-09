package erp.skoolerp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import erp.skoolerp.Fragment.Events;
import erp.skoolerp.Fragment.Leave;
import erp.skoolerp.Fragment.Student_attendance;
import erp.skoolerp.Pojo.Date_pojo;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout
{
	// for logging
	private static final String LOGTAG = "Calendar View";

	// how many days to show, defaults to six weeks, 42 days
	private static final int DAYS_COUNT = 42;

	// default date format
	private static final String DATE_FORMAT = "MMM yyyy";

	// date format
	private String dateFormat;

	// current displayed month
	private Calendar currentDate = Calendar.getInstance();

	//event handling
	private EventHandler eventHandler = null;

	private Date today, selected_date;
	// internal components
	private LinearLayout header;
	private ImageView btnPrev;
	private ImageView btnNext;
	private TextView txtDate;
	private GridView grid;
	private Spinner mnth, yr;
	ArrayList<String> mnth_ar, year_ar;
	Context context;
	int year_selected, mnth_selected, actual_month;
	String frag_name;
	public ArrayList<Date_pojo> event_array;
	public Student_attendance frag_std;
	public Events frag_event;
	public Leave frag_leave;
	public CalendarView(Context context)
	{
		super(context);
	}

	public CalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initControl(context, attrs);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initControl(context, attrs);
	}

	private void initControl(Context context, AttributeSet attrs)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.control_calendar, this);

		this.context = context;
		mnth_ar = new ArrayList<String>();
		year_ar = new ArrayList<String>();
		event_array = new ArrayList<>();

		today = new Date();
		selected_date = today;
		loadDateFormat(attrs);
		assignUiElements();
		assignClickHandlers();

		updateCalendar();
	}

	private void loadDateFormat(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

		try
		{
			// try to load provided date format, and fallback to default otherwise
			dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
			if (dateFormat == null)
				dateFormat = DATE_FORMAT;
		}
		finally
		{
			ta.recycle();
		}
	}

	private void assignUiElements()
	{
		header = (LinearLayout)findViewById(R.id.calendar_header);
		mnth = (Spinner) findViewById(R.id.mnth);
		yr = (Spinner) findViewById(R.id.year);
		grid = (GridView)findViewById(R.id.calendar_grid);
	}

	private void assignClickHandlers()
	{
		// long-pressing a day
		grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id)
			{
				// handle long-press
				if (eventHandler == null)
					return false;

				eventHandler.onDayLongPress((Date)view.getItemAtPosition(position));
				return true;
			}
		});

		yr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				year_selected = Integer.valueOf(year_ar.get(i));
				currentDate.set(year_selected, actual_month, 1);

				selected_date = new Date(year_selected, actual_month, 1);
//				if(!event_array.isEmpty())
//				{
					updateCalendar(event_array);
//				}else
//				{
//					updateCalendar(null);
//				}
				if(frag_name.equals("std_attend"))
				{
					frag_std.setadapter(year_selected, mnth_selected);
				}else if(frag_name.equals("events"))
				{
					frag_event.setadapter(year_selected, mnth_selected);
				}else if(frag_name.equals("leave"))
				{
					frag_leave.setadapter(year_selected, mnth_selected);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});

		mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				mnth_selected = i+1;
				actual_month = i;
				currentDate.set(year_selected, actual_month, 1);
				selected_date = new Date(year_selected, actual_month, 1);
//				if(!event_array.isEmpty())
//				{
					updateCalendar(event_array);
//				}else
//				{
//					updateCalendar(null);
//				}
				if(frag_name.equals("std_attend"))
				{
					frag_std.setadapter(year_selected, mnth_selected);
				}else if(frag_name.equals("events"))
				{
					frag_event.setadapter(year_selected, mnth_selected);
				}else if(frag_name.equals("leave"))
				{
					frag_leave.setadapter(year_selected, mnth_selected);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});
	}

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar()
	{
		updateCalendar(null);
	}

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar(ArrayList<Date_pojo> events)
	{
		event_array = events;
		ArrayList<Date> cells = new ArrayList<>();
		Calendar calendar = (Calendar)currentDate.clone();

		// determine the cell for current month's beginning
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		// move calendar backwards to the beginning of the week
		calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

		// fill cells
		while (cells.size() < DAYS_COUNT)
		{
			cells.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		// update grid
		grid.setAdapter(new CalendarAdapter(getContext(), cells, events));
	}

	public void settoolbar(ArrayList<String> year)
	{
		Calendar calendar = Calendar.getInstance();
		int thisMonth = calendar.get(Calendar.MONTH);
		int thisyr = calendar.get(Calendar.YEAR);

		year_ar = year;
		ArrayAdapter<String> yr_adap = new ArrayAdapter<String>(context, R.layout.spinner_item, year);
		yr_adap.setDropDownViewResource(R.layout.spinner_drop_item);
		yr.setAdapter(yr_adap);


		String[] months = new DateFormatSymbols().getMonths();
		for (int i = 0; i < months.length; i++)
		{
			mnth_ar.add(months[i]);
		}

		ArrayAdapter<String> mnth_adap = new ArrayAdapter<String>(context, R.layout.spinner_item, mnth_ar);
		mnth_adap.setDropDownViewResource(R.layout.spinner_drop_item);
		mnth.setAdapter(mnth_adap);

		int yvalue = year_ar.indexOf(String.valueOf(thisyr));
		if(yvalue != -1){
			yr.setSelection(yvalue);
		}
		mnth.setSelection(thisMonth);
		year_selected = thisyr;
		mnth_selected = thisMonth+1;
	}

	private class CalendarAdapter extends ArrayAdapter<Date> {
		// days with events
		private ArrayList<Date_pojo> eventDays;
		TextView date_text;
		private LayoutInflater inflater;

		public CalendarAdapter(Context context, ArrayList<Date> days, ArrayList<Date_pojo> eventDays) {
			super(context, R.layout.control_calendar_day, days);
			this.eventDays = eventDays;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date date = getItem(position);
			String currentdate = formatter.format(date);

			int month = date.getMonth();

			if (view == null)
				view = inflater.inflate(R.layout.control_calendar_day, parent, false);
			date_text = (TextView) view.findViewById(R.id.date_text);

			date_text.setBackgroundResource(0);
			date_text.setTextColor(Color.BLACK);

			if (month != selected_date.getMonth())
			{
				// if this day is outside current month, grey it out
				date_text.setTextColor(getResources().getColor(R.color.greyed_out));
			}else{

				if (eventDays != null)
				{
					for (Date_pojo eventDate : eventDays)
					{
						String eventdate = eventDate.getDate();
						if (currentdate.equals(eventdate))
						{
							if(eventDate.isAbsent()){
								date_text.setBackgroundResource(R.drawable.round);
							}else{
								date_text.setBackgroundResource(R.drawable.blue);
							}
							date_text.setTextColor(Color.WHITE);
						}
					}
				}
			}

//			if (day == today.getDay())
//			{
//				// if it is today, set it to blue/bold
//				//((TextView)view).setTypeface(null, Typeface.BOLD);
//				date_text.setTextColor(getResources().getColor(R.color.today));
//				Toast.makeText(context, "...EQUAL...", Toast.LENGTH_SHORT).show();
//			} else
			//{

			//}

			date_text.setText(String.valueOf(date.getDate()));

			return view;
		}
	}

	/**
	 * Assign event handler to be passed needed events
	 */
	public void setEventHandler(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}

	/**
	 * This interface defines what events to be reported to
	 * the outside world
	 */
	public interface EventHandler
	{
		void onDayLongPress(Date date);
	}

	public void Frag_name(String name)
	{
		frag_name = name;
	}

}
