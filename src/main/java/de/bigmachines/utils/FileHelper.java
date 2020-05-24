package de.bigmachines.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.core.util.Loader;

import de.bigmachines.config.ManualLoader;

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
            
            inputStream.close();
            outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<File> getResourcesFolder(String folder) {
		List<File> files = new ArrayList<File>();
		Stream<Path> walker;
		try {
			URI uri = ManualLoader.class.getResource(folder).toURI();
	    	Path myPath;
	    	if (uri.getScheme().equals("jar")) {
	    		FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String, Object> emptyMap());
	    		myPath = fs.getPath(folder);
	    	} else
	    		myPath = Paths.get(uri);
	    	
	    	walker = Files.walk(myPath, 1);
	    	for (Iterator<Path> it = walker.iterator(); it.hasNext(); ) {
	    		File f = it.next().toFile();
	    		if (f.isFile())
	    			files.add(f);
	    	}
			walker.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return files;
	}
	
	public static String getExtension(File f) {
		String[] name = f.getName().split("\\.");
		if (name.length > 0)
			return name[name.length - 1];
		else
			return "";
	}
	
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
