package xgame.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class ClassUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object value, Class<T> toType) {
		if(toType == Byte.class || toType == byte.class)
			return value instanceof Byte ? (T)value : (T) Byte.valueOf(String.valueOf(value));
		if(toType == Boolean.class || toType == boolean.class)
			return value instanceof Boolean ? (T)value : (T) Boolean.valueOf(String.valueOf(value));
		if(toType == Short.class || toType == short.class)
			return value instanceof Short ? (T)value : (T) Short.valueOf(String.valueOf(value));
		if(toType == Integer.class || toType == int.class)
			return value instanceof Integer ? (T)value : (T) Integer.valueOf(String.valueOf(value));
		if(toType == Long.class || toType == long.class)
			return value instanceof Long ? (T)value : (T) Long.valueOf(String.valueOf(value));
		if(toType == Float.class || toType == float.class)
			return value instanceof Float ? (T)value : (T) Float.valueOf(String.valueOf(value));
		if(toType == Double.class || toType == double.class)
			return value instanceof Double ? (T)value : (T) Double.valueOf(String.valueOf(value));

		return (T)value;
	}
	
	private static class InternalClassLoader extends URLClassLoader {
		public InternalClassLoader(URL[] urls) {
			super(urls);
		}
		@Override
		public void addURL(URL url) {
			super.addURL(url);
		}
	}
	
	/**
	 * 加载指定目录下的class，多个目录用逗号分隔
	 * 目录名称必须为包名目录的起始根目录，
	 * 如/project/src/com/xxx/A.class，则目录为/project/src/
	 * 如果是加载目录下的所有jar包里的class，则目录名无限制
	 * @param dirs
	 * @return
	 */
	public static List<Class<?>> scanDir(String dirs) {
		InternalClassLoader loader = new InternalClassLoader(new URL[]{ClassUtils.class.getResource("/")});
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(String dir : dirs.split(",")) {
			File dirFile = new File(dir);
			if(!dirFile.isDirectory())
				throw new IllegalArgumentException(dir + " is not a directory.");
			try {
				loader.addURL(dirFile.toURI().toURL());
				String packageRootPath = dirFile.getCanonicalPath();
				if(!packageRootPath.endsWith(File.separator))
					packageRootPath += File.separator;
				result.addAll(loadInDir(loader, packageRootPath, dirFile));
			} catch (Exception e) {
				throw new IllegalArgumentException("scan path error.", e);
			} finally {
				try {
					loader.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}
	
	private static List<Class<?>> loadInDir(InternalClassLoader loader, String packageRootPath, File dir)  throws Exception {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (File file : dir.listFiles()) {
			URL url = file.toURI().toURL();
			loader.addURL(url);
			if("jar".equals(url.getProtocol())) {
				result.addAll(loadInJar(url));
			} else if(file.getName().endsWith(".jar")) {
				result.addAll(loadInJar(loader, file.getCanonicalPath(), ""));
			} else if (file.isDirectory()){
				result.addAll(loadInDir(loader, packageRootPath, file));
			} else if(file.getName().endsWith(".class")){
				String className = file.getCanonicalPath().replace(packageRootPath, "").replace(File.separator, ".").replace(".class", "");
				result.add(loader.loadClass(className));
			}
		}
		return result;
	}

	/**
	 * 在classpath下扫描指定包列表（多个用逗号分隔，如java.lang,java.util）中的所有class
	 * @param packageNames
	 * @return
	 */
	public static List<Class<?>> scanPackage(String packageNames) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(String packageName : packageNames.split(",")) {
			URL root = ClassUtils.class.getResource(
					(packageName.startsWith("/") ? "" : "/") + 
					packageName.replace(".", "/"));
			if(root == null)
				throw new NullPointerException("package " + packageName + " not exists.");
			try {
				if("jar".equals(root.getProtocol())) {
					result.addAll(loadInJar(root));
				} else {
					result.addAll(loadInDir(packageName, new File(root.toURI())));
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("scan package error.", e);
			}
		}
		return result;
	}
	
	private static List<Class<?>> loadInJar(URL jarUrl) throws Exception {
		String filePath = jarUrl.getFile();
		if(!filePath.startsWith("file:"))
			throw new FileNotFoundException(
				jarUrl + " cannot be resolved to absolute file path because it does not reside in the file system.");
		
		String[] s = filePath.split("!");
		return loadInJar(Thread.currentThread().getContextClassLoader(), s[0].replace("file:", ""), s[1].substring(1));
	}
	
	@SuppressWarnings("unchecked")
	private static List<Class<?>> loadInJar(ClassLoader loader, String jarPath, String packagePrefix) throws Exception {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(new File(jarPath));
			List<Class<?>> result = new ArrayList<Class<?>>();
			Enumeration<JarEntry> entries = jarFile.entries();
			while(entries.hasMoreElements()) {
				JarEntry element = entries.nextElement();
				String elementName = element.getName();
				if(elementName.endsWith(".class") && elementName.startsWith(packagePrefix))
					result.add(loader.loadClass(elementName.replace("/", ".").replace(".class", "")));
			}
			return result;
		} catch(Exception e) {
			return Collections.EMPTY_LIST;
		} finally {
			jarFile.close();
		}
	}

	private static List<Class<?>> loadInDir(String packageName, File directory) throws Exception {
		String package_path = packageName.replace(".", File.separator) + File.separator;
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (File file : directory.listFiles()) {
			if(file.isDirectory()){
				result.addAll(loadInDir(packageName, file));
			}else{
				if(file.getName().endsWith(".class")){
					String path = file.getCanonicalPath();
					String classFullname = path.substring(path.indexOf(package_path), path.length() - 6).replace(File.separator, ".");
					result.add(Class.forName(classFullname));
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
//		List<Class<?>> classes = listDirectoryInJar(ClassUtils.class.getResource("/flex/messaging/log"));
//		for (Class<?> class1 : classes) {
//			System.out.println(class1.getName());
//		}
//		List<Class<?>> cs = scanDir("F:/eclipse/wabaowork/wabao-slgserver-protos/bin");
//		System.out.println(cs);
//		URLClassLoader =ntln(loader.loadClass("com.wabao.slg.modules.protocol.dto.ProtoBufDtoProto"));
		
		System.out.println(ClassUtils.scanDir("F:/eclipse/wabaowork/wabao-dao/bin"));
	}
}
