package xgame.core.net.remote.phprpc;

import java.lang.management.ManagementFactory;
import java.util.jar.JarFile;

import xgame.core.util.Dynamic;
import xgame.core.util.InstrumentAgent;

import com.sun.tools.attach.VirtualMachine;

public class HotSwaper {

	public boolean redefineClass(String sourceCode) throws Exception {
		return Dynamic.redifineClass(sourceCode);
	}

	public Object run(String sourceCode) throws Exception {
		return Dynamic.run(sourceCode);
	}

	// 添加一个jar文件到类搜索路径中
	public String addJarFile(String jarPath) throws Exception {
		JarFile jf = new JarFile(jarPath);
		InstrumentAgent.instrument.appendToBootstrapClassLoaderSearch(jf);
		InstrumentAgent.instrument.appendToSystemClassLoaderSearch(jf);
		return "add " + jarPath;
	}
	
	public String attach(String jarPath) {
		String vmId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		try {
			VirtualMachine vm = VirtualMachine.attach(vmId);
			vm.loadAgent(jarPath);
			vm.detach();
		} catch (Exception e) {
			return jarPath + " agent load failure:" + e.toString();
		}
		return jarPath + " agent load success.";
	}
}