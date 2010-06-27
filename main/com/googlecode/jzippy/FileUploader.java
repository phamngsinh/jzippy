package com.googlecode.jzippy;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 * Uploads files to a ZippyShare server.
 * @author ben
 */
public class FileUploader {
	
	private static final char QUOTE = '"';
	private static final Regex URL = new Regex("value=\\"+QUOTE+"(.*file\\.html)\\"+QUOTE);
	private final String server;
	private final String uploadId;
	private final File file;
	private String url;
	
	/**
	 * Creates a new file uploader.
	 * @param server The server subdomain (eg: www24).
	 * @param uploadId The upload ID.
	 * @param file The file to upload.
	 */
	public FileUploader(final String server, final String uploadId, final File file){
		this.server = server;
		this.uploadId = uploadId;
		this.file = file;
	}
	
	/**
	 * Uploads the file to the server.
	 * This will block until it has finished uploading.
	 * @throws IOException If the upload fails or there is no URL to be found.
	 */
	public void upload() throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost("http://"+server+".zippyshare.com/upload");
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/3.0 (compatible; Indy Library)");
		
		final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		final ContentBody body = new FileBody(file, new MimetypesFileTypeMap().getContentType(file));
		entity.addPart("uploadId", new StringBody(uploadId));
		entity.addPart(file.getName(), body);
		entity.addPart("private", new StringBody("yes"));
		post.setEntity(entity);
		
		final HttpResponse response = client.execute(post);
		final HttpEntity responseEntity = response.getEntity();
		final String responseStr = EntityUtils.toString(responseEntity);
		responseEntity.consumeContent();
		
		if(!URL.match(responseStr)){
			throw new IOException("Unable to find file URL");
		}
		
		this.url = URL.get(1);
		
		//Close connection
		client.getConnectionManager().shutdown();
	}
	
	/**
	 * Gets the URL of the uploaded file.
	 * Format: http://[SERVER].zippyshare.com/v/[FILE INDEX]/file.html
	 */
	public String getURL(){
		return url;
	}
}