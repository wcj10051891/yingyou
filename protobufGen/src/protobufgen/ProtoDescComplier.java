package protobufgen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Ignore
public class ProtoDescComplier {
	private static final String protoc = "./protoc.exe";
	private static final String proto_file_path = "proto-out";
	private static final String descriptor_set_out = "../rpg/src/main/resources/proto-desc-out";

	public static void main(String[] args) throws Exception {
		List<String> protos = new ArrayList<String>();
		list(new File(proto_file_path), protos);
		String fpath = new File(proto_file_path).getCanonicalPath();
		for (String protoFile : protos) {
			int fromIndex = protoFile.lastIndexOf(File.separator) + 1;
			int endIndex = protoFile.indexOf(".proto");
			String descFilename = protoFile.substring(fromIndex, endIndex);
			protoc(fpath, descriptor_set_out + "/" + descFilename + ".desc", protoFile);
		}
		System.out.println("proto desc compile success.");
	}

	private static void list(File file, List<String> filepaths) throws Exception {
		if (file.isDirectory()) {
			for (File f : file.listFiles())
				list(f, filepaths);
		} else {
			String filepath = file.getCanonicalPath();
			if (filepath.endsWith(".proto"))
				filepaths.add(filepath);
		}
	}

	private static void protoc(String I, String descriptor_set_out, String protoFile) throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder(cmds(protoc + " -I" + I + " --descriptor_set_out=" + descriptor_set_out + " " + protoFile));
		Process process = processBuilder.start();
		String error = new String(readBin(process.getErrorStream()));
		if (error.length() > 0) {
			System.err.println(protoFile + " gen failure:" + error);
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