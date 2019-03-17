package com.gire.eval360.projects.domain;

public enum Relationship {
	
	JEFE, PAR, SUBORDINADO;
	
	public static boolean contains(String test) {

	    for (Relationship r : Relationship.values()) {
	        if (r.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}

}
