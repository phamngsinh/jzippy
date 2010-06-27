package com.googlecode.jzippy;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Fetches the uploadId and the server from the main page.
 * @author ben
 */
public class SessionFetcher {

	public static final String DEFAULT_FETCH_URL = "http://beta.zippyshare.com";
	private final Regex uploadIdRegex = new Regex("var uploadId = '(.*)';");
	private final Regex serverRegex = new Regex("var server = '(.*)';");
	private final String url;
	private String uploadId;
	private String server;

	/**
	 * Creates a session fetcher with the {@link #DEFAULT_FETCH_URL}.
	 */
	public SessionFetcher(){
		this(DEFAULT_FETCH_URL);
	}

	/**
	 * Creates a session fetcher with a custom URL.
	 * @param url The URL to fetch from.
	 */
	public SessionFetcher(final String url){
		this.url = url;
	}

	/**
	 * Fetches the variables.
	 * @throws IOException If there is a connection error or the regex fails to match.
	 */
	public void fetch() throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet get = new HttpGet(url);
		final ResponseHandler<String> responseHandler = new BasicResponseHandler();
		final String responseBody = client.execute(get, responseHandler);
		
		if(!uploadIdRegex.match(responseBody)){
			throw new IOException("Unable to find uploadId");
		}
		
		if(!serverRegex.match(responseBody)){
			throw new IOException("Unable to find server");
		}
		
		uploadId = uploadIdRegex.get(1);
		server = serverRegex.get(1);
		
		//Close connection
		client.getConnectionManager().shutdown();
	}

	/**
	 * Gets the upload ID.
	 */
	public String getUploadId(){
		return uploadId;
	}

	/**
	 * Gets the server subdomain (eg: www24).
	 */
	public String getServer(){
		return server;
	}
}