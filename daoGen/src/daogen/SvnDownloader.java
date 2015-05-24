package daogen;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;

public abstract class SvnDownloader {
	
	public static final String Svn_Source_Path_URL = "http://svn.wapmy.cn/svn/netgameserver/slg/wabao-slgserver/src";
	public static final String Username = "wchengjie";
	public static final String Password = "wangcj(*)!@#";

	static {
		DAVRepositoryFactory.setup();
	}

	private static final Map<String, SVNRepository> reps = new HashMap<String, SVNRepository>();

	/**
	 * 获取路径：svnSourcePathURL/*.java
	 * http://192.168.14.91/repos/arpg/dao/trunk/persist/src/java/ + com.cndw.rpg.persist.util.Utils.java
	 * 
	 * @param svnSourcePathURL	svn上java项目的源代码sourcePath路径
	 * @param fullClassName		要获取的java类的完整类名
	 * @param username			svn认证用户名
	 * @param password			svn认证密码
	 * @return
	 */
	public static synchronized byte[] getFile(String svnSourcePathURL, String fullClassName, String username,
			String password) {
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		SVNRepository repository = reps.get(svnSourcePathURL);		
		try {
			if (repository == null) {
				repository = SVNRepositoryFactory.create(
						SVNURL.parseURIDecoded(svnSourcePathURL), null);
				reps.put(svnSourcePathURL, repository);
				repository.setAuthenticationManager(new BasicAuthenticationManager(
						username, password));

			} else {
				repository = reps.get(svnSourcePathURL);
			}

			repository.getFile(fullClassName.replace('.', '/') + ".java",
					SVNRevision.HEAD.getNumber(), null, result);

		} catch (Exception e) {
//			throw new RuntimeException("fetch svn file error.", e);
		    return null;
		} finally {
			repository.closeSession();
		}
		return result.toByteArray();
	}
	
	public static synchronized byte[] getFile(String classFullName){
		return getFile(Svn_Source_Path_URL, classFullName, Username, Password);
	}
	
	public static void main(String[] args){
		byte[] data = getFile("http://192.168.14.91/repos/arpg/dao/trunk/persist/src/java", "com.cndw.xianhun.persist.entity.Executes",
				"chengjie.wang", "wcj10051891");
		
		System.out.println(new String(data));
	}
}
