package com.shadowgame.rpg.remote.phprpc;



public class Status {
    public int online() {
    	try {
			System.out.println("abcde");
	    	return 1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
	    	return 2;
		}
    }
}
