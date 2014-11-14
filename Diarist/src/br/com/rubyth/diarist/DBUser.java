package br.com.rubyth.diarist;

import android.graphics.Bitmap;

public class DBUser {
	private long _id;
	private String name;
	private String city;
	private String state;
	private String phone;
	private String login;
	private String senha;
	private Bitmap picture;
	
	DBUser(long id, String name,String city, String state, String phone, String login, String senha, Bitmap picture){
		this._id = id;
		this.name = name;
		this.city = city;
		this.state = state;
		this.phone = phone;
		this.login = login;
		this.senha = senha;
		this.picture = picture;
	}
	
	
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Bitmap getPicture(){
		return picture;		
	}
	public void setFoto(Bitmap btm){
		this.picture= btm;
	}
}
