package com.gire.eval360.reports.domain;

public enum Score {
	
	NONE("tdnone", ""), SMALL("tdsmall", "BAJA"), MEDIUM("tdmedium", "MEDIA"), LARGE("tdlarge", "ALTA");
	
	private final String cssClass;
	private final String name;
	
	private Score(String cssClass, String name) {
		this.cssClass = cssClass;
		this.name = name;
	}
	
	public String getCssClass() {
		return cssClass;
	}
	
	public String getName() {
		return name;
	}

}
