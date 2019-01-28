package ict_add_on;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class utility that downloads a file from a URL in a specific folder
 * @author shahaal
 *
 */
public class HttpDownloadUtility {
	
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Downloads a file from a URL
	 * 
	 * @param fileURL
	 *            HTTP URL of the file to be downloaded
	 * @param saveDir
	 *            path of the directory to save the file
	 * @throws IOException
	 */
	
	public static File downloadFile(String fileURL, String saveDir) throws IOException {
		
		// file to be returned after download
		File file = null;
		
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {

					fileName = disposition.substring(index + 9, disposition.length());
				}
				
			} else {

				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			System.out.println("Content-Type = " + contentType);
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			
			String filePath = saveDir + File.separator + fileName;
			
			// create the new file to be returned
			file = new File(filePath);
			
			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(file);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			
			System.out.println("File downloaded");
			
			outputStream.close();
			inputStream.close();
			httpConn.disconnect();
			return file;
			
		}
		
		System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		return file;
		
	}
}
