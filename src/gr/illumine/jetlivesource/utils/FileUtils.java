package gr.illumine.jetlivesource.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	public static void createAsciiFile( String path , String content) throws IOException {
		FileWriter fstream = new FileWriter(path);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(content);
		out.close();
	}
	
	public static void createAsciiFile(String path ,String fileName, String content) throws IOException {
		FileWriter fstream = new FileWriter(
				path + File.separator + fileName );
		BufferedWriter out = new BufferedWriter(fstream);

		out.write(content);

		out.close();
	}
	
}
