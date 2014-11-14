package br.com.rubyth.diarist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity{

	private AlertDialog alerta;
	private List<String> ufList = new ArrayList<String>();
	
	EditText nameEdit;
	EditText cityEdit;
	EditText phoneEdit;
	EditText txtLogin;
	EditText txtSenha;
	EditText txtConfirmPass;
	Spinner ufSpn;
	ImageView iv;
	
	boolean validLoginB;
	boolean validPass1B;
	boolean validSenhaB;
	ImageView validLogin;
	ImageView validPass1;
	ImageView validSenha;
	
	Button btnCadastro;
	Button btnReturn;

	final static int cameraData = 0;
	private DBAdapter datasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        datasource = new DBAdapter(this);
        nameEdit = (EditText) findViewById(R.id.l_name);
        cityEdit = (EditText) findViewById(R.id.l_city);
        
        phoneEdit = (EditText) findViewById(R.id.l_phone);
        phoneEdit.addTextChangedListener(Mask.insert("(##)#####-####", phoneEdit));
        
        ufSpn = (Spinner) findViewById(R.id.l_state);
        
        btnCadastro = (Button) findViewById(R.id.btn_register_l);
        btnReturn = (Button) findViewById(R.id.btn_return_l);
        
        txtLogin = (EditText) findViewById(R.id.l_login);
        txtSenha = (EditText) findViewById(R.id.l_pass);
        txtConfirmPass = (EditText) findViewById(R.id.l_confirm_pass);
        
		iv = (ImageView) findViewById(R.id.ivReturnedPic_l);
		
		validLogin = (ImageView) findViewById(R.id.l_valid_login);
		validPass1 = (ImageView) findViewById(R.id.l_valid_pass1);
		validSenha = (ImageView) findViewById(R.id.l_valid_senha);
		sizeItems();
		setSpinner();
		
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
					if(cursor.getCount() != 0 || txtLogin.getText().toString().isEmpty()){
						validLogin.setBackgroundResource(R.drawable.x1);
						validLoginB = false;
					} else  {
						validLogin.setBackgroundResource(R.drawable.ok);
						validLoginB = true;
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
		
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
	
		btnCadastro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String uf = ufSpn.getSelectedItem().toString();  
				if (nameEdit.getText().toString().isEmpty() || cityEdit.getText().toString().isEmpty() || phoneEdit.getText().toString().isEmpty() || validLoginB == false || validSenhaB == false || validPass1B == false) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
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
						DBUser user =  datasource.createUser(nameEdit.getText().toString(), cityEdit.getText().toString(), uf, phoneEdit.getText().toString(), txtLogin.getText().toString(), txtSenha.getText().toString(), loadBitmapFromView(iv));
						datasource.close();
						AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
						dialog.setTitle("Confirmation");
						dialog.setMessage("User: " + user.getName() + " registered");
						dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						});
						dialog.show();
					} catch (SQLiteException e) {
						Toast.makeText(Register.this, "Failure in Register", Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
	}
	
    public void setSpinner(){
    	ufList.add("AC");
    	ufList.add("AL");
    	ufList.add("AP");
    	ufList.add("AM");
    	ufList.add("BA");
    	ufList.add("CE");
    	ufList.add("DF");
    	ufList.add("ES");
    	ufList.add("GO");
    	ufList.add("MA");
    	ufList.add("MT");
    	ufList.add("MS");
    	ufList.add("MG");
    	ufList.add("PA");
    	ufList.add("PB");
    	ufList.add("PR");
    	ufList.add("PE");
    	ufList.add("PI");
    	ufList.add("RJ");
    	ufList.add("RN");
    	ufList.add("RS");
    	ufList.add("RO");
    	ufList.add("RR");
    	ufList.add("SC");
    	ufList.add("SP");
    	ufList.add("SE");
    	ufList.add("TO");
    	
    	ArrayAdapter<String> ArrayAdapterUF = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ufList);
		final ArrayAdapter<String> StateArrayAdapter = ArrayAdapterUF;
		StateArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
		ufSpn.setAdapter(StateArrayAdapter);
		
    }

    public void sizeItems(){
    	
   	 final DisplayMetrics metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 
		 nameEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/3);
		 cityEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);

		 phoneEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/6);
		 
		 txtLogin.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);
		 txtSenha.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);
		 txtConfirmPass.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);
		 
		 btnCadastro.setWidth(metrics.widthPixels/5+metrics.widthPixels/5+metrics.widthPixels/12);
		 btnReturn.setWidth(metrics.widthPixels/5+metrics.widthPixels/5+metrics.widthPixels/12);

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
}
