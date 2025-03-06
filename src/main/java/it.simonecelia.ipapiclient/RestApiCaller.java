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

	private static final String PAYLOAD = "{\"key\": \"value\"}";  // useless

	private static final int MINUTES = 10;

	public static void main ( String[] args ) {
		Executors.newSingleThreadScheduledExecutor ().scheduleAtFixedRate ( () -> {
			try {
				sendPutRequest ();
			} catch ( Exception e ) {
				System.err.println ( "Errore durante la chiamata: " + e.getMessage () );
			}
		}, 0, MINUTES, TimeUnit.MINUTES );
	}

	private static void sendPutRequest () throws Exception {
		var connection = getHttpURLConnection ();
		try ( var os = connection.getOutputStream () ) {
			var input = RestApiCaller.PAYLOAD.getBytes ( StandardCharsets.UTF_8 );
			os.write ( input, 0, input.length );
		}
		var responseCode = connection.getResponseCode ();
		System.out.println ( "Response code: " + responseCode );
	}

	private static HttpURLConnection getHttpURLConnection () throws URISyntaxException, IOException {
		var uri = new URI ( ENDPOINT );
		var url = uri.toURL ();
		var connection = (HttpURLConnection) url.openConnection ();
		connection.setRequestMethod ( "PUT" );
		connection.setRequestProperty ( "Content-Type", "application/json" );
		connection.setDoOutput ( false );  // true, send data in request's body
		return connection;
	}
}
