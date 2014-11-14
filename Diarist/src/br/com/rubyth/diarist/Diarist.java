package br.com.rubyth.diarist;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Diarist extends Activity {
	Button login;
	EditText txtLogin;
	EditText txtSenha;
	ImageView imageRegister;
	ImageView imageInfo;
	
	SharedPreferences preference;
	final static String USERNAME_KEY = "username";
	
	DBUser user;
	DBAdapter datasource;
	private AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        preference = getSharedPreferences("user", Context.MODE_PRIVATE);
        
        datasource = new DBAdapter(this);
		login = (Button) findViewById(R.id.btn_login);
		txtLogin = (EditText) findViewById(R.id.txt_login);
		txtSenha = (EditText) findViewById(R.id.txt_senha);
		imageRegister = (ImageView) findViewById(R.id.image_register);
		imageInfo = (ImageView) findViewById(R.id.image_info);
		
		txtLogin.setText(preference.getString(USERNAME_KEY, null)); 
		txtSenha.requestFocus();
		
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// --------------------------------------------------------------------------------
				datasource.open();
				Cursor cursor = datasource.validateLogin(txtLogin.getText().toString());
				System.out.println("----------------------------------" + cursor.getCount());
				datasource.close();
				if(cursor.getCount() != 0){
				validate();

				} else  {	
					AlertDialog.Builder dialog = new AlertDialog.Builder(Diarist.this);
					dialog.setTitle("Confirmation");
					dialog.setMessage("Incorrect Reporting");
					dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					txtSenha.setText("");
					txtLogin.requestFocus();
					}
				});
				dialog.show();
				}
//----------------------------------------------------------------------------------------
			}
		});
		
		imageRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogRegister();
			}
		});
		
		imageInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Diarist.this, Info.class);
				startActivity(i);
				
			}
		});
	}
	public void validate(){
	datasource.open();
	user = datasource.getlogin(txtLogin.getText().toString());
	datasource.close();
		if (user.getSenha().equals(txtSenha.getText().toString())){
			putUserInformations(user.getId());
			Intent intent = new Intent(Diarist.this, Scheduling.class);
			txtSenha.setText("");
			startActivity(intent);
		} else {		
			AlertDialog.Builder dialog = new AlertDialog.Builder(Diarist.this);
			dialog.setTitle("Confirmation");
			dialog.setMessage("Incorrect Reporting");
			dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			txtSenha.setText("");
			txtLogin.requestFocus();
			}
		});
		dialog.show();
		datasource.close();
		}
	}
	
	private void dialogRegister(){
    	//Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("User");
        itens.add("Diarist");

        //adapter utilizando um layout customizado (TextView)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Category");
        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
                if (arg1 == 0){
                	Intent i = new Intent(Diarist.this, Register.class);
    				startActivity(i);
                } else {
                	Intent i = new Intent(Diarist.this, NewDiarist.class);
    				startActivity(i);
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
	
	private void putUserInformations(long id){
		Editor editor = preference.edit();
		editor.putLong("user_id", id);
		editor.putString(Diarist.USERNAME_KEY, txtLogin.getText().toString());
		editor.commit();
	}
	
}
