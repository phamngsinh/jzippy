package com.googlecode.jzippy;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("Fetching session info...");
		
		final SessionFetcher fetcher = new SessionFetcher();
		fetcher.fetch();
		
		final String uploadId = fetcher.getUploadId();
		final String server = fetcher.getServer();
		
		System.out.println("uploadId = "+uploadId);
		System.out.println("server = "+server);
		
		final FileUploader uploader = new FileUploader(server, uploadId, new File("test.txt"));
		uploader.upload();
		
		System.out.println("File uploaded!");
		System.out.println("URL = "+uploader.getURL());
	}
}