package br.com.rubyth.diarist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.BitmapFactory;

public class DBAdapter {
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	private String[] allColumnsSchedule = { DBHelper.IDSchedule, DBHelper.DAY, DBHelper.MONTH, DBHelper.YEAR, DBHelper.ID_DIARIST, DBHelper.ID_USER_SCHEDULE};
	private String[] allColumns = { DBHelper.ID, DBHelper.NAME, DBHelper.CITY, DBHelper.STATE , DBHelper.PRICE , DBHelper.PHONE, DBHelper.CPF, DBHelper.EMAIL, DBHelper.PICTURE, DBHelper.TOTAL_SERVICOS, DBHelper.TOTAL_AVALIACOES, DBHelper.RESULTADO_AVALIACOES};
	private String[] allColumnsUser = { DBHelper.ID_USER, DBHelper.NAME_USER, DBHelper.CITY_USER, DBHelper.STATE_USER, DBHelper.PHONE_USER, DBHelper.LOGIN_USER, DBHelper.SENHA_USER, DBHelper.PHOTO_USER};

	public DBAdapter(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public DBDiarist createDiarist(String name,String city, String state, String price, String phone, String cpf, String email, Bitmap picture, int total_servicos, int total_avaliacoes, double resultado_avaliacoes ) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.NAME, name);
		values.put(DBHelper.CITY, city);
		values.put(DBHelper.STATE, state);
		values.put(DBHelper.PRICE, price);
		values.put(DBHelper.PHONE, phone);
		values.put(DBHelper.CPF, cpf);
		values.put(DBHelper.EMAIL, email);
			
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		picture.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		byte[] photo = baos.toByteArray(); 
			
		values.put(DBHelper.PICTURE, photo);
		
