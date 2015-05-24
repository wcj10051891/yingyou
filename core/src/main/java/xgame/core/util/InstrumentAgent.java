package xgame.core.util;

import java.lang.instrument.Instrumentation;

public class InstrumentAgent {
	public static Instrumentation instrument;
	
	public static void premain(String arg, Instrumentation instance) {
		instrument = instance;
	}
	
	public static void agentmain(String arg, Instrumentation instance) {
		instrument = instance;
	}

}
