package it.simonecelia.ipapiclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RestApiCaller {

	private static final String DEFAULT_ENDPOINT = "https://www.simonecelia.it/ipapi/be/ip/update.php";

	private static final int DEFAULT_MINUTES = 10;

	public static void main ( String[] args ) {
		var endpoint = ( args.length > 0 ) ? args[0] : DEFAULT_ENDPOINT;
		var minutes = ( args.length > 1 ) ? parseMinutes ( args[1] ) : DEFAULT_MINUTES;
		Executors.newSingleThreadScheduledExecutor ().scheduleAtFixedRate ( () -> {
			try {
				sendPutRequest ( endpoint, minutes );
			} catch ( Exception e ) {
				System.err.println ( "Error calling the API: " + e.getMessage () );
			}
		}, 0, minutes, TimeUnit.MINUTES );
	}

	private static int parseMinutes ( String arg ) {
		try {
			var value = Integer.parseInt ( arg );
			return ( value > 0 ) ? value : DEFAULT_MINUTES;
		} catch ( NumberFormatException e ) {
			System.err.println ( "Invalid minutes value, using default: " + DEFAULT_MINUTES );
			return DEFAULT_MINUTES;
		}
	}

	private static void sendPutRequest ( String endpoint, int minutes ) throws Exception {
		var responseCode =  getHttpURLConnection ( endpoint ).getResponseCode ();
		var message = String.format ( "%s Sent request to: %s, response code: %d, next call in %d minutes", now (), endpoint, responseCode, minutes );
		System.out.println ( message );
	}

	private static String now () {
		return "[" + LocalDateTime.now ().format ( DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm:ss" ) ) + "]";
	}

	private static HttpURLConnection getHttpURLConnection ( String endpoint ) throws URISyntaxException, IOException {
		var connection = (HttpURLConnection) new URI ( endpoint ).toURL ().openConnection ();
		connection.setRequestMethod ( "PUT" );
		connection.setRequestProperty ( "Content-Type", "application/json" );
		connection.setDoOutput ( true );
		return connection;
	}
}
