package br.com.rubyth.diarist;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class SchedulingNewDate extends Activity  implements OnClickListener{
	
	private ViewPager awesomePager;
    private static int NUM_AWESOME_VIEWS = 2;
    private Context cxt;
    private AwesomePagerAdapter awesomeAdapter;
    private AlertDialog alertSelectDate;
    
	int idDiarist;
	long idUser;
	SharedPreferences prefs;
	
	DBAdapter  datasource;
	DBDiarist diarist;
	
	RatingBar pontuacao;
	TextView txtName;
	TextView txtPrice;
	TextView txtAddress;
	TextView txtPhone;
	TextView txtCPF;
	TextView txtEmail;
	ImageView ivFoto;
	TextView txtScoreDiarist;
	TextView txtTotalScheduling;
	
	private static final String tag = "SimpleCalendarViewActivity";
	private ImageView calendarToJournalButton;
	private Button currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduling_info_diarist);
		
		cxt = this;
		datasource = new DBAdapter(this);
		
		idDiarist = getIntent().getIntExtra("IdDiarist", 0);
//		idUser = getIntent().getIntExtra("IdUser", 0);
		 prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
		 idUser = prefs.getLong("user_id", 0);
	        
		datasource.open();
		diarist = datasource.getdiarist(idDiarist);
		datasource.close();
		
		awesomeAdapter = new AwesomePagerAdapter();
		awesomePager = (ViewPager) findViewById(R.id.pagerView);
		awesomePager.setAdapter(awesomeAdapter);
		
		
	  
	}

	class AwesomePagerAdapter extends PagerAdapter {
		/**
		* Indica o número de telas que o Adapter vai montar.
		*/
		
		  @Override
		  public int getCount() {
		   return NUM_AWESOME_VIEWS;
		  }
 
		/**
		* Método responsável pelo carregamento das suas telas
		* No exemplo eu uso uma quantidade de telas estáticas e diferentes,
		* por isso preciso tratar qual tela devo carregar,
		* pois este método sera chamado uma vez para cada tela que vc indicou 
		* que vai montar no método getCount()
		*/
		  
		  @Override
		  public Object instantiateItem(View collection, int position) {		  
		   switch (position) {
		    case 0:
		     LinearLayout tela1 = (LinearLayout) View.inflate(SchedulingNewDate.this,
		    		 R.layout.simple_calendar_view, null);		    
		     ((ViewPager) collection).addView(tela1, position);
		     // METODOS TELA1
		     carregaCalendar();
		     
		     return tela1;
		     
		    case 1:
		     LinearLayout tela2 = (LinearLayout) View.inflate(SchedulingNewDate.this,
		    		 R.layout.info_diarist, null);
		     ((ViewPager) collection).addView(tela2, position);
		     // METODOS TELA2
		     carregaDetalhesContacto();
		     return tela2;
		     
		    	
		    default:
		     return null;
		   }
		  }

		  /**
		   * Remove a tela pela posição. 
		   */
		  @Override
		  public void destroyItem(View collection, int position, Object view) {
		   ((ViewPager) collection).removeView((LinearLayout) view);
		  }

		  @Override
		  public boolean isViewFromObject(View view, Object object) {
		   return view == ((LinearLayout) object);
		  }

		  @Override
		  public void finishUpdate(View arg0) {}

		  @Override
		  public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		  @Override
		  public Parcelable saveState() {
		   return null;
		  }

		  @Override
		  public void startUpdate(View arg0) {}
		 }
	
	private void carregaDetalhesContacto(){
		txtName = (TextView) findViewById(R.id.name_info);
		txtPrice = (TextView) findViewById(R.id.price_info);
		txtAddress = (TextView) findViewById(R.id.address_info_diarist);
		txtPhone = (TextView) findViewById(R.id.phone_info);
		txtCPF = (TextView) findViewById(R.id.cpf_info);
		txtEmail = (TextView) findViewById(R.id.mail_info);
		ivFoto = (ImageView) findViewById(R.id.image_info);		
		txtScoreDiarist = (TextView) findViewById(R.id.score_info);
		txtTotalScheduling= (TextView) findViewById(R.id.total_info);
		pontuacao = (RatingBar) findViewById(R.id.pontuacao_rating);
		
		if (diarist.getName().split(" ").length >=2){
				txtName.setText(diarist.getName().split(" ")[0] +" " + diarist.getName().split(" ")[1]);
		} else {
			txtName.setText(diarist.getName());
		}
		txtPrice.setText(diarist.getPrice());
		txtAddress.setText("Address: "+diarist.getCity() + " - " + diarist.getState());
		txtPhone.setText("Phone: "+diarist.getPhone());
		txtCPF.setText("CPF: "+diarist.getCPF());
		txtEmail.setText("E-mail: "+diarist.getEmail());
		ivFoto.setImageBitmap(diarist.getPicture());
		
		DecimalFormat df = new DecimalFormat("0.#");  
		String evaluationC = df.format(diarist.getResultadoAvaliacoes()/diarist.getTotalAvaliacoes());  
		txtScoreDiarist.setText("Evaluation (0-5): " + evaluationC);
		
		double evaluation = diarist.getResultadoAvaliacoes()/diarist.getTotalAvaliacoes();  
		if (evaluation <=0.5){
			pontuacao.setProgress(1);
		} 
		else if (evaluation > 0.5 && evaluation<=1.0){
			pontuacao.setProgress(2);
		} 
		else if (evaluation > 1.0 && evaluation<=1.5){
			pontuacao.setProgress(3);
		} 
		else if (evaluation > 1.5 && evaluation<=2.0){
			pontuacao.setProgress(4);
		} 
		else if (evaluation > 2.0 && evaluation<=2.5){
			pontuacao.setProgress(5);
		} 
		else if (evaluation > 2.5 && evaluation<=3.0){
			pontuacao.setProgress(6);
		} 
		else if (evaluation > 3.0 && evaluation<=3.5){
			pontuacao.setProgress(7);
		} 
		else if (evaluation > 3.5 && evaluation<=4.0){
			pontuacao.setProgress(8);
		} 
		else if (evaluation > 4.0 && evaluation<=4.5){
			pontuacao.setProgress(9);
		} 
		else if (evaluation > 4.5 && evaluation<=5.0){
			pontuacao.setProgress(10);
		}
		
		pontuacao.setEnabled(false);
		
		txtTotalScheduling.setText("Schedules Made: "+String.valueOf(diarist.getTotalServicos()));
	}
	
	private void carregaCalendar(){
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

/**
 * 
 * @param monthvalue
 * @param year
 */
private void setGridCellAdapterToDate(int month, int year)
	{
		adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

@Override
public void onClick(View v)
	{
		if (v == prevMonth)
			{
				if (month <= 1)
					{
						month = 12;
						year--;
					}
				else
					{
						month--;
					}
				Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);
			}
		if (v == nextMonth)
			{
				if (month > 11)
					{
						month = 1;
						year++;
					}
				else
					{
						month++;
					}
				Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				setGridCellAdapterToDate(month, year);
			}

	}

@Override
public void onDestroy()
	{
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

// ///////////////////////////////////////////////////////////////////////////////////////
// Inner Class
public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		private final int month, year;
		private int daysInMonth, prevMonthDays;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
			{
				super();
				this._context = context;
				this.list = new ArrayList<String>();
				this.month = month;
				this.year = year;

				Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
				Calendar calendar = Calendar.getInstance();
				setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
				setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
				Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
				Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
				Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

				// Print Month
				printMonth(month, year);

				// Find Number of Events
				eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
			}
		private String getMonthAsString(int i)
			{
				return months[i];
			}

		private String getWeekDayAsString(int i)
			{
				return weekdays[i];
			}

		private int getNumberOfDaysOfMonth(int i)
			{
				return daysOfMonth[i];
			}

		public String getItem(int position)
			{
				return list.get(position);
			}

		@Override
		public int getCount()
			{
				return list.size();
			}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy)
			{
				Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
				// The number of days to leave blank at
				// the start of this month.
				int trailingSpaces = 0;
				int leadSpaces = 0;
				int daysInPrevMonth = 0;
				int prevMonth = 0;
				int prevYear = 0;
				int nextMonth = 0;
				int nextYear = 0;

				int currentMonth = mm - 1;
				String currentMonthName = getMonthAsString(currentMonth);
				daysInMonth = getNumberOfDaysOfMonth(currentMonth);

				Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

				// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
				GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
				Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

				if (currentMonth == 11)
					{
						prevMonth = currentMonth - 1;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						nextMonth = 0;
						prevYear = yy;
						nextYear = yy + 1;
						Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}
				else if (currentMonth == 0)
					{
						prevMonth = 11;
						prevYear = yy - 1;
						nextYear = yy;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						nextMonth = 1;
						Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}
				else
					{
						prevMonth = currentMonth - 1;
						nextMonth = currentMonth + 1;
						nextYear = yy;
						prevYear = yy;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}

				// Compute how much to leave before before the first day of the
				// month.
				// getDay() returns 0 for Sunday.
				int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
				trailingSpaces = currentWeekDay;

				Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
				Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
				Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

				if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
					{
						++daysInMonth;
					}

				// Current Month Days
				for (int i = 1; i <= daysInMonth; i++)
					{
						Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
						if (i == getCurrentDayOfMonth())
							{
								list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
							}
						else
							{
								list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
							}
					}
			}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		
		private HashMap findNumberOfEventsPerMonth(int year, int month)
			{
				HashMap map = new HashMap<String, Integer>();

				 DateFormat dateFormatter2 = new DateFormat();
				 // --------------------------------------------------------------------------------
				 String dbDate;
					String dateValidate = null;
					datasource.open();
					Cursor cursor = datasource.validateDay(idDiarist);
					
						while (cursor.moveToNext()){
							dbDate = (cursor.getInt(0)+"-"+(cursor.getInt(1)+"-"+(cursor.getInt(2))));
							for (int dayV=1;dayV<=31; dayV++){
								dateValidate = dayV+"-"+month+"-"+year;
							if(dbDate.equals(dateValidate)){
								System.out.println("d-------------------- DAY NOT VALID: "+dbDate.split("-")[0]);
								 String day = dateFormatter2.format(dbDate.split("-")[0], 0).toString();
								 if (map.containsKey(day)){
								 Integer val = (Integer) map.get(day) + 1;
								 map.put(day, val);
								 } else{
								 map.put(day, 1);
								 }
							}
						}
					}
					
					datasource.close();
					//----------------------------------------------------------------------------------------
				return map;
			}

		@Override
		public long getItemId(int position)
			{
				return position;
			}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
			{
				View row = convertView;
				if (row == null)
					{
						LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
					}

				// Get a reference to the Day gridcell
				gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
				gridcell.setOnClickListener(this);

				// ACCOUNT FOR SPACING

				Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
				String[] day_color = list.get(position).split("-");
				String theday = day_color[0];
				String themonth = day_color[2];
				String theyear = day_color[3];
				if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
					{
						if (eventsPerMonthMap.containsKey(theday))
							{
								num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
								Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
								gridcell.setClickable(false);
								num_events_per_day.setText("X");
							}
					}

				// Set the Day GridCell
				gridcell.setText(theday);
				gridcell.setTag(theday + "-" + themonth + "-" + theyear);
				Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

				if (day_color[1].equals("GREY"))
					{
						gridcell.setTextColor(Color.LTGRAY);
					}
				if (day_color[1].equals("WHITE"))
					{
						gridcell.setTextColor(Color.WHITE);
					}
				if (day_color[1].equals("BLUE"))
					{
						gridcell.setTextColor(getResources().getColor(R.color.static_text_color));
					}
				return row;
			}
		@Override
		public void onClick(View view)
			{
				String date_month_year = (String) view.getTag();
				
				final int daySelected = Integer.parseInt(date_month_year.split("-")[0]);
				final int monthSelected = getMounthSelected(date_month_year);
				final int yearSelected = Integer.parseInt(date_month_year.split("-")[2]);
				
				final String dateSelected = daySelected+"-"+monthSelected+"-"+yearSelected;
				
//------------------------------------------------------------------------------------------------------------
			      Date dateAtual = new Date(System.currentTimeMillis());    
			      
			      Date dataAgendamento = null;
				try {
					dataAgendamento = formataData(dateSelected);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			      int diasDiferenca;
			      diasDiferenca = (int) ((dateAtual.getTime() - dataAgendamento.getTime()) / 86400000L);
//			      System.out.println("TESTE --------------------------------------  : "+diasDiferenca);	
			      
			      if (diasDiferenca>-1){ //>-1
			    	 alertNotValidDay();
			      } else {
			    	  dialogAgendamento(date_month_year, daySelected, monthSelected, yearSelected, idDiarist, idUser);  
			      }
//------------------------------------------------------------------------------------------------------------    
				try
					{
						Date parsedDate = dateFormatter.parse(date_month_year);
						Log.d(tag, "Parsed Date: " + parsedDate.toString());
						
					}
				
				catch (ParseException e)
					{
						e.printStackTrace();
					}
			}
		private void alertNotValidDay(){
			AlertDialog AlertNotValidDay;
			AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
	        builder.setTitle("Day not Valid");
	        builder.setMessage("Schedules with at least two days in antecedence");
	        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            }
	        });
	        AlertNotValidDay = builder.create(); //CRIA
	        AlertNotValidDay.show(); //MOSTRA
		}
		
		public Date formataData(String data) throws Exception {   
	        if (data == null || data.equals(""))  
	            return null;  
	          
	        Date date = null;  
	        try {  
	            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
	            date = (java.util.Date)formatter.parse(data);  
	        } catch (ParseException e) {              
	            throw e;  
	        }  
	        return date;  
	    }  
		
		private int getMounthSelected(String date){
			String monthSelectedString = date.split("-")[1];
			int monthSelectedInt = 0; 
			
			if(monthSelectedString.equals("January")){
				monthSelectedInt = 1;
			} else if (monthSelectedString.equals("February")){
				monthSelectedInt = 2;
			} else if (monthSelectedString.equals("March")){
				monthSelectedInt = 3;
			} else if (monthSelectedString.equals("April")){
				monthSelectedInt = 4;
			} else if (monthSelectedString.equals("May")){
				monthSelectedInt = 5;
			} else if (monthSelectedString.equals("June")){
				monthSelectedInt = 6;
			} else if (monthSelectedString.equals("July")){
				monthSelectedInt = 7;
			} else if (monthSelectedString.equals("August")){
				monthSelectedInt = 8;
			} else if (monthSelectedString.equals("September")){
				monthSelectedInt = 9;
			} else if (monthSelectedString.equals("October")){
				monthSelectedInt = 10;
			} else if (monthSelectedString.equals("November")){
				monthSelectedInt = 11;
			} else if (monthSelectedString.equals("December")){
				monthSelectedInt = 12;
			} 
				return monthSelectedInt;
		}
		
		private void dialogAgendamento(final String date, final int day, final int month, final int year, final int id_diarist, final long id_user) {
	       
	        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
	        builder.setTitle("Date " + date);
	        builder.setMessage("Schedule this Day ?");
	        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	alertSelectDate.dismiss();
	            	try {
	            		datasource.open();
						datasource.createSchedule(day, month, year, id_diarist, id_user);
						datasource.AddTotalServicos(diarist.getId(),diarist.getTotalServicos()+1);
						datasource.close();
						dialogConfirm(date);
					} catch (Exception e) {
						Toast.makeText(SchedulingNewDate.this, "Not Available Day ", arg1).show();
					}       
	            }
	        });
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            }
	        });
	        alertSelectDate = builder.create(); //CRIA
	        alertSelectDate.show(); //MOSTRA
	    }

		private void dialogConfirm(String date){
			AlertDialog AlertConfirm;
			AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
	        builder.setTitle("Confirmation");
	        builder.setMessage(date+ " Scheduled");
	        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	finish();

	            }
	        });
	        AlertConfirm = builder.create(); //CRIA
	        AlertConfirm.show(); //MOSTRA
		}
		public int getCurrentDayOfMonth()
			{
				return currentDayOfMonth;
			}

		private void setCurrentDayOfMonth(int currentDayOfMonth)
			{
				this.currentDayOfMonth = currentDayOfMonth;
			}
		public void setCurrentWeekDay(int currentWeekDay)
			{
				this.currentWeekDay = currentWeekDay;
			}
		public int getCurrentWeekDay()
			{
				return currentWeekDay;
			}
		}
	}

