package br.com.rubyth.diarist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class DiaristCursorAdapter extends ResourceCursorAdapter{

	int idDiarist;
	DBAdapter datasource;
	DBDiarist diarist;
	
	@SuppressWarnings("deprecation")
	public DiaristCursorAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView name = (TextView) view.findViewById(R.id.name_list);
		name.setText(cursor.getString(cursor.getColumnIndex("name")));

		TextView price = (TextView) view.findViewById(R.id.price_list);
		price.setText(cursor.getString(cursor.getColumnIndex("price")) + "  - ");
		
		TextView address = (TextView) view.findViewById(R.id.address_list);
		address.setText(cursor.getString(cursor.getColumnIndex("city")) + " / " +
		(cursor.getString(cursor.getColumnIndex("state"))));
		
		ImageView photo = (ImageView) view.findViewById(R.id.image_list);
		
		idDiarist = cursor.getInt(cursor.getColumnIndex("_id"));
		datasource = new DBAdapter(context);
		datasource.open();
		diarist = datasource.getdiarist(idDiarist);
		datasource.close();
		photo.setImageBitmap(diarist.getPicture());
		
	}
}
