package xgame.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.instrument.ClassDefinition;
import java.net.URI;
import java.util.Arrays;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class StringSourceCompiler {
	private static ThreadLocal<byte[]> byteCode = new ThreadLocal<byte[]>();
	private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	private static String getFullyQualifiedClassName(String sourceCode) {
		String packageName = "";
		int packageIndex = sourceCode.indexOf("package ");
		if(packageIndex != -1 && (packageIndex == 0 || Character.isWhitespace(sourceCode.charAt(packageIndex - 1)))) {
			int semiIndex = sourceCode.indexOf(";", packageIndex + 8);
			packageName = sourceCode.substring(packageIndex + 8, semiIndex).trim();
		}
		String className = "";
		int classnameIndex = sourceCode.indexOf(" class ");
		if(classnameIndex != -1) {
			int openBraceIndex = sourceCode.indexOf("{", classnameIndex + 7);
			className = sourceCode.substring(classnameIndex + 7, openBraceIndex).trim();
			int superIndex = -1;
			if((superIndex = className.indexOf(" extends ")) != -1 || 
					(superIndex = className.indexOf(" implements ")) != -1) {
				className = className.substring(0, superIndex).trim();
			}
		}
		
		if("".equals(className))
			throw new RuntimeException("className not found.");
		
		return "".equals(packageName) ? className : packageName + "." + className;
	}

	public static ClassDefinition compile(String sourceCode){
		return compile(sourceCode, null);
	}
	public static ClassDefinition compile(String sourceCode, Writer compileError){
		String fullyQualifiedClassName = getFullyQualifiedClassName(sourceCode);
		DynamicFileManager fileManager = new DynamicFileManager(compiler.getStandardFileManager(null, null, null));
		try {
			if(compiler.getTask(compileError, fileManager, null, null, null, Arrays.asList(new StringJavaFileObject(fullyQualifiedClassName, sourceCode))).call()){
				try {
					return new ClassDefinition(new DynamicClassLoader().loadClass(fullyQualifiedClassName), byteCode.get());
				} catch (Exception e) {
					throw new RuntimeException("compile success, but loadClass=" + fullyQualifiedClassName + "error.", e);
				}
			} else {
				throw new RuntimeException("string source compile error:" + fullyQualifiedClassName + "->\n" + sourceCode);
			}
		} finally {
			byteCode.set(null);
			try {fileManager.close();} catch (IOException e) {}
		}
	}

static class DynamicFileManager extends ForwardingJavaFileManager<JavaFileManager> {
	protected DynamicFileManager(JavaFileManager fileManager) {
		super(fileManager);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind, FileObject outputFile)
			throws IOException {
		return new StringJavaFileObject(qualifiedName, kind);
	}
}

static class DynamicClassLoader extends ClassLoader {
	
	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		byte[] code = byteCode.get();
		return defineClass(className, code, 0, code.length);
	}
}

static class StringJavaFileObject extends SimpleJavaFileObject {
	private String sourceCode;
	private ByteArrayOutputStream codeOut = new ByteArrayOutputStream() {
		public synchronized void write(byte[] b, int off, int len) {
			super.write(b, off, len);
			byteCode.set(this.buf);
		}
	};

	StringJavaFileObject(String className, String code) {
		super(toURI(className), Kind.SOURCE);
		this.sourceCode = code;
	}

	private static URI toURI(String className) {
		return URI.create("string:///" + className.replace(".", "/") + Kind.SOURCE.extension);
	}

	public StringJavaFileObject(String className, Kind kind) {
		super(toURI(className), kind);
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return codeOut;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return sourceCode;
	}
}
}