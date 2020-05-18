package de.bigmachines.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.core.util.Loader;

public class FileHelper {
	
	public static void copyFile(String source, String destination) {
		copyFile(new File(source), new File(destination));
	}
	
	public static void copyFile(File source, File destination) {
    	try {
    		FileInputStream inputStream = new FileInputStream(source);
			FileOutputStream outputStream = new FileOutputStream(destination);
			byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
            	outputStream.write(buffer, 0, length);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}