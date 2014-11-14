package br.com.rubyth.diarist;

public class DBSchedule {

	private long _id;
	private int day;
	private int month;
	private int year;
	private long id_diarist;
	private long id_user;
	
	public DBSchedule (long _id, int day, int month, int year, long id_diarist, long id_user){
		this._id = _id;
		this.day = day;
		this.month = month;
		this.year = year;
		this.id_diarist = id_diarist;
		this.id_user = id_user;
	}
	
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	
	public int getDay(){
		return this.day;
	}
	public void setDay(int day){
		this.day = day;
	}
	
	public int getMonth(){
		return this.month;
	}
	public void setMonth(int month){
		this.month = month;
	}
	
	public int getYear(){
		return this.year;
	}
	public void setYear(int year){
		this.year = year;
	}
	
	public long getIdDiarist(){
		return this.id_diarist;
	}
	public void setIdDiarist(long id_diarist){
		this.id_diarist = id_diarist;
	}
	
	public long getIdUser(){
		return this.id_user;
	}
	public void setIdUser(long id_user){
		this.id_user = id_user;
	}
}
