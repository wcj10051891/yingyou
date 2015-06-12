package xgame.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public abstract class IOUtils {
	public static String read(String filepath) {
		StringBuilder result = new StringBuilder();
		Scanner scan = null;
		String lineSeparator = System.getenv("line.separator");
		try {
			scan = new Scanner(bufferedIn(new FileInputStream(new File(filepath))));
			while(scan.hasNextLine()) {
				result.append(scan.nextLine());
				if(scan.hasNextLine())
					result.append(lineSeparator);
			}
			return result.toString();
		} catch (Exception e) {
			throw new RuntimeException("IoUtils.read error.", e);
		} finally {
			if(scan != null)
				scan.close();
		}
	}
	
	public static BufferedInputStream bufferedIn(InputStream in) {
		return new BufferedInputStream(in);
	}
	
	public static BufferedOutputStream bufferedOut(OutputStream out) {
		return new BufferedOutputStream(out);
	}
	
	public static BufferedReader bufferedReader(Reader reader) {
		return new BufferedReader(reader);
	}
	
	public static BufferedWriter bufferedWriter(Writer writer) {
		return new BufferedWriter(writer);
	}
	
	public static void deleteFile(File file) {
		if(file.isDirectory())
			for (File f : file.listFiles())
				deleteFile(f);
		file.delete();
	}
	
	public static File writeFile(String parentDir, String filename, String fileContent) throws IOException {
		File outFile = new File(parentDir, filename);
		if (!outFile.exists()) {
			outFile.getParentFile().mkdirs();
			outFile.createNewFile();
		}
		BufferedWriter writer = bufferedWriter(new FileWriter(outFile));
		writer.write(fileContent);
		writer.flush();
		writer.close();
		return outFile;
	}
}
