package it.nttdata.jdbcusers;

import java.sql.SQLException;
import java.util.List;

public class Main {

	public static void main(String[] args) throws SQLException {
		/*
		1)per poter creare la connessione al mio db, devo scaricare da google: mariadb java connector
		product: JAVA8 connector - version: 2.7.4 - click download ---- .jar è un eseguibile quindi potrebbe dare errore nel download, ma basta dare il consenso
		.jar file archivio java, esportazione progetto java in questo caso insieme di librerie di supporto
		
		2)per includere la libreria build path path > add build path e Crea Referenced Libraries
		3) creare connessione con DB >> Classe dei Driver : Referenced Libraries > mariadb.jdbc > Driver Class
		Creo Repository : tutti metodi per la connessione a db*/
		
		
		//creaimo istanza di repository
		//Repository repo = new Repository();
		
		//DESIGN PATTERN SINGLETON:
		Repository repo = Repository.getInstance();
		/*try {
			repo.connect(); // questo lo facevamo se il metodo connect fosse stato public
		} catch (SQLException e) {
			e.printStackTrace();
		} */
		
		
		//FIND ALL USERS 
		System.out.println("******* FIND ALL USERS ON MY DB PROVA! *******");
		List<User> data = repo.findAll();
		for(User user : data) {
			System.out.println(user.getNome() + " " + user.getCognome());
		}
		System.out.println("********** INSERT *********");
		
		
		//INSERIMENTO UTENTE:
		User u = new User("paolo", "rossi");
		repo.insert(u);
		data = repo.findAll();
		for(User user : data) { 
			System.out.println(user.getNome() + " " + user.getCognome());
		}
		
		//DELETE
		System.out.println("********** DELETE *********");
		for(User user : data) {
			if(user.getId() > 1) repo.delete(user);
		}
		data = repo.findAll();
		for(User user : data) {
			System.out.println(user.getNome() + " " + user.getCognome());
		}
		
		//UPDATE
		System.out.println("********** UPDATE *********");
		if(data.size() > 0) {
			User user = data.get(0); //recupero utente con indice zero
			user.setNome("Giorgio"); // cambio nome
			user.setCognome("Verri"); //cambio cognome
			repo.update(user); //avviene l'update dell'user
		}
		data = repo.findAll();
		for(User user : data) {
			System.out.println(user.getNome() + " " + user.getCognome());
		}
		
		
		//Request HTTP 
		System.out.println("********** REQUEST *********");
		// libreria JSON IN JAVA
		List<User> users = Request.downloadUsers();
		repo.insert(users);
		/*
		for(User user : users) {
			repo.insert(user);
		}*/
		
		
		data = repo.findAll();
		for(User user : data) {
			System.out.println(user.getNome() + " " + user.getCognome());
		}
		
	}

}
