package it.nttdata.jdbcusers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Repository  {
	private Connection connection;
	//public per poterlo provare subito
	//private ....... e poi definire costruttore e al suo interno chiami connect
	
	
	//DESIGN PATTERN: SINGLETON: ci permette di fare condivisione della nostra istanza
	private volatile static Repository instance = null;
	//realizzare una nuova istanza di Repository, metodo publico ma di metodo statico che mi ritorna l'istanza definita
	//se questa è diversa da null
	//se invece è la prima volta instance è una new 
	//e per sincronizzare l'accesso al metodo e funziona su programmi multi thread
	
	// in java è possibile dire di salvare la variabile instance non nella cache nei processori
	//ma di salvare questa var nella RAM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//la RAM è molto più ampia, keyword volatile  salva li!
	public synchronized static Repository getInstance() throws SQLException {
		if(instance == null) {
			synchronized (Repository.class) {
				if(instance == null) instance = new Repository();
			}
		}
		return instance;
	}// al primo avvio condivide il costruttore , guarda main.java
	
	public Repository() throws SQLException  {
		connect();
	}
	//CONNESSIONE CON DB
	/**
	 * metodo che ci connette al db,  verificare l'esistenza della classe driver
	 * DriverManager ha un metodo statico > che può essere richiamato senza dover istanziare di nuovo l'oggetto di quella classe.
	 * @throws SQLException
	 */
	private void connect() {
		//metodo che ci connette al db
		//verificare l'esistenza della classe driver
		try {
			Class.forName("org.mariadb.jdbc.Driver"); //check del package del driver
			
			String url = "jdbc:mariadb://localhost:3306/dbprova?user=root&password=";//?parametri da inviare
			//DriverManager ha un metodo statico > che può essere richiamato senza dover istanziare di nuovo l'oggetto di quella classe
			 connection = DriverManager.getConnection(url);
	
		} catch (ClassNotFoundException |SQLException  e) {
			e.printStackTrace();
		}
	}
	

	/*
	 * Metodo per recuperare tutti gli utenti della tabella che ho creato su phpmyadmin
	 * Usiamo:
	 * List è un'interfaccia
	 * e ArrayList è un'implementazione di List
	 * */
	public List<User> findAll(){
		//List è un'interfaccia
		//ArrayList è un'implementazione di List
		List<User> users = new ArrayList<User>();
		
		try {
			//recuperiamo dati da DB: query SQL:
			String sql = "SELECT * FROM utenti ORDER BY nome ASC;";
			
			//per mandare in esecuzione questa query sul nostro db
			//connection al DB ci restituisce un'istanza di statement
			Statement stmt = connection.createStatement();
			ResultSet res = stmt.executeQuery(sql); // qua avrò il risultato della query "SELECT ALL ..."
			//OGGETTI ITERABILI e 
			//MAP
		
			//cos'è un Set? >> è un oggetto iterabile, come List.. è possibile ciclare sul SET per recuperare i dati
			while(res.next()) {
				// inizia a puntare al primo elemento finchè nn li hai visti tutti quanti
				// non possiamo usare forEach, bisogna sapere il tipo iesimo della mia collezione.. List di user elem di foreach è user
				//set ritorna un cursore che ci permette di muoverci in una entry del databaase, al nostro db
				//quest res mi da i valori di questo cursore
				User user = new User();
				/*user.setId(res.getInt(0)); //get int colonna 0
				user.setNome(res.getString(1));
				user.setCognome(res.getString(2));
				user.setLatitudine(res.getDouble(3));
				user.setLongitudine(res.getDouble(4));
				
				 se qualcuno aggiunge o modifica la tabella però,  l'indice della colonna non può essere cosi statico.
				 va recuperato in maniera dinamica
				*/
				user.setId(res.getInt(res.findColumn("id"))); //indice tramite nome colonna! fino a qualche tmepo fa
				user.setNome(res.getString("nome")); //getString già include il findColumn qua ci vanno i nomi delle colonne del db
				user.setCognome(res.getString("cognome"));
				user.setLatitudine(res.getDouble("latitudine"));
				user.setLongitudine(res.getDouble("longitudine"));
				
				users.add(user);
			}
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	

	/*
	 * Metodo per inserire un utente nella tabella
	 * la stringa sql è una string parametrica, quindi qualcun altro va a mappare i valori (inserendo ?,? ...)
	 * 
	 * return boolean: true, se la mia query ritorna un int = 1 che corrisponde al num di righe manipolate
	 * */
	public boolean insert (User user) {
		
		try {
			String sql = "INSERT INTO utenti VALUES (NULL, ?, ?, ?, ?)"; //stringa parametrica qualcun altro li va a mappare
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getNome());
			stmt.setString(2, user.getCognome());
			stmt.setDouble(3, user.getLatitudine());
			stmt.setDouble(4, user.getLongitudine());
			
			return stmt.executeUpdate() == 1; //effettua la query sul mio db e mi ritorna qualcosa: un intero! 
			//corrisponde al numero di righe manipolate da questa query sql
			//qual è il numero di righe manipolate da noi? 1!
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public boolean insert (List<User> users) {
		
		boolean[] results = new boolean[users.size()];
		try {

			//auto commmit non devi farlo, non deve andare ad eseguire ogni volta che è pronta una query, 
			//metti tutte insieme, dopo il ciclo for fai il commit... se qualcosa va storto annulla le operazioni!!
			connection.setAutoCommit(false);
			
			int i = 0;
			for(User user : users) {
				String sql = "INSERT INTO utenti VALUES (NULL, ?, ?, ?, ?)"; //stringa parametrica qualcun altro li va a mappare
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setString(1, user.getNome());
				stmt.setString(2, user.getCognome());
				stmt.setDouble(3, user.getLatitudine());
				stmt.setDouble(4, user.getLongitudine());
					
				results[i] = stmt.executeUpdate() == 1; //return sostituito con results (array di booleani)  
				//elemento iesimo di results è uguale a quello che c'è a destra
				i++;
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
			return false;
	}
	
	
	public boolean delete(User user) {
		try {
			String sql = "DELETE FROM utenti WHERE id = " + user.getId();
			Statement stmt = connection.createStatement();
			return stmt.executeUpdate(sql) == 1 ;//ci ritorna il num delle righe = 1!
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(User user) {
		try {
			String sql="UPDATE utenti SET nome = ?, cognome = ?, latitudine = ?, longitudine = ? WHERE id=? " ;
			//ALTER TABLE MODIFCA LA TABELLA, qua facciamo un UPDATE dei dati
					
		   PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, user.getNome());
			stmt.setString(2, user.getCognome());
			stmt.setDouble(3, user.getLatitudine());
			stmt.setDouble(4, user.getLongitudine());
			stmt.setInt(5, user.getId());
			
			return stmt.executeUpdate() == 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
