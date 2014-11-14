package br.com.rubyth.diarist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class NewDiarist extends Activity {

	private AlertDialog alerta;
	private List<String> ufList = new ArrayList<String>();
	private List<String> priceList = new ArrayList<String>();
	
	EditText nameEdit;
	EditText cityEdit;
	Spinner priceSpn;
	EditText phoneEdit;
	EditText cpfEdit;
	EditText mailEdit;
	Spinner ufSpn;
	ImageView iv;
	
	Button btnCadastro;
	Button btnReturn;
	final static int cameraData = 0;
	private DBAdapter datasource;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_diarist);
        
        datasource = new DBAdapter(this);
        nameEdit = (EditText) findViewById(R.id.d_name);
        cityEdit = (EditText) findViewById(R.id.d_city);
        priceSpn = (Spinner) findViewById(R.id.d_price);
        
        phoneEdit = (EditText) findViewById(R.id.d_tel);
        phoneEdit.addTextChangedListener(Mask.insert("(##)#####-####", phoneEdit));
        
        cpfEdit = (EditText) findViewById(R.id.d_cpf);
        cpfEdit.addTextChangedListener(Mask.insert("###.###.###-##", cpfEdit));
        
        mailEdit = (EditText) findViewById(R.id.d_mail);
        
        ufSpn = (Spinner) findViewById(R.id.d_state);
        
        btnCadastro = (Button) findViewById(R.id.btn_register_d);
        btnReturn = (Button) findViewById(R.id.btn_return_d);
        
		iv = (ImageView) findViewById(R.id.ivReturnedPic);
		
        setSpinner();
        sizeItems();
        nameEdit.requestFocus();
        
        iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogImage();
				
			}
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
				String price = priceSpn.getSelectedItem().toString();  
				if (nameEdit.getText().toString().isEmpty() || cityEdit.getText().toString().isEmpty() || phoneEdit.getText().toString().isEmpty()) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(NewDiarist.this);
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
						DBDiarist d =  datasource.createDiarist(nameEdit.getText().toString(), cityEdit.getText().toString(), uf, price, phoneEdit.getText().toString(), cpfEdit.getText().toString(), mailEdit.getText().toString(), loadBitmapFromView(iv), 0, 0, 0);
						datasource.close();
						AlertDialog.Builder dialog = new AlertDialog.Builder(NewDiarist.this);
						dialog.setTitle("Confirmation");
						dialog.setMessage("Diarist: " + d.getName() + " registered");
						dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						});
						dialog.show();
					} catch (Exception e) {
						Toast.makeText(NewDiarist.this, "Failure in Register", Toast.LENGTH_SHORT).show();
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

    	priceList.add("R$ 10,00");
    	priceList.add("R$ 15,00");
    	priceList.add("R$ 20,00");
    	priceList.add("R$ 25,00");
    	priceList.add("R$ 30,00");
    	priceList.add("R$ 35,00");
    	priceList.add("R$ 40,00");
    	priceList.add("R$ 45,00");
    	priceList.add("R$ 50,00");
    	priceList.add("R$ 55,00");
    	priceList.add("R$ 60,00");
    	priceList.add("R$ 65,00");
    	priceList.add("R$ 70,00");
    	priceList.add("R$ 75,00");
    	priceList.add("R$ 80,00");
    	
    	ArrayAdapter<String> ArrayAdapterUF = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ufList);
		final ArrayAdapter<String> StateArrayAdapter = ArrayAdapterUF;
		StateArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
		ufSpn.setAdapter(StateArrayAdapter);
		
		ArrayAdapter<String> ArrayAdapterPrice= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, priceList);
		final ArrayAdapter<String> PriceArrayAdapter = ArrayAdapterPrice;
		PriceArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
		priceSpn.setAdapter(PriceArrayAdapter);
    }
    
    public void sizeItems(){
    	
    	 final DisplayMetrics metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		 priceSpn.setMinimumWidth(metrics.widthPixels/5);
		 
		 
		 nameEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/3);
		 
		 cityEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/8);

		 phoneEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/6);
		 
		 mailEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/3);
		 
		 
		 cpfEdit.setWidth(metrics.widthPixels/2+metrics.widthPixels/6);
		 
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