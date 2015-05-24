package daogen;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Utils {
	public static String firstUpperCase(String arg){
		char first = arg.charAt(0);
		return arg.replaceFirst(String.valueOf(first), String.valueOf(Character.toUpperCase(first)));
	}
	
	public static String firstLowerCase(String arg){
		char first = arg.charAt(0);
		return arg.replaceFirst(String.valueOf(first), String.valueOf(Character.toLowerCase(first)));
	}
	
	public static void writeFile(File o, String content){
		if(!content.isEmpty()){
			try {
				FileOutputStream fout = new FileOutputStream(o);
				fout.write(content.getBytes());
				fout.flush();
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String genericMark(int size){
		if(size <= 0)
			return "";
		List<String> str = new ArrayList<String>();
		for(int i=0;i<size;i++){
			str.add("?");
		}
		return org.apache.commons.lang.StringUtils.join(str, ",");
	}
}
