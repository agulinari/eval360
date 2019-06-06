package com.gire.eval360.automation.domain;

public enum Relationship {
	
	AUTO, JEFE, PAR, SUBORDINADO;
	
	public static boolean contains(String test) {

	    for (Relationship r : Relationship.values()) {
	        if (r.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

}
