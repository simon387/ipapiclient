package it.simonecelia.ipapiclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RestApiCaller {

	private static final String ENDPOINT = "https://www.simonecelia.it/ipapi/be/ip/update.php";

	private static final String PAYLOAD = "{\"key\": \"value\"}";  // Dati da inviare nel corpo della richiesta PUT (se necessario)

	public static void main ( String[] args ) {
		Executors.newSingleThreadScheduledExecutor ().scheduleAtFixedRate ( () -> {
			try {
				sendPutRequest ();
			} catch ( Exception e ) {
				System.err.println ( "Errore durante la chiamata: " + e.getMessage () );
			}
		}, 0, 1, TimeUnit.MINUTES );  // Inizia subito e ripete ogni minuto
	}

	private static void sendPutRequest () throws Exception {
		// Creazione della URL
		var connection = getHttpURLConnection ();

		// Scrivi il payload nel corpo della richiesta
		try ( var os = connection.getOutputStream () ) {
			var input = RestApiCaller.PAYLOAD.getBytes ( StandardCharsets.UTF_8 );
			os.write ( input, 0, input.length );
		}

		// Ottieni il codice di risposta
		var responseCode = connection.getResponseCode ();
		System.out.println ( "Codice risposta: " + responseCode );

		// Puoi gestire la risposta se necessario (ad esempio leggere il corpo della risposta)
		if ( responseCode == HttpURLConnection.HTTP_OK ) {
			System.out.println ( "Richiesta eseguita correttamente." );
		} else {
			System.out.println ( "Errore durante l'esecuzione della richiesta." );
		}
	}

	private static HttpURLConnection getHttpURLConnection () throws URISyntaxException, IOException {
		var uri = new URI ( ENDPOINT );
		// Crea la connessione utilizzando la URI
		var url = uri.toURL ();
		// Apri la connessione
		var connection = (HttpURLConnection) url.openConnection ();

		// Imposta il metodo HTTP come PUT
		connection.setRequestMethod ( "PUT" );
		connection.setRequestProperty ( "Content-Type", "application/json" );
		connection.setDoOutput ( true );  // Indica che invieremo dati nel corpo della richiesta
		return connection;
	}
}
