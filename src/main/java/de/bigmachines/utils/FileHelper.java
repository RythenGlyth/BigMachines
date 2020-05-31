package de.bigmachines.utils;

import de.bigmachines.BigMachines;
import org.apache.logging.log4j.core.util.Loader;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileHelper {
	
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
	
	public static HashMap<String, String> oldGetResourcesFolder(String folder) {
		final HashMap<String, String> files = new HashMap<>();
		
		try {
			//final ClassLoader loader = BigMachines.class.getClassLoader();
			
			//final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			//final InputStream in = loader.getResourceAsStream(folder);
			final InputStream in = Loader.getResource(folder, null).openStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			System.out.println("------------------------------");
			while(reader.ready()) {
				final String line = reader.readLine();
				System.out.println(line);
				final BufferedReader file = new BufferedReader(new InputStreamReader(Loader.getResource(folder + line, null).openStream()));
				files.put(line, file.lines().collect(Collectors.joining("\n")));
			}
			System.out.println("------------------------------");
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		return files;
	}
	
	public static HashMap<String, String> getResourcesFolder(String folder, String extensionName) {
		HashMap<String, String> files = new HashMap<>();
		Stream<Path> walker;
		try {
			URI uri = BigMachines.class.getResource(folder).toURI();
	    	Path myPath;
	    	if (uri.getScheme().equals("jar")) {
	    		FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
	    		myPath = fs.getPath(folder);
	    	} else
	    		myPath = Paths.get(uri);
	    	
	    	walker = Files.walk(myPath);
	    	for (Iterator<Path> it = walker.iterator(); it.hasNext(); ) {
	    		Path p = it.next();
	    		if(FileHelper.getExtension(p.getFileName().toString()).equals("json")) {
					final BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(
									BigMachines.class.getResource(folder + p.getFileName().toString())
									.openStream()
							)
					);
					
		    		files.put(p.getFileName().toString(), bufferedReader.lines().collect(Collectors.joining("\n")));
	    		}
	    	}
			walker.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return files;
	}
	
	public static String getExtension(String s) {
		String[] name = s.split("\\.");
		if (name.length > 0)
			return name[name.length - 1];
		else
			return "";
	}
	
	public static String getExtension(File f) {
		return getExtension(f.getName());
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
