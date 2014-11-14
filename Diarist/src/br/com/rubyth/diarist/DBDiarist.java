package br.com.rubyth.diarist;

import android.graphics.Bitmap;

public class DBDiarist {

	private long _id;
	private String name;
	private String city;
	private String state;
	private String price;
	private String phone;
	private String cpf;
	private String email;
	private Bitmap picture;
	private int total_servicos;
	private int total_avaliacoes;
	private double resultado_avaliacoes;
	
	public DBDiarist(long id, String name,String city, String state, String price, String phone, String cpf, String email, Bitmap picture, int total_servicos, int total_avaliacoes, double resultado_avaliacoes){
		this._id = id;
		this.name = name;
		this.city = city;
		this.state = state;
		this.price = price;
		this.phone = phone;
		this.cpf = cpf;
		this.email = email;
		this.picture = picture;
		this.total_servicos = total_servicos;
		this.total_avaliacoes = total_avaliacoes;
		this.resultado_avaliacoes = resultado_avaliacoes;
	}
	
	public int getTotalServicos() {
		return total_servicos;
	}
	public void setTotalServicos(int total_servicos) {
		this.total_servicos = total_servicos;
	}
	
	public int getTotalAvaliacoes() {
		return total_avaliacoes;
	}
	public void setTotalAvaliacoes(int total_avaliacoes) {
		this.total_avaliacoes = total_avaliacoes;
	}
	
	public double getResultadoAvaliacoes() {
		return resultado_avaliacoes;
	}
	public void setResultadoAvaliacoes(int resultado_avaliacoes) {
		this.total_avaliacoes = resultado_avaliacoes;
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
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCPF() {
		return cpf;
	}
	public void setCPF(String cpf) {
		this.cpf = cpf;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Bitmap getPicture(){
		return picture;		
	}
	public void setFoto(Bitmap btm){
		this.picture= btm;
	}
}
