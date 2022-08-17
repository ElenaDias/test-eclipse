package it.nttdata.jdbcusers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Request {	// Request -- ci consente di fare le richieste internet
	//connessione internet che scarica i dati da internet:
	public static List<User> downloadUsers(){ // metodo static che ritorna una lista di utenti
		
		List<User> users = new ArrayList<User>();
		
		try {
			//porta 80 di default http
			String address = "http://jsonplaceholder.typicode.com/users"; // INDIRIZZO DOVE SCARICARE RISORSE -- UNA STRINGA
			//VALIDO L'INDIRZZO
			URL url = new URL(address); //oggeto URL oltre a darmi connessione, mi fa anche dei controlli sull'indirizzo che ho definito
			//APRO UNA CONNESSIONE E RETURN UNA CONNESSIONE DI TIPO HTTP
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //quando apro una connessione possono esserci diversi problemi
			
			connection.setRequestMethod("GET");
			//no parametri nel body della richiesta
			
			//resp code dev'essere 200 pquando tutto ok!!
			int code = connection.getResponseCode();
			if(code == HttpURLConnection.HTTP_OK) { // se questo code è uguale al codice 200 -- HTPP_OK -- 
				//se code 200
				InputStream in = connection.getInputStream();  //flusso di dati in input  >> la response del server 
				//Se so che è di tipo testuale per andare a leggere lo stream in ingresso, posso usare bufferedreader
				BufferedReader reader = new BufferedReader(new InputStreamReader(in)); //nel constructor vuole un oggetto che estende la classe reader
				//input stream reader 
				
				/*Reader che mi consente di leggere l'output del server
				input applicativo 
				se questo è un testo riga x riga
				*/
				
				String result = "";
				String line; // riga che aggiornerò 
				//ciclo finchè ho qualche riga da leggere nelal risposta del server
				while( (line = reader.readLine()) != null ) { //line assegno il val di reader.readLine
					//dammi la singola riga fino a capo
					
					result += line; // concateno valore di line e del result
					//quando arrivo a null si conclude il mio ciclo
				}
				/*
				 * [  
				 * {    
				 * "id": 1,    
				 * "name": "Leanne Graham",   
				 *  "username": "Bret",    
				 *  "email": "Sincere@april.biz",    
				 *  "address": {      
				 *  	"street": "Kulas Light",      
				 *  	"suite": "Apt. 556",      
				 *  	"city": "Gwenborough",      
				 *  	"zipcode": "92998-3874",      
				 *  	"geo": {        
				 *  		"lat": "-37.3159",        
				 *  		"lng": "81.1496"      
				 *  	}    
				 *  },    
				 *  "phone": "1-770-736-8031 x56442",    
				 *  "website": "hildegard.org",    
				 *  "company": {      
				 *  	"name": "Romaguera-Crona",      
				 *  	"catchPhrase": "Multi-layered client-server neural-net",      
				 *  	"bs": "harness real-time e-markets"    
				 *  	}  
				 *  },  
				 * */
				
				//System.out.println(result);
				//Devo decodificare quel JSON:
				JSONArray array = new JSONArray(result);
				
				
				for(int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					
					if(item != null ){
						User user = new User();
						String name = item.optString("name"); //recupero il dato per com'è su jsonplaceholder
						if(name != null) {
							String[] n = name.split(" ");
							if(n.length >= 2 ) {
								user.setNome(n[0]);
								user.setCognome(n[1]);
							}
						}
						JSONObject addr = item.optJSONObject("address");
						if(addr != null) {
							JSONObject geo = addr.optJSONObject("geo");
							if(geo != null) {
								user.setLatitudine(geo.optDouble("lat"));
								user.setLongitudine(geo.optDouble("lng"));
							}
						}
						users.add(user); //ADESSO POSSIAMO ANDARE A METTERE GLI USER NEL NOSTRO DB
					}
				}
			}
//RICORDARSI DI CHIUDERE LA CONNESSIONE, QUANDO HO FINITO DI LAVORARE CON ESSA
			connection.disconnect();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) { //possono esserci errori nella risposta 404, page not found, bad request, internal server error, ecc ecc
			e.printStackTrace();
		}
		
		return users;
	}
}
