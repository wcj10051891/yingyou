package jprotobufgen;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

public class Proto2Cs {
	private static final String protogen = "./protobuf-net/protogen";

	public static void gen(String protoFile, String csFile) throws Exception {
//		List<String> protos = new ArrayList<String>();
//		list(new File(proto_file_path), protos);
		protoc(protoFile, csFile);
		System.out.println("proto compile success.");
	}
//
//	private static void list(File file, List<String> filepaths) throws Exception {
//		if (file.isDirectory()) {
//			for (File f : file.listFiles())
//				list(f, filepaths);
//		} else {
//			String filepath = file.getCanonicalPath();
//			if (filepath.endsWith(".proto"))
//				filepaths.add(filepath);
//		}
//	}

	private static void protoc(String protofile, String csFile) throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder(cmds(protogen + " -i:" + protofile + " -o:" + csFile));
		Process process = processBuilder.start();
		String error = new String(readBin(process.getErrorStream()));
		if (error.length() > 0) {
			System.err.println(protofile + " gen failure:" + error);
		}
		process.destroy();
	}

	private static String[] cmds(String cmd) {
		StringTokenizer st = new StringTokenizer(cmd);
		int n = st.countTokens();
		String[] result = new String[n];
		for (int i = 0; st.hasMoreTokens(); i++)
			result[i] = st.nextToken();
		return result;
	}

	private static byte[] readBin(InputStream in) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
		return out.toByteArray();
	}
}