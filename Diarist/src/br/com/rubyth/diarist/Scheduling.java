package br.com.rubyth.diarist;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

public class Scheduling extends Activity {
	DBAdapter datasource;
	ImageView evaluate;
	ImageView settings;
	ImageView logout;
	ImageView photoIV;
	TextView nameText;
	
	SharedPreferences prefs;
	long id_user;
	
	String nameUser;
	DBUser user;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling);
        datasource = new DBAdapter(this);
        
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        id_user = prefs.getLong("user_id", 0);
        
        datasource.open();
        user = datasource.getuser(id_user);
        Cursor cursor = datasource.getDiarist();
        
        // our adapter instance
        final DiaristCursorAdapter adapter = new DiaristCursorAdapter(this, R.layout.list_view_row_item,  cursor);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        datasource.close();
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Scheduling.this , SchedulingNewDate.class);
	        	Cursor cursor = (Cursor) adapter.getItem(position);
	        	intent.putExtra("IdDiarist",cursor.getInt(cursor.getColumnIndex("_id")));
	        	intent.putExtra("IdUser", id_user);
	        	startActivity(intent);	
			}
		});
        
        photoIV = (ImageView) findViewById(R.id.photo_top);
        nameText = (TextView) findViewById(R.id.name_top);
        photoIV.setImageBitmap(user.getPicture());
        nameText.setText(user.getName());
        
        evaluate = (ImageView) findViewById(R.id.evaluate_top);
        settings = (ImageView) findViewById(R.id.settings_top);
        logout = (ImageView) findViewById(R.id.logout_top);
        
        evaluate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Scheduling.this, ConsultSchedulings.class);
				startActivity(i);
			}
		});
        
        settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Scheduling.this, AlterUser.class);
				startActivity(i);
			}
		});
        
        logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	datasource.open();
        user = datasource.getuser(id_user);
        datasource.close();
        photoIV.setImageBitmap(user.getPicture());
        nameText.setText(user.getName());
    }

}