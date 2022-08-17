package it.nttdata.jdbcusers;

public class User {
	private int id;
	private String nome;
	private String cognome;
	private double latitudine;
	private double longitudine;
	
	//default constructor
	public User () {
	}
	//custom constructor
	public User (String nome,String cognome) {
		this.nome = nome;
		this.cognome = cognome;
	}
	
	
	//GETTERS AND SETTERS:	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public double getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}
	public double getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}
}
