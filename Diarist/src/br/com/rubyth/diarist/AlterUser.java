package br.com.rubyth.diarist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AlterUser extends Activity{

	private AlertDialog alerta;
	
	EditText nameEdit;
	EditText cityEdit;
	EditText phoneEdit;
	EditText txtLogin;
	EditText txtSenha;
	EditText txtConfirmPass;
	EditText stateEdit;
	ImageView iv;
	
	String login;
	boolean validLoginB = true;
	boolean validPass1B = true;
	boolean validSenhaB = true ;
	ImageView validLogin;
	ImageView validPass1;
	ImageView validSenha;
	
	ImageView btnEdit;
	ImageView btnSave;

	final static int cameraData = 0;
	private DBAdapter datasource;
	private DBUser user;  
	
	SharedPreferences prefs;
	long idUser;
	
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alter_user);
		
		datasource = new DBAdapter(this);
		prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        idUser = prefs.getLong("user_id", 0);
        
        datasource.open();
        user = datasource.getuser(idUser);
        datasource.close();
        
        nameEdit = (EditText) findViewById(R.id.a_name);
        cityEdit = (EditText) findViewById(R.id.a_city);
        
        phoneEdit = (EditText) findViewById(R.id.a_phone);
        phoneEdit.addTextChangedListener(Mask.insert("(##)#####-####", phoneEdit));
        
        stateEdit = (EditText) findViewById(R.id.a_state);
        
        txtLogin = (EditText) findViewById(R.id.a_login);
        txtSenha = (EditText) findViewById(R.id.a_pass);
        txtConfirmPass = (EditText) findViewById(R.id.a_confirm_pass);
        
        validLogin = (ImageView) findViewById(R.id.a_valid_login);
        validPass1 = (ImageView) findViewById(R.id.a_valid_pass1);
        validSenha = (ImageView) findViewById(R.id.a_valid_senha);
 		iv = (ImageView) findViewById(R.id.ivReturnedPic_a);
 		
 		btnEdit = (ImageView) findViewById(R.id.edit_top_user);
 		btnSave = (ImageView) findViewById(R.id.save_top_user);
 		
		sizeItems();
		disableEdit();
// SET -------------------------------------------------------------------------------------------------------
		login = user.getLogin();
		nameEdit.setText(user.getName());
		txtLogin.setText(user.getLogin());
		txtSenha.setText(user.getSenha());
		txtConfirmPass.setText(user.getSenha());
		cityEdit.setText(user.getCity());
		stateEdit.setText(user.getState());
		phoneEdit.setText(user.getPhone());
		iv.setImageBitmap(user.getPicture());
// SET -------------------------------------------------------------------------------------------------------	
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogImage();
				
			}
		});
	
		txtLogin.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
// --------------------------------------------------------------------------------
					datasource.open();
					Cursor cursor = datasource.validateLogin(txtLogin.getText().toString());
					if(cursor.getCount() == 0 || txtLogin.getText().toString().equals(login)){
						validLogin.setBackgroundResource(R.drawable.ok);
						validLoginB = true;
					} else  {
						validLogin.setBackgroundResource(R.drawable.x1);
						validLoginB = false;
					}
					datasource.close();
//----------------------------------------------------------------------------------------
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		txtSenha.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (txtSenha.getText().toString().length() <4){
					validPass1.setBackgroundResource(R.drawable.x1);
					validPass1B = false;
				} else {
					validPass1.setBackgroundResource(R.drawable.ok);
					validPass1B = true;
				}
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		txtConfirmPass.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (txtConfirmPass.getText().toString().equals(txtSenha.getText().toString())){
					validSenha.setBackgroundResource(R.drawable.ok);
					validSenhaB = true;
				} else {
					validSenha.setBackgroundResource(R.drawable.x1);
					validSenhaB = false;
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		btnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enableEdit();
			}
		});
	
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogConfirmAlter();
			}
		});
	}

    public void sizeItems(){
    	
   	 final DisplayMetrics metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 
		 nameEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/3);
		 cityEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);

		 stateEdit.setWidth(metrics.widthPixels/5);
		 phoneEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/6);
		 
		 txtLogin.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);
		 txtSenha.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);
		 txtConfirmPass.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);
   }

    private void dialogImage(){
    	//Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Take");
        itens.add("Search");

        //adapter utilizando um layout customizado (TextView)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecting yor Picture");
        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
                if (arg1 == 0){
                	Intent i= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    				startActivityForResult(i,cameraData);
                } else {
                	Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
    				photoPickerIntent.setType("image/*");
    				startActivityForResult(photoPickerIntent, 1);
    				onActivityResult(0, 0, getIntent());
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

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
			if(resultCode == RESULT_OK){
				Uri imageUri = data.getData();
				try {
					Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
					iv.setImageBitmap(bmp);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	
    public static Bitmap loadBitmapFromView (View v) {
		Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
	    Canvas c = new Canvas(b);
	    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	    v.draw(c);
	    return b;
	}

	private void disableEdit(){
		iv.setEnabled(false);
		nameEdit.setEnabled(false);
		txtLogin.setEnabled(false);
		txtSenha.setEnabled(false);
		txtConfirmPass.setEnabled(false);
		cityEdit.setEnabled(false);
		stateEdit.setEnabled(false);
		phoneEdit.setEnabled(false);
		btnSave.setEnabled(false);
	}
	
	private void enableEdit(){
		iv.setEnabled(true);
		nameEdit.setEnabled(true);
		txtLogin.setEnabled(true);
		txtSenha.setEnabled(true);
		txtConfirmPass.setEnabled(true);
		cityEdit.setEnabled(true);
		stateEdit.setEnabled(true);
		phoneEdit.setEnabled(true);
		
		btnSave.setEnabled(true);
		btnEdit.setBackgroundResource(R.drawable.shapetextviewtransparent);
		btnSave.setBackgroundResource(R.drawable.button_selected);
		
	}
	
	private void dialogConfirmAlter(){
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Yes");
        itens.add("No");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_alerta, itens);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Want to change ?");

        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
                if (arg1 == 0){
                	if (nameEdit.getText().toString().isEmpty() || cityEdit.getText().toString().isEmpty() || phoneEdit.getText().toString().isEmpty() || validLoginB == false || validSenhaB == false || validPass1B == false) {
    					AlertDialog.Builder dialog = new AlertDialog.Builder(AlterUser.this);
    					dialog.setTitle("Confirmation");
    					dialog.setMessage("Incorrect Reporting");
    					dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							nameEdit.requestFocus();
    						}
    					});
    					dialog.show();
    		
    				} else {
    					try {
    						datasource.open();
    	                	datasource.AlterUser(idUser, nameEdit.getText().toString(), txtLogin.getText().toString(), txtSenha.getText().toString(), cityEdit.getText().toString(), stateEdit.getText().toString(), phoneEdit.getText().toString(), loadBitmapFromView(iv));
    	            		datasource.close();
    						AlertDialog.Builder dialog = new AlertDialog.Builder(AlterUser.this);
    						dialog.setTitle("Confirmation");
    						dialog.setMessage("User changed");
    						putUserInformations(idUser);
    						dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								finish();
    							}
    						});
    						dialog.show();
    					} catch (Exception e) {
    						Toast.makeText(AlterUser.this, "Failure in Register", Toast.LENGTH_SHORT).show();
    					}
    					
    				}
                } else {
                	finish();
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
		Editor editor = prefs.edit();
		editor.putLong("user_id", id);
		editor.putString(Diarist.USERNAME_KEY, txtLogin.getText().toString());
		editor.commit();
	}

}
