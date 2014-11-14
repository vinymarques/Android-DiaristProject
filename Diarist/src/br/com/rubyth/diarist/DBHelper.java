package br.com.rubyth.diarist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DBHelper extends SQLiteOpenHelper{

	protected static final String DATABASE_NAME = "NewDiarist.db";
    public static final String TABLE_NAME = "Diarist";
    protected static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PRICE = "price";
    public static final String PHONE = "phone";
    public static final String CPF= "cpf";
    public static final String EMAIL = "email";
    public static final String PICTURE = "picture";
    public static final String TOTAL_SERVICOS = "total_servicos";
    public static final String TOTAL_AVALIACOES = "total_avaliacoes";
    public static final String RESULTADO_AVALIACOES = "resultado_avaliacoes";
    private static final String DATABASE_CREATE = "create table "
    		+ TABLE_NAME + "( " + ID
    		+ " integer primary key autoincrement, " + NAME
    		+ " text not null, " + CITY + " text not null, " + STATE + " text not null, " + PRICE + " text not null, "
    		+ PHONE+" text not null," + CPF + " text, " + EMAIL+ " text, " + PICTURE+" BLOB, "
    		+ TOTAL_SERVICOS+" int, "
    		+ TOTAL_AVALIACOES+" int, "
    		+ RESULTADO_AVALIACOES+" double);";
    
    public static final String TABLE_NAME_USER = "User";
    
    public static final String IDSchedule = "_id";
  	public static final String DAY = "day";
  	public static final String MONTH = "month";
  	public static final String YEAR = "year";
  	public static final String ID_DIARIST = "id_diarist";
  	public static final String ID_USER_SCHEDULE = "id_user";
    public static final String TABLE_NAME_SCHEDULE = "scheduling";
    private static final String DATABASE_CREATE_SCHEDULE = "create table scheduling(" 
    				+ " _id integer primary key autoincrement,"
    				+ " day integer not null,"
    				+ " month integer not null,"
    				+ " year integer not null,"
    				+ " id_diarist integer not null,"
    				+ " id_user integer not null,"
    				+ " FOREIGN KEY(id_diarist) REFERENCES "+TABLE_NAME+" (_id),"
    				+ " FOREIGN KEY(id_user) REFERENCES "+TABLE_NAME_USER+" (_id));";
    
    public static final String ID_USER = "_id";
    public static final String NAME_USER = "name";
    public static final String CITY_USER = "city";
    public static final String STATE_USER = "state";
    public static final String PHONE_USER = "phone";
    public static final String LOGIN_USER = "login";
    public static final String SENHA_USER = "senha";
    public static final String PHOTO_USER = "photo";
    private static final String DATABASE_CREATE_USER = "create table "+TABLE_NAME_USER+"(" 
			+ID_USER+" integer primary key autoincrement,"
			+NAME_USER+" text not null,"
			+CITY_USER+" text,"
			+STATE_USER+" text,"
			+PHONE_USER+" text,"
			+LOGIN_USER+" text,"
			+SENHA_USER+" text,"
			+PHOTO_USER+" blob);";
    
   public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }
   @Override
   public void onCreate(SQLiteDatabase db) {
           db.execSQL(DATABASE_CREATE);
           db.execSQL(DATABASE_CREATE_SCHEDULE);
           db.execSQL(DATABASE_CREATE_USER);
   }
   
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           Log.w(DBHelper.class.getName(),
           "Upgrading database from version " + oldVersion + " to "
           + newVersion + ", which will destroy all old data");
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SCHEDULE);
          db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
          onCreate(db);
  }
}

