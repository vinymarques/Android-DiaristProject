package br.com.rubyth.diarist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ConsultSchedulings extends Activity {
	private AlertDialog alerta;
	DBAdapter datasource;
	ListView list;
	
	SharedPreferences prefs;
	long idUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_schedulings);
        
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        idUser = prefs.getLong("user_id", 0);
        datasource = new DBAdapter(this);
        datasource.open();
        
        Cursor cursor = datasource.getSchedule(idUser);
        
        // our adapter instance
        final SchedulingCursorAdapter adapter = new SchedulingCursorAdapter(this, R.layout.item_list_schedulings,  cursor);
        list = (ListView) findViewById(R.id.list_schedulings);
        list.setAdapter(adapter);
        datasource.close();
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor = (Cursor) adapter.getItem(position);
				int idDiarist = cursor.getInt(cursor.getColumnIndex("id_diarist"));
				int idService = cursor.getInt(cursor.getColumnIndex("_id"));
				
        		//------------------------------------------------------------------------------------------------------------
				Date dateAtual = new Date(System.currentTimeMillis());    
        	      Date dataAgendamento = null;
        		try {
        			dataAgendamento = formataData(String.valueOf(cursor.getInt(cursor.getColumnIndex("day")) + "-" +
        					(cursor.getInt(cursor.getColumnIndex("month")) + "-" +
        							(cursor.getInt(cursor.getColumnIndex("year"))))));
        		} catch (Exception e1) {
        			e1.printStackTrace();
        		}
        	      int diasDiferenca;
        	      diasDiferenca = (int) ((dateAtual.getTime() - dataAgendamento.getTime()) / 86400000L);
        	      
        	      if (diasDiferenca>0){
        	    	  dialogRating(idDiarist, idService);
        	      } else {
        	    	  String dateAg = String.valueOf(cursor.getInt(cursor.getColumnIndex("day")) + "-" +
          					(cursor.getInt(cursor.getColumnIndex("month")) + "-" +
          							(cursor.getInt(cursor.getColumnIndex("year")))));
        	    	  dialogPending(idService, dateAg);
  				}
        //------------------------------------------------------------------------------------------------------------   
				
			}
		});
    }
    
    
    private void dialogPending(final int id_servico, String data_ag){
    	ArrayList<String> itens = new ArrayList<String>();
        itens.add("Cancel Schedule");

        //adapter utilizando um layout customizado (TextView)
        ArrayAdapter<String> adapterOp = new ArrayAdapter<String>(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(data_ag+ ": Day Pending");
        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapterOp, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	
                if (arg1 == 0){
                	datasource.open();
                	datasource.EliminaSchedule(id_servico);
                    list.removeAllViewsInLayout();
                    Cursor cursor = datasource.getSchedule(idUser);
                    final SchedulingCursorAdapter adapter = new SchedulingCursorAdapter(ConsultSchedulings.this, R.layout.item_list_schedulings,  cursor);
                    list = (ListView) findViewById(R.id.list_schedulings);
                    list.setAdapter(adapter);
                    datasource.close();
                    alerta.dismiss();
                }
            }
        });
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alerta = builder.create();
        alerta.show();
    }
    

    private void dialogRating(final int id_diarist, final int id_servico){
    	ArrayList<String> itens = new ArrayList<String>();
        itens.add("1 - Poor");
        itens.add("2 - Fair");
        itens.add("3 - Good");
        itens.add("4 - Very Good");
        itens.add("5 - Excellent");

        //adapter utilizando um layout customizado (TextView)
        ArrayAdapter<String> adapterOp = new ArrayAdapter<String>(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Evaluate the Service");
        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapterOp, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	datasource.open();
            	DBDiarist diarist;
                diarist = datasource.getdiarist(id_diarist);
                if (arg1 == 0){
                     datasource.AddTotalAvaliacoes(id_diarist, diarist.getTotalAvaliacoes()+1);
                     datasource.AddResultAvaliacoes(id_diarist, (diarist.getResultadoAvaliacoes()+1.0));
                     Toast.makeText(ConsultSchedulings.this, "Voting Completed", Toast.LENGTH_SHORT).show();
                } else 
                	if (arg1 == 1){
                		datasource.AddTotalAvaliacoes(id_diarist, diarist.getTotalAvaliacoes()+1);
                        datasource.AddResultAvaliacoes(id_diarist, (diarist.getResultadoAvaliacoes()+2.0));
                		Toast.makeText(ConsultSchedulings.this, "Voting Completed", Toast.LENGTH_SHORT).show();
                } else 
                	if (arg1 == 2){
                		datasource.AddTotalAvaliacoes(id_diarist, diarist.getTotalAvaliacoes()+1);
                        datasource.AddResultAvaliacoes(id_diarist, (diarist.getResultadoAvaliacoes()+3.0));
                		Toast.makeText(ConsultSchedulings.this, "Voting Completed", Toast.LENGTH_SHORT).show();
                } else 
                	if (arg1 == 3){
                		datasource.AddTotalAvaliacoes(id_diarist, diarist.getTotalAvaliacoes()+1);
                        datasource.AddResultAvaliacoes(id_diarist, (diarist.getResultadoAvaliacoes()+4.0));
                		Toast.makeText(ConsultSchedulings.this, "Voting Completed", Toast.LENGTH_SHORT).show();
                } else 
                	if (arg1 == 4){
                		datasource.AddTotalAvaliacoes(id_diarist, diarist.getTotalAvaliacoes()+1);
                        datasource.AddResultAvaliacoes(id_diarist, (diarist.getResultadoAvaliacoes()+5.0));
                		Toast.makeText(ConsultSchedulings.this, "Voting Completed", Toast.LENGTH_SHORT).show();
                }
                datasource.EliminaSchedule(id_servico);
                list.removeAllViewsInLayout();
                
                Cursor cursor = datasource.getSchedule(idUser);
                final SchedulingCursorAdapter adapter = new SchedulingCursorAdapter(ConsultSchedulings.this, R.layout.item_list_schedulings,  cursor);
                list = (ListView) findViewById(R.id.list_schedulings);
                list.setAdapter(adapter);
                datasource.close();
                alerta.dismiss();
            }
        });
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alerta = builder.create();
        alerta.show();
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