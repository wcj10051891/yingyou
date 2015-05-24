package com.shadowgame.rpg.jmx;

import javax.management.MXBean;

/**
 * @author wcj10051891@gmail.com
 */
@MXBean
public interface IRemote {
	String run(String srcWithRunMethod);
	String redefineClass(String src);
}