		values.put(DBHelper.TOTAL_SERVICOS, total_servicos);
		values.put(DBHelper.TOTAL_AVALIACOES, total_avaliacoes);
		values.put(DBHelper.RESULTADO_AVALIACOES, resultado_avaliacoes);
		
			
		long insertId = database.insert(DBHelper.TABLE_NAME, null, values);
		// To show how to query
		Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns, DBHelper.ID + " = " + insertId, null,null, null, null);
		
		cursor.moveToFirst();
		return cursorToDiarist(cursor);
	}
	
	public int AddTotalServicos(long id, int total_servicos) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TOTAL_SERVICOS, total_servicos);
        return database.update(DBHelper.TABLE_NAME, values, DBHelper.ID + " = " + id, null);
    }
	
	public int AddTotalAvaliacoes(long id, int total_avaliacoes) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TOTAL_AVALIACOES, total_avaliacoes);
        return database.update(DBHelper.TABLE_NAME, values, DBHelper.ID + " = " + id, null);
    }
	
	public int AddResultAvaliacoes(long id, double resultado_avaliado) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.RESULTADO_AVALIACOES, resultado_avaliado);
        return database.update(DBHelper.TABLE_NAME, values, DBHelper.ID + " = " + id, null);
    }
	
	public void EliminaDiarist(int idContacto){
		//database.delete(DB.TABLE_NAME, "id=?", new String [] {Integer.toString(idContacto)});
		database.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = " + idContacto, null);
		}
	private DBDiarist cursorToDiarist(Cursor cursor) {
		byte[] blob = cursor.getBlob(cursor.getColumnIndex(DBHelper.PICTURE));
		DBDiarist contacto = null;
		if (blob.length > 0) {
	      	Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
	      	contacto = new DBDiarist(cursor.getLong(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4),
	    			cursor.getString(5), cursor.getString(6), cursor.getString(7),bmp, cursor.getInt(9), cursor.getInt(10), cursor.getDouble(11));
		} else {
			contacto = new DBDiarist(cursor.getLong(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4),
	    			cursor.getString(5), cursor.getString(6), cursor.getString(7), null, cursor.getInt(9), cursor.getInt(10), cursor.getDouble(11));
		}
		return contacto;
	}

	public Cursor getDiarist(){
		Cursor cursor = database.rawQuery("select _id, name, city, state, price, phone, cpf, email, picture, total_servicos, total_avaliacoes, resultado_avaliacoes from " + DBHelper.TABLE_NAME, null);
			return cursor;
		}
	public DBDiarist getdiarist(int idContacto){
		Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns, DBHelper.ID + " = " + idContacto, null,null, null, null);
			cursor.moveToFirst();
		return cursorToDiarist(cursor);
	}
	
	
	public DBSchedule createSchedule(int day, int month, int year, long id_diarist, long id_user) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.DAY, day);
		values.put(DBHelper.MONTH, month);
		values.put(DBHelper.YEAR, year);
		values.put(DBHelper.ID_DIARIST, id_diarist);
		values.put(DBHelper.ID_USER_SCHEDULE, id_user);
			
			
		long insertId = database.insert(DBHelper.TABLE_NAME_SCHEDULE, null, values);
		// To show how to query
		Cursor cursor = database.query(DBHelper.TABLE_NAME_SCHEDULE, allColumnsSchedule, DBHelper.IDSchedule + " = " + insertId, null,null, null, null);
		
		cursor.moveToFirst();
		return cursorToSchedule(cursor);
	}
	
	public void EliminaSchedule(int idContacto){
		//database.delete(DB.TABLE_NAME, "id=?", new String [] {Integer.toString(idContacto)});
		database.delete(DBHelper.TABLE_NAME_SCHEDULE, DBHelper.IDSchedule + " = " + idContacto, null);
		}
	
	private DBSchedule cursorToSchedule(Cursor cursor) {
		DBSchedule contacto = null;
		contacto = new DBSchedule(cursor.getLong(0), cursor.getInt(1), cursor.getInt(2),
				cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
		return contacto;
	}

	public Cursor getSchedule(long id_user){
		Cursor cursor = database.rawQuery("select *from " + DBHelper.TABLE_NAME_SCHEDULE+" WHERE id_user="+id_user, null);
			return cursor;
		}
	
	public DBSchedule getschedule(int idContacto){
		Cursor cursor = database.query(DBHelper.TABLE_NAME_SCHEDULE, allColumnsSchedule, DBHelper.IDSchedule + " = " + idContacto, null,null, null, null);
			cursor.moveToFirst();
		return cursorToSchedule(cursor);
		}
	
	public Cursor validateDay(int id_diarist){
		Cursor cursor = database.rawQuery("select day, month, year from "+ DBHelper.TABLE_NAME_SCHEDULE+" WHERE id_diarist ="+id_diarist, null);
		return cursor;
	}
	
	
	public DBUser createUser (String name,String city, String state, String phone, String login, String senha, Bitmap picture) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.NAME_USER, name);
		values.put(DBHelper.CITY_USER, city);
		values.put(DBHelper.STATE_USER, state);
		values.put(DBHelper.PHONE_USER, phone);
		values.put(DBHelper.LOGIN_USER, login);
		values.put(DBHelper.SENHA_USER, senha);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		picture.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		byte[] photo = baos.toByteArray(); 
			
		values.put(DBHelper.PHOTO_USER, photo);
			
		long insertId = database.insert(DBHelper.TABLE_NAME_USER, null, values);
		// To show how to query
		
		Cursor cursor = database.query(DBHelper.TABLE_NAME_USER, allColumnsUser, DBHelper.ID_USER + " = " + insertId, null,null, null, null);
		
		cursor.moveToFirst();
		return cursorToUser(cursor);
	}
	
	public void EliminaUser(int idContacto){
		//database.delete(DB.TABLE_NAME, "id=?", new String [] {Integer.toString(idContacto)});
		database.delete(DBHelper.TABLE_NAME_USER, DBHelper.ID_USER + " = " + idContacto, null);
		}
	
	private DBUser cursorToUser(Cursor cursor) {
		byte[] blob = cursor.getBlob(cursor.getColumnIndex(DBHelper.PHOTO_USER));
	      	Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
	      	
	      	
	      	DBUser contacto = new DBUser(cursor.getLong(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4),
	    			cursor.getString(5), cursor.getString(6), bmp);
		return contacto;
	}

	public Cursor getUser(){
		Cursor cursor = database.rawQuery("select *from " + DBHelper.TABLE_NAME_USER, null);
			return cursor;
		}
	public DBUser getuser(long idContacto){
		Cursor cursor = database.query(DBHelper.TABLE_NAME_USER, allColumnsUser, DBHelper.ID_USER + " = " + idContacto, null,null, null, null);
			cursor.moveToFirst();
		return cursorToUser(cursor);
	}
	
	public Cursor validateLogin(String login){
		Cursor cursor = database.rawQuery("select login from "+DBHelper.TABLE_NAME_USER+" WHERE login="+"'"+login+"';", null);
		return cursor;
	}
	
	public DBUser getlogin(String login){
		Cursor cursor = database.query(DBHelper.TABLE_NAME_USER, allColumnsUser, DBHelper.LOGIN_USER + "='"+login+"'", null, null, null, null);
			cursor.moveToFirst();
		return cursorToUser(cursor);
	}
	
	public int AlterUser(long id, String name, String login, String senha, String city, String state, String phone, Bitmap picture) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME_USER, name);
        values.put(DBHelper.LOGIN_USER, login);
        values.put(DBHelper.SENHA_USER, senha);
        values.put(DBHelper.CITY_USER, city);
        values.put(DBHelper.STATE_USER, state);
        values.put(DBHelper.PHONE_USER, phone);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		picture.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		byte[] photo = baos.toByteArray(); 
			
		values.put(DBHelper.PHOTO_USER, photo);
     
        return database.update(DBHelper.TABLE_NAME_USER, values, DBHelper.ID + " = " + id, null);
    }
}