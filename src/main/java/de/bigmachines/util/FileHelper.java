package de.bigmachines.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.core.util.Loader;

public class FileHelper {
	
	public static void copyFileUsingStreamAndLoader(String source, File dest) {
		try {
			InputStream inputStream = Loader.getResource(source, null).openStream();
			OutputStream outputStream = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
