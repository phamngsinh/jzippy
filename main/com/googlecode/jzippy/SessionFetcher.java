package com.googlecode.jzippy;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class SessionFetcher {

	public static final String DEFAULT_FETCH_URL = "http://beta.zippyshare.com";
	private final Regex uploadIdRegex = new Regex("var uploadId = '(.*)';");
	private final Regex serverRegex = new Regex("var server = '(.*)';");
	private final String url;
	private String uploadId;
	private String server;

	public SessionFetcher(){
		this(DEFAULT_FETCH_URL);
	}

	public SessionFetcher(final String url){
		this.url = url;
	}

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
		
		//CloseConnection
		client.getConnectionManager().shutdown();
	}

	public String getUploadId(){
		return uploadId;
	}

	public String getServer(){
		return server;
	}
}