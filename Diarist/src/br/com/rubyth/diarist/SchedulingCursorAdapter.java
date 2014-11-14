package br.com.rubyth.diarist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class SchedulingCursorAdapter extends ResourceCursorAdapter{

	int idDiarist;
	DBAdapter datasource;
	DBDiarist diarist;
	
	@SuppressWarnings("deprecation")
	public SchedulingCursorAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView day = (TextView) view.findViewById(R.id.day_scheduled);
		day.setText(cursor.getInt(cursor.getColumnIndex("day")) + "-" +
				(cursor.getInt(cursor.getColumnIndex("month")) + "-" +
						(cursor.getInt(cursor.getColumnIndex("year")))));


		TextView diaristName = (TextView) view.findViewById(R.id.diarist_scheduled);
		idDiarist = cursor.getInt(cursor.getColumnIndex("id_diarist"));
		datasource = new DBAdapter(context);
		datasource.open();
		diarist = datasource.getdiarist(idDiarist);
		datasource.close();
		diaristName.setText("Diarist: " + diarist.getName());
		
		String dateSelected = String.valueOf(day.getText());
		//------------------------------------------------------------------------------------------------------------
		ImageView image = (ImageView) view.findViewById(R.id.image_scheduled);
				
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
//	      System.out.println("TESTE --------------------------------------  : "+diasDiferenca);	
	      
	      if (diasDiferenca>0){
	    	image.setBackgroundResource(R.drawable.curtir);
	      } else {
	    	  image.setBackgroundResource(R.drawable.pending);
	      }
//------------------------------------------------------------------------------------------------------------   
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
}
